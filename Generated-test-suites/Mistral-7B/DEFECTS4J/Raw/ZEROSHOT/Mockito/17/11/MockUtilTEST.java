package org.mockito.internal.util.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoConstants;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockHandler;
import org.mockito.internal.creation.Factory;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory0;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.core.ReflectUtils;
import org.mockito.cglib.core.KeyFactory;
import org.mockito.cglib.proxy.MethodInfo;
import org.mockito.cglib.proxy.MethodHandler;
import org.mockito.internal.util.MockUtilTest.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MockUtilTest {
    private static final Class<? extends MockHandler<Object>> MOCK_HANDLER_TYPE = MockHandler.class;
    private static final Class<? extends MethodInterceptorFilter> METHOD_INTERCEPTOR_FILTER_TYPE = MethodInterceptorFilter.class;
    private static final Class<? extends Factory<Object>> FACTORY_TYPE = Factory.class;
    private static final Class<? extends Callback> CALLBACK_TYPE = Callback.class;
    private static final Class<? extends MethodInterceptor> METHOD_INTERCEPTOR_TYPE = MethodInterceptor.class;

    private CreationValidator creationValidator;
    private MockUtil mockUtil;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() throws Exception {
        // Test creating a mock with no extra interfaces
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getExtraInterfaces()).thenReturn(new Class[0]);
        when(mockSettingsClass.getSpiedInstance()).thenReturn(new Object());
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        when(creationValidator.validateType(MockName.class)).thenReturn(null);
        when(creationValidator.validateType(Factory.class)).thenReturn(null);
        when(creationValidator.validateMockedType(MockName.class, new Object())).thenReturn(null);
        mockUtil.createMock(MockName.class, mockSettingsClass);
        verify(creationValidator, times(3)).validateType(MockName.class);
        verifyNoMoreInteractions(creationValidator);
        verify(mockSettingsClass).getExtraInterfaces();
        verify(mockSettingsClass).getSpiedInstance();
        verify(mockSettingsClass).getMockName();
        verifyNoInteractions(ClassImposterizer.class);
        verifyNoInteractions(LenientCopyTool.class);

        // Test creating a mock with extra interfaces
        Class<MyExtraInterface> extraInterfaceClass = mock(MyExtraInterface.class);
        when(mockSettingsClass.getExtraInterfaces()).thenReturn(new Class[]{extraInterfaceClass});
        mockUtil.createMock(MockName.class, mockSettingsClass);
        verify(creationValidator, times(5)).validateType(MockName.class);
        verify(creationValidator, times(2)).validateType(MyExtraInterface.class);
        verifyNoMoreInteractions(creationValidator);
        verify(mockSettingsClass).getExtraInterfaces();
        verifyNoInteractions(ClassImposterizer.class);
        verifyNoInteractions(LenientCopyTool.class);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNotAMock() {
        Object notAMock = new Object();
        mockUtil.createMock(notAMock.getClass(), new MockSettingsImpl());
    }

    @Test
    public void testResetMock() throws Exception {
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        when(mockSettingsClass.getSpiedInstance()).thenReturn(new Object());
        Factory factory = mock(Factory.class);
        when(factory.getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));
        when(factory.getHandler()).thenReturn(mock(MethodHandler.class));
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        when(filter.getMockHandler()).thenReturn(mockHandler(mockNameClass, mockSettingsClass, factory));
        when(factory.setCallback(0, anyObject())).thenAnswer(i -> factory);

        Object mock = mockUtil.createMock(MockName.class, mockSettingsClass);
        mockUtil.resetMock(mock);
        verify(factory).setCallback(0, filter);
        verifyNoMoreInteractions(factory);
        verifyNoInteractions(mockSettingsClass);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNotAMock() {
        Object notAMock = new Object();
        mockUtil.resetMock(notAMock);
    }

    @Test
    public void testGetMockHandler() throws Exception {
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        when(mockSettingsClass.getSpiedInstance()).thenReturn(new Object());
        when(creationValidator.validateType(MockName.class)).thenReturn(null);
        when(creationValidator.validateType(Factory.class)).thenReturn(null);
        when(creationValidator.validateMockedType(MockName.class, new Object())).thenReturn(null);
        Object mock = mockUtil.createMock(MockName.class, mockSettingsClass);
        MockHandler<Object> mockHandler = mockHandler(mockNameClass, mockSettingsClass, mockFactory(mockHandlerType));
        when(mockHandler.getMockSettings()).thenReturn(mockSettingsClass);
        when(getInterceptor(mock)).thenReturn(mockHandler);
        Object result = mockUtil.getMockHandler(mock);
        assertEquals(mockHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNotAMock() {
        Object notAMock = new Object();
        mockUtil.getMockHandler(notAMock);
    }

    @Test
    public void testIsMock() throws Exception {
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        Object mock = mockUtil.createMock(MockName.class, mockSettingsClass);
        assertTrue(mockUtil.isMock(mock));
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetInterceptor() throws Exception {
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        when(mockSettingsClass.getSpiedInstance()).thenReturn(new Object());
        when(creationValidator.validateType(MockName.class)).thenReturn(null);
        when(creationValidator.validateType(Factory.class)).thenReturn(null);
        when(creationValidator.validateMockedType(MockName.class, new Object())).thenReturn(null);
        Object mock = mockUtil.createMock(MockName.class, mockSettingsClass);
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        when(filter.getMockHandler()).thenReturn(mockHandler(mockNameClass, mockSettingsClass, mockFactory(mockHandlerType)));
        when(mockSettingsClass.getHandler()).thenReturn(filter);
        MethodInterceptor interceptor = mockInterceptor(mockNameClass, mockSettingsClass, mockFactory(methodInterceptorType));
        when(filter.getInterceptor()).thenReturn(interceptor);
        MethodHandler methodHandler = mockHandler(mockNameClass, mockSettingsClass, mockFactory(factoryType));
        when(interceptor.intercept(anyObject(), anyMethod(), anyArray(), anyObject())).thenCallRealMethod();
        when(mockFactory(mockHandlerType).getHandler()).thenReturn(methodHandler);
        MethodHandler result = mockUtil.getInterceptor(mock);
        assertEquals(methodHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithNotAMock() {
        Object notAMock = new Object();
        mockUtil.getInterceptor(notAMock);
    }

    @Test
    public void testGetMockName() throws Exception {
        Class<MockName> mockNameClass = mock(MockName.class);
        Class<MockSettingsImpl> mockSettingsClass = mock(MockSettingsImpl.class);
        when(mockSettingsClass.getMockName()).thenReturn(mockNameClass);
        when(mockNameClass.getName()).thenReturn("MockName");
        Object mock = mockUtil.createMock(MockName.class, mockSettingsClass);
        String result = mockUtil.getMockName(mock);
        assertEquals("MockName", result);
    }

    private MockHandler<Object> mockHandler(Class<MockName> mockNameClass, Class<MockSettingsImpl> mockSettingsClass, Factory<Object> factory) throws Exception {
        MockHandler<Object> handler = mock(MockHandler.class);
        when(handler.getMockSettings()).thenReturn(mockSettingsClass);
        when(handler.getFactory()).thenReturn(factory);
        when(handler.getMockName()).thenReturn(mockNameClass);
        return handler;
    }

    private Factory<Object> mockFactory(Class<? extends Factory<Object>> factoryType) throws Exception {
        Factory<Object> factory = mock(factoryType.asSubclass(Factory.class));
        when(factory.getHandler()).thenReturn(mockHandler(MOCK_HANDLER_TYPE, MockSettingsImpl.class, factory));
        when(factory.getCallback(0)).thenReturn(mock(Callback.class));
        return factory;
    }

    private MethodInterceptor mockInterceptor(Class<MockName> mockNameClass, Class<MockSettingsImpl> mockSettingsClass, Factory<Object> factory) throws Exception {
        MethodInterceptor interceptor = mock(methodInterceptorType.asSubclass(MethodInterceptor.class));
        when(interceptor.intercept(anyObject(), anyMethod(), anyArray(), anyObject())).thenCallRealMethod();
        when(interceptor.getHandler()).thenReturn(mockHandler(MOCK_HANDLER_TYPE, mockSettingsClass, factory));
        return interceptor;
    }
}

class MyExtraInterface {}