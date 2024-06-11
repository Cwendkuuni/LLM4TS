package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.AnnotatedClass;
import org.mockito.internal.util.reflection.ArrayUtils;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        String mock = mockUtil.createMock(classToMock, settings);
        assertEquals(String.class, mock.getClass());
        assertNotNull(mock);

        mock = mockUtil.createMock(String.class, settings);
        assertEquals(String.class, mock.getClass());
        assertNotNull(mock);

        settings.setExtraInterfaces(new Class[]{Serializable.class});
        mock = mockUtil.createMock(String.class, settings);
        assertEquals(String.class, mock.getClass());
        assertNotNull(mock);
        assertTrue(mock instanceof Serializable);

        settings.setSpiedInstance("hello");
        mock = mockUtil.createMock(String.class, settings);
        assertEquals("hello", mock.toString());

        settings.setExtraInterfaces(new Class[0]);
        settings.setSpiedInstance(null);
        mock = mockUtil.createMock(String.class, settings);
        assertNull(mock);

        MockUtil mockUtilNoValidation = new MockUtil();
        settings.setSpiedInstance("hello");
        mock = mockUtilNoValidation.createMock(String.class, settings);
        assertEquals("hello", mock.toString());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(Object.class, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(new Object(), new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithExtraInterfacesOfWrongType() {
        MockUtil mockUtil = new MockUtil();
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{Integer.class});
        mockUtil.createMock(String.class, settings);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        String mock = mock(String.class);
        mockUtil.resetMock(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
        verify(handler).getMockSettings();
    }

    @Test
    public void testIsMock() {
        Factory mock = mock(Factory.class);
        assertTrue(mockUtil.isMock(mock));

        Object notMock = new Object();
        assertFalse(mockUtil.isMock(notMock));
    }

    @Test
    public void testGetInterceptor() {
        String mock = mock(String.class);
        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mock);
        assertEquals(mock, interceptorFilter.getMock());
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals(MockName.MOCK_NAME_PREFIX + "String", mockName.getName());
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setDefaultAnswer(RETURNS_DEFAULTS);
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings);
        MockHandler<Object> mockHandler = spy(MockHandler.class);
        InvocationNotifierHandler<Object> invocationNotifierHandler = new InvocationNotifierHandler<>(mockHandler, settings);
        assertEquals(invocationNotifierHandler, filter.getInvocationNotifierHandler());
        assertEquals(settings, filter.getMockSettings());
    }

    @Test
    public void testValidateType() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<String> classToMock = String.class;
        mockUtil.validateType(classToMock);

        Class<Object> clazz = Object.class;
        try {
            mockUtil.validateType(clazz);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testValidateExtraInterfaces() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<String> classToMock = String.class;
        Class[] extraInterfaces = new Class[0];
        mockUtil.validateExtraInterfaces(classToMock, extraInterfaces);

        Class<String> classToMock2 = String.class;
        Class<Number> numberClass = Number.class;
        Class[] extraInterfaces2 = new Class[]{numberClass};
        try {
            mockUtil.validateExtraInterfaces(classToMock2, extraInterfaces2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        Class<String> classToMock3 = String.class;
        Class<Number> numberClass2 = Number.class;
        Class<String> stringClass = String.class;
        Class[] extraInterfaces3 = new Class[]{numberClass2, stringClass};
        mockUtil.validateExtraInterfaces(classToMock3, extraInterfaces3);
    }

    @Test
    public void testValidateMockedType() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<String> classToMock = String.class;
        Object spiedInstance = new Object();
        mockUtil.validateMockedType(classToMock, spiedInstance);

        Class<Object> clazz = Object.class;
        try {
            mockUtil.validateMockedType(clazz, spiedInstance);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        Class<String> classToMock2 = String.class;
        Object spiedInstance2 = new String("hello");
        mockUtil.validateMockedType(classToMock2, spiedInstance2);

        Class<String> classToMock3 = String.class;
        Object spiedInstance3 = new Object()[0];
        try {
            mockUtil.validateMockedType(classToMock3, spiedInstance3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        Class<String> classToMock4 = String.class;
        Object spiedInstance4 = new String[0];
        try {
            mockUtil.validateMockedType(classToMock4, spiedInstance4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInitiateMockName() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        mockUtil.initiateMockName(classToMock, settings);
        assertEquals(classToMock.getSimpleName(), settings.getMockName().getName());
    }

    @Test
    public void testImposterise() {
        Class<?> classToMock = MockitoMockitoMockitoMockito.class;
        InvocationHandler handler = mock(InvocationHandler.class);
        Proxy proxy = Proxy.newProxyInstance(ClassUtil.class.getClassLoader(), new Class[]{classToMock}, handler);

        ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();

        when(imposterizer.imposterise(any(MethodInterceptorFilter.class), eq(classToMock), anyArray())).thenReturn(proxy);
        when(mockHandler.getMockSettings()).thenReturn(settings);

        Object mock = mockUtil.createMock(classToMock, settings);
        assertEquals(proxy, mock);
    }

    @Test
    public void testCopyToMock() {
        String spiedInstance = "hello";
        Object mock = mock(Object.class);

        LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);
        MockHandler<Object> mockHandler = mock(MockHandler.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(lenientCopyTool.copyToMock(spiedInstance, mock)).thenReturn(mock);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
        mockUtil.copyToMock(spiedInstance, mock);

        verify(mockHandler, times(1)).copyToMock(spiedInstance, mock);
    }
}