package org.mockito.internal.creation.bytebuddy;

import org.mockito.internal.util.StringJoiner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler));
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock));
    }
    
    @Test
    public void testDescribeClass() {
        Class type = null;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        String result = byteBuddyMockMaker.describeClass(type);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClassWithClass() {
        Class type = ByteBuddyMockMaker.class;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        String result = byteBuddyMockMaker.describeClass(type);
        
        assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker', loaded by classloader : '" + ByteBuddyMockMaker.class.getClassLoader() + "'", result);
    }
    
    @Test
    public void testDescribeClassWithInstance() {
        Object instance = null;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        String result = byteBuddyMockMaker.describeClass(instance);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClassWithInstanceObject() {
        Object instance = new ByteBuddyMockMaker();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        String result = byteBuddyMockMaker.describeClass(instance);
        
        assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker', loaded by classloader : '" + instance.getClass().getClassLoader() + "'", result);
    }
    
    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertNull(result);
    }
    
    @Test
    public void testGetHandlerWithMockMethodInterceptorMockAccess() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertNotNull(result);
    }
    
    @Test
    public void testResetMock() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        assertDoesNotThrow(() -> byteBuddyMockMaker.resetMock(mock, newHandler, settings));
    }
    
    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        assertDoesNotThrow(() -> byteBuddyMockMaker.initializeClassInstantiator());
    }
    
    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.asInternalMockHandler(handler));
    }
}