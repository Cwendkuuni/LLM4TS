package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.MockitoSession;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(new Object());

        MockUtil util = new MockUtil();
        Object mock = util.createMock(Object.class, settings);

        assertTrue(isMockitoMock(mock));
        assertNotNull(Mockito.mockConstructor(Object.class, settings.getExtraInterfaces()));

        Class<?>[] interfaces = settings.getExtraInterfaces();
        if (interfaces != null) {
            MockitoSession session = Mockito.mockSession();
            Mockito.mockStatic(Mockito.class);
            when(Mockito.mockStatic(Mockito.class).thenReturn(session));
            when(session.mock(Object.class)).thenReturn(mock);
            session.finishMocking();

            Object instance = Mockito.mock(Object.class, Arrays.asList(interfaces));
            assertEquals(mock, instance);
        }

        assertEquals(settings.getMockName().getName(), Whitebox.getInternalState(mock, "mockName"));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        new MockUtil().createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        new MockUtil().createMock(String.class, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidSettings() {
        new MockUtil().createMock(Object.class, null);
    }

    @Test
    public void testCreateMockWithEmptyExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil util = new MockUtil();
        Object mock = util.createMock(Object.class, settings);

        assertTrue(isMockitoMock(mock));
        assertArrayEquals(new Class[0], settings.getExtraInterfaces());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{String.class, Integer.class});

        MockUtil util = new MockUtil();
        Object mock = util.createMock(Object.class, settings);

        assertTrue(isMockitoMock(mock));
        assertArrayEquals(new Class[]{String.class, Integer.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        Object spiedInstance = new Object();

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);

        MockUtil util = new MockUtil();
        Object mock = util.createMock(Object.class, settings);

        assertTrue(isMockitoMock(mock));
        assertNotNull(spiedInstance);
        assertEquals(spiedInstance, Whitebox.getInternalState(mock, "mockBase"));
    }

    @Test
    public void testCreateMockWithNullSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil util = new MockUtil();
        Object mock = util.createMock(Object.class, settings);

        assertTrue(isMockitoMock(mock));
        assertNull(settings.getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockUtil util = new MockUtil();

        util.resetMock(mock);

        MockHandlerInterface<Object> handler = Mockito.mockStatic(Mockito.class).mockStatic().getMockHandler(mock);
        assertEquals(RETURNS_DEFAULTS, handler.getDefaultAnswer());
    }

    @Test(expected = NullPointerException.class)
    public void testResetMockWithNullMock() {
        MockUtil util = new MockUtil();
        util.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);

        MockUtil util = new MockUtil();

        assertEquals(Mockito.mockStatic(Mockito.class).mockStatic().getMockHandler(mock), util.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil util = new MockUtil();
        util.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(Object.class);
        assertTrue(new MockUtil().isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockWithNullObject() {
        assertFalse(new MockUtil().isMockitoMock(null));
    }

    @Test
    public void testIsMockitoMockWithNonMockitoMock() {
        Object nonMockitoMock = new Object();
        assertFalse(new MockUtil().isMockitoMock(nonMockitoMock));
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        assertTrue(new MockUtil().isMock(mock));
    }

    @Test
    public void testIsMockWithNullObject() {
        assertFalse(new MockUtil().isMock(null));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Object.class);

        MockUtil util = new MockUtil();

        MethodInterceptorFilter interceptor = util.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithNullMock() {
        MockUtil util = new MockUtil();
        util.getInterceptor(null);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);

        MockUtil util = new MockUtil();

        MockName mockName = util.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("mockObject", mockName.getName());

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockName(new MockName("testMockName"));
        MockUtil utilWithSettings = new MockUtil(settings);

        MockName customMockName = utilWithSettings.getMockName(mock);
        assertEquals("testMockName", customMockName.getName());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        MockUtil util = new MockUtil();
        util.getMockName(null);
    }

    @Test
    public void testNewLenientCopyTool() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        LenientCopyTool lenientCopyTool = new LenientCopyTool();

        Constructor<LenientCopyTool> constructor = LenientCopyTool.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        LenientCopyTool lenientCopyToolInstance = (LenientCopyTool) constructor.newInstance();

        assertEquals(lenientCopyTool, lenientCopyToolInstance);
    }

    @Test
    public void testMockHandler() {
        MockHandler<Object> mockHandler = new MockHandler<>(new MockSettingsImpl());

        assertEquals(0, mockHandler.getMockName().getIndex());
    }

    @Test
    public void testMockHandlerWithSettings() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockName(new MockName("testMockName"));
        MockHandler<Object> mockHandler = new MockHandler<>(settings);

        assertEquals("testMockName", mockHandler.getMockName().getName());
    }
}