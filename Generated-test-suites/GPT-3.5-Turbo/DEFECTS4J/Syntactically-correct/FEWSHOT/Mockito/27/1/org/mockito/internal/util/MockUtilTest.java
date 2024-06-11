package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_validClass() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        assertTrue(mock instanceof ClassToMock);
        assertFalse(mock instanceof Serializable);
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidClass() {
        InvalidClassToMock mock = mockUtil.createMock(InvalidClassToMock.class, new MockSettingsImpl());
    }
    
    @Test
    public void testCreateMock_extraInterfaces() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(InterfaceToMock.class));
        
        assertTrue(mock instanceof ClassToMock);
        assertTrue(mock instanceof InterfaceToMock);
        assertFalse(mock instanceof Serializable);
    }
    
    @Test
    public void testCreateMock_withSpiedInstance() {
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        
        assertTrue(mock instanceof ClassToMock);
        assertFalse(mock instanceof Serializable);
        
        assertFalse(spiedInstance.equals(mock));
    }
    
    @Test
    public void testResetMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        mockUtil.resetMock(mock);
        
        // Verify that the mock has been reset
        assertFalse(mock.equals(new ClassToMock()));
    }
    
    @Test
    public void testGetMethodInterceptorFilter() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
        
        assertTrue(filter instanceof MethodInterceptorFilter);
    }
    
    @Test
    public void testGetMethodInterceptorFilter_notAMock() {
        Object invalidMock = new Object();
        
        MethodInterceptorFilter filter = mockUtil.getInterceptor(invalidMock);
        
        assertNull(filter);
    }
    
    @Test
    public void testGetMockHandler() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);
        
        assertTrue(handler instanceof MockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_invalidMock() {
        Object invalidMock = new Object();
        
        mockUtil.getMockHandler(invalidMock);
    }
    
    @Test
    public void testIsMock_true() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_false() {
        Object invalidMock = new Object();
        
        boolean isMock = mockUtil.isMock(invalidMock);
        
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().name("mockName"));
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertEquals("mockName", mockName.toString());
    }
}

class ClassToMock {
    
}

interface InterfaceToMock {
    
}

class InvalidClassToMock {
    
}