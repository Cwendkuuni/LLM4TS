package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.Mockito;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.internal.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.AnnotatedElementFinder;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldGetter;
import org.mockito.internal.creation.jmock.ClassImposterizer.MethodInterceptor;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class MockUtilTest {

    private CreationValidator creationValidator = mock(CreationValidator.class);
    private MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
    private MockHandler<Object> mockHandler = mock(MockHandler.class);
    private MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);
    private MethodInterceptor methodInterceptor = mock(MethodInterceptor.class);
    private ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
    private LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);

    @Test
    public void testCreateMock() {
        when(creationValidator.validateType(any(Class.class))).thenReturn(null);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][][].class))).thenReturn(null);
        when(creationValidator.validateMockedType(any(Class.class), any(Object.class))).thenReturn(null);

        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mockSettings;

        String mock = mockUtil.createMock(classToMock, settings);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());

        MockHandler<String> handler = mockHandler;
        MethodInterceptorFilter filter = methodInterceptorFilter;
        Class<?>[] ancillaryTypes = new Class<?>[0];

        when(classImposterizer.imposterise(eq(filter), eq(classToMock), ancillaryTypes)).thenReturn(mock);
        when(lenientCopyTool.copyToMock(any(), eq(mock))).thenReturn(mock);

        verify(creationValidator).initiateMockName(classToMock);
        verify(classToMock).getName();
        verify(handler).setMockSettings(settings);
        verify(filter).setHandler(handler);
        verify(filter).setMockSettings(settings);

        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
        assertEquals(settings, mockHandler.getMockSettings());
        assertEquals(handler, getInterceptor(mock).getHandler());

        when(creationValidator.validateType(any(Class.class))).thenThrow(new IllegalStateException());
        try {
            mockUtil.createMock(String.class, mockSettings);
            fail();
        } catch (IllegalStateException e) {
            //expected
        }

        when(creationValidator.validateMockedType(any(Class.class), any(Object.class))).thenThrow(new IllegalStateException());
        try {
            mockUtil.createMock(String.class, mockSettings);
            fail();
        } catch (IllegalStateException e) {
            //expected
        }

        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][][].class))).thenThrow(new IllegalStateException());
        try {
            mockUtil.createMock(String.class, mockSettings);
            fail();
        } catch (IllegalStateException e) {
            //expected
        }
    }

    @Test
    public void testResetMock() {
        when(mockSettings.getSpiedInstance()).thenReturn(new Object());

        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(mock);

        MockHandlerInterface<Object> oldMockHandler = getMockHandler(mock);
        MockHandler<Object> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));

        Method method = ReflectionUtils.findMethod(mock.getClass(), "getName");
        Object name = ReflectionUtils.invokeMethod(method, mock);
        assertEquals("MockitoMockName_" + ReflectionUtils.getShortName(mock.getClass()), name);

        assertNull(getInterceptor(mock).getHandler());
        assertEquals(newMockHandler, getInterceptor(mock).getHandler());
    }

    @Test
    public void testGetMockHandler() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        when(isMockitoMock(anyObject())).thenReturn(true);

        MockHandler<Object> handler = mockHandler;
        doReturn(handler).when(mockUtil).getInterceptor(mock);

        assertEquals(handler, mockUtil.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        Object mock = null;
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        when(isMockitoMock(anyObject())).thenReturn(false);

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        when(isMockitoMock(mock)).thenReturn(true);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        Factory factory = mock(Factory.class);
        Callback callback = mock(Callback.class);

        when(isMockitoMock(mock)).thenReturn(true);
        when(getMockHandler(mock).getHandler()).thenReturn(mockHandler);
        when(getMockHandler(mock).getInterceptor()).thenReturn(callback);
        when(factory.getCallback(0)).thenReturn(callback);

        assertEquals(callback, mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetInterceptorWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        assertNull(mockUtil.getInterceptor(null));
    }

    @Test
    public void testGetInterceptorWithNonMockitoMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();

        assertNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetMockName() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Object> handler = mockHandler;
        MockName mockName = new MockName("MockName");

        when(getMockHandler(mock).getMockSettings().getMockName()).thenReturn(mockName);

        assertEquals(mockName, mockUtil.getMockName(mock));
    }

    @Test
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        assertNull(mockUtil.getMockName(null));
    }
}