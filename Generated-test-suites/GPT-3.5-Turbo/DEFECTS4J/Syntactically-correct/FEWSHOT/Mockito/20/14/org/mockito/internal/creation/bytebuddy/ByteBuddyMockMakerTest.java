package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings mockCreationSettings;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        mockCreationSettings = mock(MockCreationSettings.class);
        mockHandler = mock(MockHandler.class);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        mockMaker.createMock(mockCreationSettings, mockHandler);
    }
    
    @Test
    public void testCreateMock() {
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.DEFAULT);
        
        Object mock = mockMaker.createMock(mockCreationSettings, mockHandler);
        
        assertEquals(mockHandler, mockMaker.getHandler(mock));
        verify(mockCreationSettings).getTypeToMock();
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithoutMockAccessor() {
        mockMaker.resetMock(new Object(), mockHandler, mockCreationSettings);
    }
    
    @Test
    public void testResetMock() {
        ByteBuddyMockMaker.MockAccess mockAccess = mock(ByteBuddyMockMaker.MockAccess.class);
        Object mock = mock(Object.class);
        when(mock).thenReturn(mockAccess);
        
        mockMaker.resetMock(mock, mockHandler, mockCreationSettings);
        
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Object mock = mock(Object.class);
        
        Object ensuredMock = mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
        
        assertEquals(mock, ensuredMock);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedTypeIsNull() {
        Object ensuredMock = mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, null);
        
        assertNull(ensuredMock);
    }

    @Test
    public void testDescribeClassTypeIsNull() {
        String result = mockMaker.describeClass(null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass() {
        String result = mockMaker.describeClass(String.class);
        
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassInstanceIsNull() {
        String result = mockMaker.describeClass((Object) null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClassInstance() {
        String result = mockMaker.describeClass("test");
        
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandlerWithoutMockAccessor() {
        Object mock = new Object();
        
        MockHandler handler = mockMaker.getHandler(mock);
        
        assertNull(handler);
    }
    
    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker.MockAccess mockAccess = mock(ByteBuddyMockMaker.MockAccess.class);
        Object mock = mock(Object.class);
        when(mock).thenReturn(mockAccess);
        
        MockHandler handler = mockMaker.getHandler(mock);
        
        assertEquals(mockHandler, handler);
    }
}