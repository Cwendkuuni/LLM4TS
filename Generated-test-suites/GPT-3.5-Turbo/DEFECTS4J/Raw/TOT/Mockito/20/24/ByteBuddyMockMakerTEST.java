import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.*;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;

public class ByteBuddyMockMakerTest {

  private ByteBuddyMockMaker mockMaker;
  private MockCreationSettings mockCreationSettings;
  private MockHandler mockHandler;
  
  @Before
  public void setup() {
    mockMaker = new ByteBuddyMockMaker();
    mockCreationSettings = mock(MockCreationSettings.class);
    mockHandler = new InternalMockHandler();
  }

  @Test
  public void testCreateMock_validSettingsAndHandler_returnsMock() {
    // Arrange
    when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.REGULAR);
    when(mockCreationSettings.getTypeToMock()).thenReturn(String.class);
    when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[] {});
    
    // Act
    Object mock = mockMaker.createMock(mockCreationSettings, mockHandler);
    
    // Assert
    assertEquals(String.class, mock.getClass().getSuperclass());
    verifyZeroInteractions(mockHandler);
  }
  
  @Test
  public void testCreateMock_serializableModeAcrossClassloaders_throwsMockitoException() {
    // Arrange
    when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
    
    // Act & Assert
    assertThrows(MockitoException.class, () -> mockMaker.createMock(mockCreationSettings, mockHandler));
  }
  
  @Test
  public void testCreateMock_classCastException_throwsMockitoException() {
    // Arrange
    when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.REGULAR);
    when(mockCreationSettings.getTypeToMock()).thenReturn(Double.class);
    when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[] {});
    
    // Act & Assert
    assertThrows(MockitoException.class, () -> mockMaker.createMock(mockCreationSettings, mockHandler));
  }
  
  @Test
  public void testCreateMock_instantiationException_throwsMockitoException() {
    // Arrange
    when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.REGULAR);
    when(mockCreationSettings.getTypeToMock()).thenReturn(Throwable.class);
    when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[] {});
    
    // Act & Assert
    assertThrows(MockitoException.class, () -> mockMaker.createMock(mockCreationSettings, mockHandler));
  }
  
  @Test
  public void testEnsureMockIsAssignableToMockedType_validSettingsAndMock_returnsMock() {
    // Arrange
    String mock = "mock";
    
    // Act
    Object result = mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
    
    // Assert
    assertEquals(String.class, result.getClass());
  }
  
  @Test
  public void testEnsureMockIsAssignableToMockedType_invalidSettingsAndMock_throwsClassCastException() {
    // Arrange
    Integer mock = 1;
    
    // Act & Assert
    assertThrows(ClassCastException.class, () -> mockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock));
  }
  
  @Test
  public void testDescribeClass_nullType_returnsNullDescription() {
    // Act
    String description = mockMaker.describeClass(null);
    
    // Assert
    assertEquals("null", description);
  }
  
  @Test
  public void testDescribeClass_validType_returnsTypeDescription() {
    // Act
    String description = mockMaker.describeClass(String.class);
    
    // Assert
    assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
  }
  
  @Test
  public void testDescribeClass_nullInstance_returnsNullDescription() {
    // Act
    String description = mockMaker.describeClass((Object) null);
    
    // Assert
    assertEquals("null", description);
  }
  
  @Test
  public void testDescribeClass_validInstance_returnsInstanceDescription() {
    // Arrange
    String instance = "instance";
    
    // Act
    String description = mockMaker.describeClass(instance);
    
    // Assert
    assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
  }
  
  @Test
  public void testGetHandler_validMock_returnsMockHandler() {
    // Arrange
    Object mock = mock(String.class, withSettings().spiedInstance("mock"));
    
    // Act
    MockHandler handler = mockMaker.getHandler(mock);
    
    // Assert
    assertEquals(InternalMockHandler.class, handler.getClass());
  }
  
  @Test
  public void testGetHandler_invalidMock_returnsNull() {
    // Arrange
    Object mock = new Object();
    
    // Act
    MockHandler handler = mockMaker.getHandler(mock);
    
    // Assert
    assertEquals(null, handler);
  }
  
  @Test
  public void testResetMock_validMockAndHandler_resetHandler() {
    // Arrange
    Object mock = mock(String.class);
    MockHandler newHandler = new InternalMockHandler();
    
    // Act
    mockMaker.resetMock(mock, newHandler, mockCreationSettings);
    
    // Assert
    MockHandler handler = mockMaker.getHandler(mock);
    assertEquals(newHandler, handler);
  }
  
  @Test
  public void testResetMock_invalidMockAndHandler_throwsMockitoException() {
    // Arrange
    Object mock = new Object();
    MockHandler newHandler = new InternalMockHandler();
    
    // Act & Assert
    assertThrows(MockitoException.class, () -> mockMaker.resetMock(mock, newHandler, mockCreationSettings));
  }
  
  @Test
  public void testInitializeClassInstantiator_objenesisMissing_throwsIllegalStateException() {
    // Arrange
    GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class, withSettings().extraInterfaces(ClassInstantiator.class));
    mockMaker = new ByteBuddyMockMaker(globalConfiguration);
    
    // Act & Assert
    assertThrows(IllegalStateException.class, () -> mockMaker.initializeClassInstantiator());
  }

}