package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.io.Serializable;

public class MockUtilTest {
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock() {
        Class<?> classToMock = TestClass.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        MockHandlerInterface<?> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getHandler()).thenReturn(mock(MockHandlerInterface.class).getHandler());
        when(mockUtil.getMockHandler(classToMock)).thenReturn(mockHandler);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(null);

        Object result = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, result.getClass());

        verify(mockUtil).createMock(classToMock, settings);
        verify(mockHandler).getMockSettings();
    }

    @Test
    public void testResetMock() {
        TestClass mock = mock(TestClass.class);

        MockHandlerInterface<TestClass> oldMockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(mockUtil.newMethodInterceptorFilter(null)).thenReturn(mock(MethodInterceptorFilter.class));
        when(org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS)).thenReturn(null);

        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, null);
    }

    @Test
    public void testGetMockHandler() {
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<TestClass> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getHandler()).thenReturn(mock(MockHandlerInterface.class).getHandler());
        when(mockUtil.getInterceptor(mock)).thenReturn(null);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        MockHandlerInterface<TestClass> result = mockUtil.getMockHandler(mock);
        assertEquals(mockHandler, result);

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testGetMockHandler_NullMock() {
        TestClass mock = null;
        when(mockUtil.getMockHandler(mock)).thenThrow(new NullPointerException("Argument should be a mock, but is null!"));

        try {
            mockUtil.getMockHandler(mock);
        } catch (NullPointerException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testGetMockHandler_NotMock() {
        TestClass mock = mock(TestClass.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        try {
            mockUtil.getMockHandler(mock);
        } catch (Exception e) {
            assertEquals("Argument should be a mock, but is: TestClass", e.getMessage());
        }

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        TestClass mock = mock(TestClass.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        boolean result = mockUtil.isMock(mock);
        assertEquals(true, result);

        verify(mockUtil).isMock(mock);
    }

    @Test
    public void testIsMock_NotMock() {
        Object mock = new Object();
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        boolean result = mockUtil.isMock(mock);
        assertEquals(false, result);

        verify(mockUtil).isMock(mock);
    }

    @Test
    public void testGetMockName() {
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface<TestClass> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        MockName result = mockUtil.getMockName(mock);
        assertEquals(mockName, result);

        verify(mockUtil).getMockName(mock);
    }

    private static class TestClass implements Serializable {
    }
}