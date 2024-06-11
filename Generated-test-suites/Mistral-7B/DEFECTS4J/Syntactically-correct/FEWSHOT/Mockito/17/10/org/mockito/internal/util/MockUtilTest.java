package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        final Class<String> stringClass = String.class;
        final MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        assertEquals(settings.getMockName(stringClass), mock.getClass().getSimpleName() + "#");

        verify(settings).validateType(stringClass);
        verify(settings).validateExtraInterfaces(eq(stringClass), isA(Class[].class));
        verify(settings).validateMockedType(stringClass, isNull());
        verify(settings).initiateMockName(stringClass);

        // Check that the mock has been created using CGLIB
        Class<?> mockClass = Whitebox.getImplementedInterfaces(mock)[0];
        assertNotNull(mockClass);
        assertFalse(mockClass.isInterface());
        assertFalse(mockClass.isAnnotation());
        assertFalse(mockClass.isEnum());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        final Class<String> stringClass = String.class;
        final Class<Integer> intClass = Integer.class;
        final MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{intClass});

        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        assertEquals(settings.getMockName(stringClass), mock.getClass().getSimpleName() + "#");

        verify(settings).validateType(stringClass);
        verify(settings).validateExtraInterfaces(eq(stringClass), arrayContaining(intClass));
        verify(settings).validateMockedType(stringClass, isNull());
        verify(settings).initiateMockName(stringClass);

        // Check that the mock has been created with the extra interface
        Class<?> mockClass = Whitebox.getImplementedInterfaces(mock)[1];
        assertEquals(intClass, mockClass);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        final Class<String> stringClass = String.class;
        final String spiedString = "spied";
        final MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        assertEquals(settings.getMockName(stringClass), mock.getClass().getSimpleName() + "#");

        String spiedMock = spy(spiedString);
        settings.setSpiedInstance(spiedMock);

        mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        assertEquals(settings.getMockName(stringClass), mock.getClass().getSimpleName() + "#");

        verify(settings).validateType(stringClass);
        verify(settings).validateExtraInterfaces(eq(stringClass), isA(Class[].class));
        verify(settings).validateMockedType(stringClass, eq(spiedMock));
        verify(settings).initiateMockName(stringClass);

        // Check that the spied instance has been copied to the mock using LenientCopyTool
        LenientCopyTool lenientCopyTool = new LenientCopyTool();
        lenientCopyTool.copyToMock(spiedMock, mock);
        assertEquals(spiedString, mock.toString());
    }

    @Test
    public void testResetMock() {
        final Class<String> stringClass = String.class;
        final MockSettingsImpl settings = new MockSettingsImpl();
        final String mockString = "mock";

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        verify(mockHandler).getMockSettings().getMockName().startsWith("mockString#");

        mockUtil.resetMock(mock);

        mockHandler = mockUtil.getMockHandler(mock);
        verify(mockHandler).getMockSettings().getMockName().startsWith("MockitoMockString#");
        verify(mockHandler).getMockSettings().defaultAnswer(RETURNS_DEFAULTS);
    }

    @Test
    public void testIsMock() {
        Object mockObject = mock(String.class);
        assertTrue(new MockUtil().isMock(mockObject));

        Object nonMockObject = new Object();
        assertFalse(new MockUtil().isMock(nonMockObject));
    }

    @Test
    public void testIsMockitoMock() {
        Object mockObject = mock(String.class);
        assertTrue(new MockUtil(new CreationValidator()).isMockitoMock(mockObject));

        Object nonMockObject = new Object();
        assertFalse(new MockUtil(new CreationValidator()).isMockitoMock(nonMockObject));
    }

    @Test
    public void testGetInterceptor() {
        final String mockString = "mock";
        String mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertTrue(interceptor instanceof MethodInterceptorFilter);
        assertNotNull(interceptor.getHandler());

        MockHandler<String> mockHandler = (MockHandler<String>) interceptor.getHandler();
        assertEquals(mockString, mockHandler.getMockSettings().getMockName().get());
    }

    @Test
    public void testGetMockName() {
        final String mockString = "mock";
        Object mockObject = mock(String.class);

        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mockObject);

        assertEquals(mockString, mockName.get());
    }
}