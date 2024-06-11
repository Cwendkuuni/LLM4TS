
package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        MockHandlerInterface<T> mockHandler = mock(MockHandlerInterface.class);
        mockUtil.getMockHandler(mock);
        verify(mock, times(1)).getHandler();
        verify(mock, times(1)).getMockSettings().getMockName();
        verify(mock, times(1)).setCallback(0, newFilter);
        lenientCopyTool.copyToMock(spiedInstance, mock);
        
        assertEquals(mock, mockUtil.createMock(classToMock, settings));
        assertNotNull(mockUtil.createMock(classToMock, settings));
    }

    @Test
    public void testResetMock() {
        notAMockException();
        verify();
        assertNull(mockUtil.resetMock(mock));
        assertNotNull(mockUtil.resetMock(mock));
    }
    
    @Test
    public void testGetMockHandler() {
        notAMockException();
        
        assertEquals(mockHandler, mockUtil.getMockHandler(mock));
        assertNotNull(mockUtil.getMockHandler(mock));
    }
    
    @Test
    public void testIsMock() {
        notAMockException();
        
        assertFalse(mockUtil.isMock(null));
        assertFalse(mockUtil.isMock(mock));
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testGetInterceptor() {
        Callback callback = factory.getCallback(0);
        
        assertTrue(mockUtil.getInterceptor(mock) instanceof MethodInterceptorFilter);
        assertNotNull(mockUtil.getInterceptor(mock));
    }
    
    @Test
    public void testGetMockName() {
        assertEquals(mockName, mockUtil.getMockName(mock));
        assertNotNull(mockUtil.getMockName(mock));
    }

    private void notAMockException() {
        throw new NotAMockException(msg);
        assertNotNull(mockUtil.getMockHandler(mock));
    }
}
