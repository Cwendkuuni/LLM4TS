package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {
    
    @Test
    public void testCreateMockWithMockSettings() {
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Class<?> classToMock = Object.class;

        Object mock = mockUtil.createMock(classToMock, mockSettings);

        assertNotNull(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidType() {
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Class<?> classToMock = null;

        mockUtil.createMock(classToMock, mockSettings);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);

        mockUtil.resetMock(mock);

        // Verify that no exception is thrown.
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockUtil mockUtil = new MockUtil();
        
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(mockSettings);

        assertNotNull(filter);
    }

    @Test
    public void testGetMockHandlerWithValidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);

        MockHandlerInterface handler = mockUtil.getMockHandler(mock);

        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullArgument() {
        MockUtil mockUtil = new MockUtil();
        Object mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMockWithValidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);

        boolean result = mockUtil.isMock(mock);

        assertEquals(true, result);
    }

    @Test
    public void testIsMockWithNullArgument() {
        MockUtil mockUtil = new MockUtil();
        Object mock = null;

        boolean result = mockUtil.isMock(mock);

        assertEquals(false, result);
    }

    @Test
    public void testIsMockWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        boolean result = mockUtil.isMock(mock);

        assertEquals(false, result);
    }

    @Test
    public void testGetInterceptorWithValidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertNotNull(interceptor);
    }

    @Test
    public void testGetInterceptorWithInvalidMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertNull(interceptor);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }
}