package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock() {
        // Positive test case
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, mockSettings);
        assertNotNull(mock);

        // Negative test case
        try {
            ClassToMock notMock = new ClassToMock();
            mockUtil.createMock(notMock.getClass(), mockSettings);
            fail("Should have thrown NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + ClassToMock.class, e.getMessage());
        }
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = mock(ClassToMock.class);
        mockUtil.resetMock(mock);
        // Verify mock was reset
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testGetMockHandler() {
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock() {
        ClassToMock mock = mock(ClassToMock.class);
        boolean isMock = mockUtil.isMock(mock);
        assertTrue(isMock);

        ClassToMock notMock = new ClassToMock();
        isMock = mockUtil.isMock(notMock);
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = mock(ClassToMock.class, withSettings().name("mockName"));
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("mockName", mockName.toString());
    }

    private static class ClassToMock {
        // Empty class used for testing purposes
    }
}