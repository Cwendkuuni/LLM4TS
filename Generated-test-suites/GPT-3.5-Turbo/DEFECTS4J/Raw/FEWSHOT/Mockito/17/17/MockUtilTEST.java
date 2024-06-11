package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mock.MockName;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Test class without extra interfaces
        TestClass1 mock1 = mockUtil.createMock(TestClass1.class, settings);
        assertNotNull(mock1);
        
        // Test class with extra interfaces
        TestClass2 mock2 = mockUtil.createMock(TestClass2.class, settings.extraInterfaces(TestInterface.class));
        assertNotNull(mock2);
        assertTrue(mock2 instanceof TestInterface);
        
        // Test class with spied instance
        TestClass3 spiedInstance = new TestClass3();
        TestClass3 mock3 = mockUtil.createMock(TestClass3.class, settings.spiedInstance(spiedInstance));
        assertNotNull(mock3);
        assertTrue(mock3 instanceof TestClass3);
        
        // Test invalid mocked type
        boolean exceptionThrown = false;
        try {
            TestClass4 mock4 = mockUtil.createMock(TestClass4.class, settings);
        } catch (NotAMockException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test resetting mock
        TestClass1 mock1 = mockUtil.createMock(TestClass1.class, new MockSettingsImpl());
        assertNotNull(mock1);
        mockUtil.resetMock(mock1);
        
        // Test invalid mock
        boolean exceptionThrown = false;
        try {
            TestClass1 obj = new TestClass1();
            mockUtil.resetMock(obj);
        } catch (NotAMockException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        // Test getting mock handler
        TestClass1 mock1 = mockUtil.createMock(TestClass1.class, new MockSettingsImpl());
        assertNotNull(mock1);
        MockHandlerInterface<TestClass1> handler = mockUtil.getMockHandler(mock1);
        assertNotNull(handler);
        
        // Test getting mock handler for invalid mock
        boolean exceptionThrown = false;
        try {
            TestClass1 obj = new TestClass1();
            mockUtil.getMockHandler(obj);
        } catch (NotAMockException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test checking mock
        TestClass1 mock1 = mockUtil.createMock(TestClass1.class, new MockSettingsImpl());
        assertNotNull(mock1);
        assertTrue(mockUtil.isMock(mock1));
        
        // Test checking non-mock
        TestClass1 obj = new TestClass1();
        assertFalse(mockUtil.isMock(obj));
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        // Test getting mock name
        TestClass1 mock1 = mockUtil.createMock(TestClass1.class, new MockSettingsImpl().name("mock1"));
        assertNotNull(mock1);
        MockName name = mockUtil.getMockName(mock1);
        assertNotNull(name);
        assertEquals("mock1", name.toString());
        
        // Test getting mock name for invalid mock
        boolean exceptionThrown = false;
        try {
            TestClass1 obj = new TestClass1();
            mockUtil.getMockName(obj);
        } catch (NotAMockException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}

class TestClass1 {
}

class TestClass2 {
}

interface TestInterface {
}

class TestClass3 {
}

class TestClass4 {
}