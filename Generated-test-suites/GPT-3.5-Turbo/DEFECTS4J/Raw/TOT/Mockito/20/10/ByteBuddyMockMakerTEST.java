import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_Success() {
        // Create mock settings
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(String.class);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String mock = mockMaker.createMock(settings, handler);
        
        // Verify that mock instance is not null
        assertNotNull(mock);
        
        // Verify the mockitoInterceptor of the mock instance
        InternalMockHandler internalHandler = mockMaker.getHandler(mock);
        assertNotNull(internalHandler);
        assertEquals(handler, internalHandler.getMockHandler());
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloadersNotSupported() {
        // Create mock settings
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
        
        // Exception should be thrown due to serialization not supported
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        // Create mock settings
        MockCreationSettings<ClassCastException> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(ClassCastException.class);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
        
        // Exception should be thrown due to ClassCastException
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        // Create mock settings
        MockCreationSettings<InstantiationException> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(InstantiationException.class);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
        
        // Exception should be thrown due to InstantiationException
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType_Success() {
        // Create mock settings
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String mock = mockMaker.createMock(settings, null);
        
        // Ensure the mock is assignable to the mocked type
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Verify the result is the same as the mock instance
        assertSame(mock, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_ClassCastException() {
        // Create mock settings
        MockCreationSettings<NumberFormatException> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(NumberFormatException.class);
        
        // Create mock instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        NumberFormatException mock = mockMaker.createMock(settings, null);
        
        // Ensure the mock is assignable to the mocked type
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // ClassCastException should occur due to incorrect cast
    }
    
    @Test
    public void testDescribeClass_NullType() {
        String result = ByteBuddyMockMaker.describeClass(null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_ClassType() {
        String result = ByteBuddyMockMaker.describeClass(String.class);
        
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }
    
    @Test
    public void testDescribeClass_Instance() {
        String result = ByteBuddyMockMaker.describeClass("mock");
        
        assertEquals("'java.lang.String', loaded by classloader : 'java.net.URLClassLoader@1234567'", result);
    }
    
    @Test
    public void testGetHandler_Success() {
        InternalMockHandler handler = mock(InternalMockHandler.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(handler);
        
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler result = mockMaker.getHandler(mockAccess);
        
        assertSame(handler, result);
    }
    
    @Test
    public void testGetHandler_NotMockAccess() {
        Object mock = new Object();
        
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler result = mockMaker.getHandler(mock);
        
        assertNull(result);
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMock_Success() {
        // Create old handler
        MockHandler oldHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(oldHandler);
        
        // Create new handler
        MockHandler newHandler = mock(MockHandler.class);
        
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.resetMock(mockAccess, newHandler, null);
        
        // Verify the mockitoInterceptor of the mockAccess is set with the new handler
        InternalMockHandler internalHandler = mockAccess.getMockitoInterceptor().getMockHandler();
        assertSame(newHandler, internalHandler);
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMock_NotMockAccess() {
        Object mock = new Object();
        MockHandler newHandler = mock(MockHandler.class);
        
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.resetMock(mock, newHandler, null);
        
        // MockitoException should be thrown due to not being a MockAccess object
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Exception() {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);
        
        ByteBuddyMockMaker.initializeClassInstantiator();
        
        // IllegalStateException should be thrown due to missing Objenesis
    }

}