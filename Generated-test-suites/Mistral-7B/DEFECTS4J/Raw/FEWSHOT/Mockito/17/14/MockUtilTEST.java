package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;

import java.lang.reflect.Proxy;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        CreationValidator creationValidator = mock(CreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));

        settings.initiateMockName(classToMock);
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals(settings.getMockName(), mockName.getName());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl(Mockito.mock(Class.class), null, null));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(int.class, new MockSettingsImpl(Mockito.mock(Class.class), null, null));
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        CreationValidator creationValidator = mock(CreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, null);
        settings.setExtraInterfaces(new Class[]{StringBuilder.class});

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(settings.getMockName(), mockHandler.getMockName().getName());
        assertTrue(mockHandler.getExtraInterfaces().length > 0);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstanceOfDifferentType() {
        CreationValidator creationValidator = mock(CreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl(classToMock, "mockName", null, "spiedInstance");

        Proxy proxy = Proxy.getProxyInstance(classToMock.getClassLoader(), new Class[]{String.class}, null);
        settings.setSpiedInstance(proxy);

        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(String.class);

        mockUtil.resetMock(mock);
        MockHandlerInterface<String> newMockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(newMockHandler);

        verify(newMockHandler, times(0)).mockSettings();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        assertEquals(mock, mockHandler.getMock());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMock() {
        MockUtil mockUtil = new MockitoMockFactory();
        Object mock = mockUtil.createMock(String.class, new MockSettingsImpl(String.class, null, null));

        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockitoMockFactory();
        Object mock = mockUtil.createMock(String.class, new MockSettingsImpl(String.class, null, null));

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);

        verify(interceptor, times(1)).intercept(any(), any(), any());
    }

    @Test
    public void testGetInterceptorWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getInterceptor(null);
    }

    private static boolean isMockitoMock(Object mock) {
        return Enhancer.isEnhanced(mock.getClass()) && getInterceptor(mock) != null;
    }
}

class MockitoMockFactory extends Mockito {
    @Override
    public <T> T mock(Class<T> type, MockSettings settings) {
        return super.mock(type, settingsWithExtraDefaultAnswer());
    }

    private MockSettings settingsWithExtraDefaultAnswer() {
        return withSettings().defaultAnswer(RETURNS_DEFAULTS);
    }
}