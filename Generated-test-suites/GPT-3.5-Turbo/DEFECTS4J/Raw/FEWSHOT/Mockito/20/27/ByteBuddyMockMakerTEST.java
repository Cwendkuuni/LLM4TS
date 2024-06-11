package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {
  @Test
  public void testCreateMock() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    MockCreationSettings<String> settings = mock(MockCreationSettings.class);
    MockHandler handler = mock(MockHandler.class);
    
    try {
      mockMaker.createMock(settings, handler);
    } catch (MockitoException e) {
      assertEquals(
        "Serialization across classloaders not yet supported with ByteBuddyMockMaker", 
        e.getMessage()
      );
    }
  }
  
  @Test
  public void testEnsureMockIsAssignableToMockedType() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    MockCreationSettings<String> settings = mock(MockCreationSettings.class);
    String mock = mockMaker.ensureMockIsAssignableToMockedType(settings, "mock");
    
    assertEquals("mock", mock);
  }
  
  @Test
  public void testDescribeClassWithType() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    String result = mockMaker.describeClass(String.class);
    
    assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
  }
  
  @Test
  public void testDescribeClassWithInstance() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    String result = mockMaker.describeClass("mock");
    
    assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
  }
  
  @Test
  public void testGetHandler() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    Object mock = mock(InternalMockHandler.MockAccess.class);
    MockHandler handler = mockMaker.getHandler(mock);
    
    assertNull(handler);
  }
  
  @Test
  public void testResetMock() {
    ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
    Object mock = mock(InternalMockHandler.MockAccess.class);
    MockHandler newHandler = mock(MockHandler.class);
    MockCreationSettings settings = mock(MockCreationSettings.class);
    
    mockMaker.resetMock(mock, newHandler, settings);
    verify(mock).setMockitoInterceptor(mockMaker.asInternalMockHandler(newHandler), settings);
  }
}