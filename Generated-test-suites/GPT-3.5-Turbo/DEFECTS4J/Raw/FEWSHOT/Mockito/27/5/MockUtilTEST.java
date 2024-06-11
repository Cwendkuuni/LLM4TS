package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        // Test case when class is null
        try {
            mockUtil.createMock(null, new MockSettingsImpl());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("classToMock cannot be null", e.getMessage());
        }

        // Test case with valid class and settings
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        // Test case when mock is null
        try {
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case with valid mock
        Object mock = createMock();
        mockUtil.resetMock(mock);
        // Verify that the resetMock method was executed successfully
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        MockUtil mockUtil = new MockUtil();
        // Test case when mock is not a Mockito mock
        Object mock = new Object();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNull(interceptor);

        // Test case when mock is a Mockito mock
        mock = createMock();
        interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        // Test case with null mock
        assertFalse(mockUtil.isMock(null));

        // Test case with non-Mockito mock
        Object mock = new Object();
        assertFalse(mockUtil.isMock(mock));

        // Test case with Mockito mock
        mock = createMock();
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        // Test case when mock is null
        try {
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case with valid mock
        Object mock = createMock();
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals(MockSettingsImpl.DEFAULT_MOCK_NAME, mockName.toString());
    }

    // Helper method to create a Mockito mock
    private Object createMock() {
        return Mockito.mock(Object.class);
    }
}