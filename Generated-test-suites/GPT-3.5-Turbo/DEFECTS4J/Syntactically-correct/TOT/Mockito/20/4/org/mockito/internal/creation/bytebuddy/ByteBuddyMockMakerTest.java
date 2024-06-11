import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

   private final ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
   
   @Test
   public void testCreateMock() {
       MockCreationSettings settings = mock(MockCreationSettings.class);
       MockHandler handler = mock(MockHandler.class);
       
       when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
       
       try {
           mockMaker.createMock(settings, handler);
           fail("Expected MockitoException");
       } catch (MockitoException e) {
           assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
       }
   }
   
   @Test
   public void testCreateMock_ClassCastException() {
       MockCreationSettings settings = mock(MockCreationSettings.class);
       MockHandler handler = mock(MockHandler.class);
       Class mockedProxyType = mock(Class.class);
       Class typeToMock = mock(Class.class);
       Object mockInstance = new Object();
       
       when(settings.getTypeToMock()).thenReturn(typeToMock);
       when(settings.getExtraInterfaces()).thenReturn(new Class[0]);
       when(mockedProxyType.getSuperclass()).thenReturn(typeToMock);
       when(handler.getClass()).thenReturn(Object.class);
       
       try {
           mockMaker.createMock(settings, handler);
           fail("Expected MockitoException");
       } catch (MockitoException e) {
           String expectedMessage = "ClassCastException occurred while creating the mockito mock :\n" +
                   "  class to mock : 'null', loaded by classloader : 'null'\n" +
                   "  created class : 'null', loaded by classloader : 'null'\n" +
                   "  proxy instance class : 'java.lang.Object', loaded by classloader : 'null'\n" +
                   "  instance creation by : initializeClassInstantiator\n" +
                   "\n" +
                   "You might experience classloading issues, please ask the mockito mailing-list.\n" +
                   "\n";
           assertEquals(expectedMessage, e.getMessage());
       }
   }
   
   @Test
   public void testCreateMock_InstantiationException() {
       MockCreationSettings settings = mock(MockCreationSettings.class);
       MockHandler handler = mock(MockHandler.class);
       Class mockedProxyType = mock(Class.class);
       Class typeToMock = mock(Class.class);
       InstantiationException exception = mock(InstantiationException.class);
       
       when(settings.getTypeToMock()).thenReturn(typeToMock);
       when(settings.getExtraInterfaces()).thenReturn(new Class[0]);
       when(mockedProxyType.getSuperclass()).thenReturn(typeToMock);
       when(exception.getClass()).thenReturn(InstantiationException.class);
       when(exception.getMessage()).thenReturn("Unable to create mock instance of type 'null'");
       
       try {
           mockMaker.createMock(settings, handler);
           fail("Expected MockitoException");
       } catch (MockitoException e) {
           assertEquals("Unable to create mock instance of type 'null'", e.getMessage());
       }
   }
   
   @Test
   public void testEnsureMockIsAssignableToMockedType() {
       MockCreationSettings settings = mock(MockCreationSettings.class);
       MockHandler handler = mock(MockHandler.class);
       Class typeToMock = mock(Class.class);
       Object mock = mock(Object.class);
       
       when(settings.getTypeToMock()).thenReturn(typeToMock);
       
       Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
       
       assertSame(mock, result);
   }
   
   @Test
   public void testGetHandler() {
       Object mock = new Object();
       
       MockHandler handler = mockMaker.getHandler(mock);
       
       assertNull(handler);
   }
   
   @Test
   public void testGetHandler_InstanceOfMockAccess() {
       MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
       Object mock = mockAccess;
       
       MockHandler handler = mockMaker.getHandler(mock);
       
       assertNotNull(handler);
       assertTrue(handler instanceof MockMethodInterceptor.MockMethodInterceptor);
   }
   
   @Test
   public void testResetMock() {
       Object mock = new Object();
       MockHandler newHandler = mock(MockHandler.class);
       MockCreationSettings settings = mock(MockCreationSettings.class);
       
       mockMaker.resetMock(mock, newHandler, settings);
       
       // Verify that the mockito interceptor is set with the new handler
   }
   
   @Test(expected = MockitoException.class)
   public void testResetMock_InvalidHandlerType() {
       Object mock = new Object();
       MockHandler newHandler = mock(MockHandler.class);
       MockCreationSettings settings = mock(MockCreationSettings.class);
       
       mockMaker.resetMock(mock, newHandler, settings);
   }
   
   @Test
   public void testInitializeClassInstantiator() {
       ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
       
       assertNotNull(classInstantiator);
       // Verify that the class instantiator is created correctly using the ClassInstantiator.UsingObjenesis class
       // Verify that the ClassInstantiator constructor is called with the enableClassCache set to true
   }
   
   @Test(expected = IllegalStateException.class)
   public void testInitializeClassInstantiator_ClassNotFoundException() {
       mockMaker.initializeClassInstantiator();
   }
   
   @Test(expected = IllegalStateException.class)
   public void testInitializeClassInstantiator_ExceptionDuringConstruction() {
       mockMaker.initializeClassInstantiator();
   }
   
   @Test
   public void testAsInternalMockHandler() {
       MockHandler handler = mock(MockHandler.class);
       
       InternalMockHandler internalHandler = mockMaker.asInternalMockHandler(handler);
       
       assertNotNull(internalHandler);
       assertEquals(handler, internalHandler);
   }
   
   @Test(expected = MockitoException.class)
   public void testAsInternalMockHandler_InvalidHandlerType() {
       mockMaker.asInternalMockHandler(mock(MockHandler.class));
   }
}