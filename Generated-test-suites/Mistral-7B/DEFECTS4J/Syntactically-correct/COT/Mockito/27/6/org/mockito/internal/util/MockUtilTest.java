package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.LenientCopyTool.LenientCopyToolException;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.*;

public class MockUtilTest {
    private MockUtil mockUtil;
    private ClassImposterizer classImposterizer;
    private Factory factory;
    private MockSettingsImpl settings;

    @Before
    public void setUp() {
        mockUtil = new MockUtil(new MockCreationValidator() {
            @Override
            public void validateType(Class<?> clazz) {
                // Do nothing
            }

            @Override
            public void validateExtraInterfaces(Class<?> clazz, Class<?>[] extraInterfaces) {
                // Do nothing
            }

            @Override
            public void validateMockedType(Class<?> clazz, Object spiedInstance) {
                // Do nothing
            }

            @Override
            public void initiateMockName(Class<?> clazz) {
                // Do nothing
            }
        });
        classImposterizer = mock(ClassImposterizer.class);
        factory = mock(Factory.class);
        settings = new MockSettingsImpl();
    }

    @Test
    public void testCreateMock() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, LenientCopyToolException {
        Class<String> classToMock = String.class;
        when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), eq(classToMock), anyArray()))
                .thenReturn(mock(String.class));
        MockHandler<String> mockHandler = mock(MockHandler.class);
        InvocationNotifierHandler<String> invocationNotifierHandler = mock(InvocationNotifierHandler.class);
        MethodInterceptorFilter methodInterceptorFilter = new MethodInterceptorFilter(invocationNotifierHandler, settings);
        when(factory.getCallback(0)).thenReturn(methodInterceptorFilter);
        when(invocationNotifierHandler.getMockHandler()).thenReturn(mockHandler);

        String mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertEquals(classToMock, mock.getClass());

        MockHandlerInterface<String> mockHandlerInterface = mockUtil.getMockHandler(mock);
        Assert.assertEquals(mockHandler, mockHandlerInterface.getHandler());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        mockUtil.createMock(null, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNotMockType() {
        Class<Integer> integerClass = Integer.class;
        mockUtil.createMock(integerClass, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstanceOfDifferentType() {
        Class<String> classToMock = String.class;
        Object spiedInstanceOfInteger = 1;
        mockUtil.createMock(classToMock, new MockSettingsImpl().spiedInstance(spiedInstanceOfInteger));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithExtraInterfacesOfDifferentType() {
        Class<String> classToMock = String.class;
        Class<?>[] extraInterfaces = new Class[]{Integer.class};
        mockUtil.createMock(classToMock, new MockSettingsImpl().extraInterfaces(extraInterfaces));
    }

    @Test
    public void testResetMock() throws Exception {
        String mock = mock(String.class);
        mockUtil.resetMock(mock);
        verify(mock, times(1)).reset();
    }

    @Test(expected = NullPointerException.class)
    public void testResetMockWithNullMock() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(String.class);
        Assert.assertTrue(mockUtil.isMock(mock));
        Object notAMock = new String("mockito");
        Assert.assertFalse(mockUtil.isMock(notAMock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMockWithMock() {
        String mock = mock(String.class);
        Assert.assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockWithNotAMock() {
        Object notAMock = new String("mockito");;
        Assert.assertFalse(mockUtil.isMockitoMock(notAMock));
    }

    @Test
    public void testGetInterceptorWithMock() {
        String mock = mock(String.class);
        MethodInterceptorFilter methodInterceptorFilter = mockUtil.getInterceptor(mock);
        Assert.assertNotNull(methodInterceptorFilter);
    }

    @Test
    public void testGetInterceptorWithNotAMock() {
        Object notAMock = new String("mockito");
        MethodInterceptorFilter methodInterceptorFilter = mockUtil.getInterceptor(notAMock);
        Assert.assertNull(methodInterceptorFilter);
    }

    @Test
    public void testGetMockNameWithMock() {
        String mock = mock(String.class);
        String expectedMockName = "mockString";
        settings.initiateMockName(String.class);

        MockHandler<String> mockHandler = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(mockHandler.getMockName()).thenReturn(expectedMockName);

        MockHandlerInterface<String> mockHandlerInterface = mockUtil.getMockHandler(mock);
        Assert.assertEquals(mockHandler, mockHandlerInterface.getHandler());
        Assert.assertEquals(expectedMockName, mockUtil.getMockName(mock));
    }

    @Test
    public void testGetMockNameWithFactoryMock() {
        String mock = mock(Factory.class);
        String expectedMockName = "mockFactory";
        settings.initiateMockName(Factory.class);

        MockHandler<Factory> mockHandler = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(mockHandler.getMockName()).thenReturn(expectedMockName);

        Whitebox.setInternalState(mock, "handler", mockHandler);
        MockHandlerInterface<Factory> mockHandlerInterface = mockUtil.getMockHandler(mock);
        Assert.assertEquals(mockHandler, mockHandlerInterface.getHandler());
        Assert.assertEquals(expectedMockName, mockUtil.getMockName(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNotAMock() {
        Object notAMock = new String("mockito");
        mockUtil.getMockName(notAMock);
    }
}