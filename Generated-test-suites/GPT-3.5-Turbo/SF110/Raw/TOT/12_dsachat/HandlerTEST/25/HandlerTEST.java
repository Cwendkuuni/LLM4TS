import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class HandlerTest {
  
  @Mock
  private ConnectionEndpoint mockConnectionEndpoint;
  
  private Handler handler;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    handler = new Handler();
  }
  
  // Test addConnection method
  
  @Test
  public void testAddConnection() {
    handler.addConnection(mockConnectionEndpoint);
    // Check that the clients list size is increased by 1
    assertEquals(1, handler.clients.size());
    // Check if the name of the client is assigned correctly
    assertEquals("", handler.clients.elementAt(0).name);
    // Check if the UserEvent is sent to the client
    Mockito.verify(mockConnectionEndpoint).send(Mockito.any(UserEvent.class));
  }
  
  // Test removeConnection method
  
  @Test
  public void testRemoveConnection() {
    String expectedName = "TestUser";
    MockEntry mockEntry = new MockEntry();
    mockEntry.ce = mockConnectionEndpoint;
    mockEntry.name = expectedName;
    handler.clients.addElement(mockEntry);
    
    handler.removeConnection(mockConnectionEndpoint);
    // Check that the clients list size is decreased by 1
    assertEquals(0, handler.clients.size());
    // Check if the UserEvent is sent to the remaining clients
    Mockito.verify(mockConnectionEndpoint).send(Mockito.any(UserEvent.class));
    // Check if the correct UserEvent is sent
    Mockito.verify(mockConnectionEndpoint).send(Mockito.argThat(userEvent -> userEvent.getName().equals(expectedName)));
  }
  
  // Test handleInput method
  
  @Test
  public void testHandleInputWithStringInput() {
    String input = "Test Input";
    InputEvent mockInputEvent = Mockito.mock(InputEvent.class);
    Mockito.when(mockInputEvent.getData()).thenReturn(input);
    Mockito.when(mockInputEvent.getSource()).thenReturn(mockConnectionEndpoint);
    
    handler.handleInput(mockInputEvent);
    
    // Check if the input is sent to all clients
    Mockito.verify(mockConnectionEndpoint).send(Mockito.eq(input));
  }
  
  @Test
  public void testHandleInputWithChallengeInput() {
    Challenge challenge = new Challenge();
    challenge.setTo(null);
    challenge.setSilent(false);
    InputEvent mockInputEvent = Mockito.mock(InputEvent.class);
    Mockito.when(mockInputEvent.getData()).thenReturn(challenge);
    Mockito.when(mockInputEvent.getSource()).thenReturn(mockConnectionEndpoint);
    
    handler.handleInput(mockInputEvent);
    
    // Check if the challenge result is sent to all clients
    Mockito.verify(mockConnectionEndpoint).send(Mockito.anyString());
  }
  
  // Other test methods for remaining methods in the Handler class
  
  // ...
  
}

class MockEntry {
  public ConnectionEndpoint ce;
  public String name;
}