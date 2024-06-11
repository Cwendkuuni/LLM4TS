package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        
        Class<String> classToMock = String.class;
        String mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertTrue(mock instanceof String);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        Assert.assertTrue(mockUtil.isMock(mock));
        
        Object nonMock = new Object();
        Assert.assertFalse(mockUtil.isMock(nonMock));
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
    }
}