package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Validate type
        try {
            mockUtil.createMock(null, settings);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Type to mock cannot be null", e.getMessage());
        }
        
        // Validate extra interfaces
        try {
            mockUtil.createMock(String.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("MockSettings cannot be null", e.getMessage());
        }
        
        // Validate mocked type
        try {
            mockUtil.createMock(String.class, new MockSettingsImpl().spiedInstance(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument type cannot be spiedInstance", e.getMessage());
        }
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        // null mock
        try {
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // invalid mock
        try {
            mockUtil.resetMock("not a mock");
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        // null mock
        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // invalid mock
        try {
            mockUtil.getMockHandler("not a mock");
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.String", e.getMessage());
        }
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        assertFalse(mockUtil.isMock(null));
        assertFalse(mockUtil.isMock("not a mock"));
        assertFalse(mockUtil.isMock(new Object()));
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        // null mock
        try {
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // invalid mock
        try {
            mockUtil.getMockName("not a mock");
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.String", e.getMessage());
        }
    }
}