package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import java.io.Serializable;


public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(null);
        settings.setSpiedInstance(null);
        MockUtil util = new MockUtil();
        
        String mock = util.createMock(String.class, settings);
        
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        MockUtil util = new MockUtil();
        String mock = "mock";
        
        util.resetMock(mock);
        
        assertTrue(util.isMock(mock));
    }
    
    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<Object>(settings);
        InvocationNotifierHandler<Object> notifierHandler = new InvocationNotifierHandler<Object>(mockHandler, settings);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(notifierHandler, settings);
        
        assertNotNull(filter);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil util = new MockUtil();
        
        util.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        util.getMockHandler(mock);
    }
    
    @Test
    public void testIsMockWithNullMock() {
        MockUtil util = new MockUtil();
        Object mock = null;
        
        boolean isMock = util.isMock(mock);
        
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMockWithNonMockitoMock() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        boolean isMock = util.isMock(mock);
        
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMockWithMockitoMock() {
        MockUtil util = new MockUtil();
        Object mock = util.createMock(String.class, new MockSettingsImpl());
        
        boolean isMock = util.isMock(mock);
        
        assertTrue(isMock);
    }
    
    @Test
    public void testGetInterceptorWithNonFactoryMock() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        MethodInterceptorFilter interceptor = util.getInterceptor(mock);
        
        assertNull(interceptor);
    }
    
    @Test
    public void testGetInterceptorWithFactoryMock() {
        MockUtil util = new MockUtil();
        Object proxy = util.createMock(String.class, new MockSettingsImpl());
        
        MethodInterceptorFilter interceptor = util.getInterceptor(proxy);
        
        assertNotNull(interceptor);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil util = new MockUtil();
        Object mock = util.createMock(String.class, new MockSettingsImpl());
        
        MockName name = util.getMockName(mock);
        
        assertNotNull(name);
    }
}