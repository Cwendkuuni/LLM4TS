package org.mockito.internal.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Test case 1: classToMock is null
        try {
            MockUtil mockUtil = new MockUtil();
            mockUtil.createMock(null, new MockSettingsImpl());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("classToMock cannot be null", e.getMessage());
        }

        // Test case 2: extraInterfaces is null
        MockUtil mockUtil = new MockUtil();
        SampleClass1 mock = mockUtil.createMock(SampleClass1.class, new MockSettingsImpl());
        assertNotNull(mock);
        assertEquals(SampleClass1.class, mock.getClass());

        // Test case 3: extraInterfaces is not null
        mockUtil = new MockUtil();
        SampleClass1 mockWithInterfaces = mockUtil.createMock(SampleClass1.class, new MockSettingsImpl().extraInterfaces(SampleInterface.class));
        assertNotNull(mockWithInterfaces);
        assertEquals(SampleClass1.class, mockWithInterfaces.getClass());
        assertTrue(SampleInterface.class.isAssignableFrom(mockWithInterfaces.getClass()));
    }

    @Test
    public void testResetMock() {
        // Test case 1: mock is null
        try {
            MockUtil mockUtil = new MockUtil();
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case 2: mock is a Mockito mock
        MockUtil mockUtil = new MockUtil();
        SampleClass1 mock = mock(SampleClass1.class);
        mockUtil.resetMock(mock);
        verifyNoMoreInteractions(mock);

        // Test case 3: mock is not a Mockito mock
        mockUtil = new MockUtil();
        SampleClass1 nonMockitoMock = new SampleClass1();
        try {
            mockUtil.resetMock(nonMockitoMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + nonMockitoMock.getClass(), e.getMessage());
        }
    }

    @Test
    public void testGetMockHandler() {
        // Test case 1: mock is null
        try {
            MockUtil mockUtil = new MockUtil();
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case 2: mock is a Mockito mock
        MockUtil mockUtil = new MockUtil();
        SampleClass1 mock = mock(SampleClass1.class);
        MockHandlerInterface<SampleClass1> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        assertEquals(MockHandler.class, handler.getClass());

        // Test case 3: mock is not a Mockito mock
        mockUtil = new MockUtil();
        SampleClass1 nonMockitoMock = new SampleClass1();
        try {
            mockUtil.getMockHandler(nonMockitoMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + nonMockitoMock.getClass(), e.getMessage());
        }
    }

    @Test
    public void testIsMock() {
        // Test case 1: mock is null
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));

        // Test case 2: mock is a Mockito mock
        mockUtil = new MockUtil();
        SampleClass1 mock = mock(SampleClass1.class);
        assertTrue(mockUtil.isMock(mock));

        // Test case 3: mock is not a Mockito mock
        mockUtil = new MockUtil();
        SampleClass1 nonMockitoMock = new SampleClass1();
        assertFalse(mockUtil.isMock(nonMockitoMock));
    }

    @Test
    public void testGetMockName() {
        // Test case 1: mock is null
        try {
            MockUtil mockUtil = new MockUtil();
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case 2: mock is a Mockito mock
        MockUtil mockUtil = new MockUtil();
        SampleClass1 mock = mock(SampleClass1.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals(MockSettingsImpl.class, mockName.getClass());

        // Test case 3: mock is not a Mockito mock
        mockUtil = new MockUtil();
        SampleClass1 nonMockitoMock = new SampleClass1();
        try {
            mockUtil.getMockName(nonMockitoMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + nonMockitoMock.getClass(), e.getMessage());
        }
    }

    // Sample class and interface for testing
    public class SampleClass1 {}
    public interface SampleInterface {}
}