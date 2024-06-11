
package org.mockito.internal.util;

import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.MethodInterceptor;

import org.junit.Test;
import static org.junit.Assert.*;


public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test createMock with valid settings
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        TestObject mock = mockUtil.createMock(classToMock, mockSettings);
        assertNotNull(mock);
        assertTrue(mock instanceof TestObject);
        
        // Test createMock with invalid classToMock
        classToMock = null;
        try {
            mock = mockUtil.createMock(classToMock, mockSettings);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // Test createMock with invalid extraInterfaces
        classToMock = TestObject.class;
        mockSettings = new MockSettingsImpl();
        mockSettings.setExtraInterfaces(new Class[] {InvalidInterface.class});
        try {
            mock = mockUtil.createMock(classToMock, mockSettings);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class org.mockito.internal.util.InvalidInterface", e.getMessage());
        }
        
        // Test createMock with valid spiedInstance
        classToMock = TestObject.class;
        mockSettings = new MockSettingsImpl();
        mockSettings.setSpiedInstance(new TestObject());
        mock = mockUtil.createMock(classToMock, mockSettings);
        assertNotNull(mock);
        assertTrue(mock instanceof TestObject);
        
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test resetMock with valid mock
        TestObject mock = new TestObject();
        mockUtil.resetMock(mock);
        
        // Test resetMock with null mock
        mock = null;
        try {
            mockUtil.resetMock(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        // Test getMockHandler with valid mock
        TestObject mock = new TestObject();
        MockHandlerInterface<TestObject> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        
        // Test getMockHandler with null mock
        mock = null;
        try {
            mockHandler = mockUtil.getMockHandler(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // Test getMockHandler with invalid mock
        String invalidMock = "not a mock";
        try {
            mockHandler = mockUtil.getMockHandler(invalidMock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.String", e.getMessage());
        }
        
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test isMock with valid mock
        TestObject mock = new TestObject();
        assertTrue(mockUtil.isMock(mock));
        
        // Test isMock with null mock
        mock = null;
        assertFalse(mockUtil.isMock(mock));
        
        // Test isMock with invalid mock
        String invalidMock = "not a mock";
        assertFalse(mockUtil.isMock(invalidMock));
        
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        // Test getMockName with valid mock
        TestObject mock = new TestObject();
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        
        // Test getMockName with null mock
        mock = null;
        try {
            mockName = mockUtil.getMockName(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
        
        // Test getMockName with invalid mock
        String invalidMock = "not a mock";
        try {
            mockName = mockUtil.getMockName(invalidMock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.String", e.getMessage());
        }
        
    }
    
    private static class TestObject {
        
    }
    
    private static interface InvalidInterface {
        
    }
    
    
}

