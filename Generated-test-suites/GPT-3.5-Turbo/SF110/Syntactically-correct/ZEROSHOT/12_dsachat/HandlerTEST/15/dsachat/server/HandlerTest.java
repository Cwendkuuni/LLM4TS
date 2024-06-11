import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {
  
  private Handler handler;
  private ConnectionEndpoint mockConnection;
  
  @Before
  public void setUp() {
    handler = new Handler();
    mockConnection = Mockito.mock(ConnectionEndpoint.class);
  }
  
  @Test
  public void testAddConnection() {
    ConnectionEndpoint cl = Mockito.mock(ConnectionEndpoint.class);
    handler.addConnection(cl);
    // TODO: Add assertion
  }
  
  @Test
  public void testRemoveConnection() {
    ConnectionEndpoint cl = Mockito.mock(ConnectionEndpoint.class);
    handler.addConnection(cl);
    handler.removeConnection(cl);
    // TODO: Add assertion
  }
  
  @Test
  public void testHandleInput_String() {
    InputEvent inputEvent = Mockito.mock(InputEvent.class);
    String string = "Test String";
    Mockito.when(inputEvent.getData()).thenReturn(string);
    Mockito.when(inputEvent.getSource()).thenReturn(mockConnection);
    
    handler.handleInput(inputEvent);
    // TODO: Add assertion
  }
  
  @Test
  public void testHandleInput_Challenge() {
    InputEvent inputEvent = Mockito.mock(InputEvent.class);
    Challenge challenge = Mockito.mock(Challenge.class);
    Mockito.when(inputEvent.getData()).thenReturn(challenge);
    Mockito.when(inputEvent.getSource()).thenReturn(mockConnection);
    
    handler.handleInput(inputEvent);
    // TODO: Add assertion
  }
  
  @Test
  public void testHandleInput_ChallReq() {
    InputEvent inputEvent = Mockito.mock(InputEvent.class);
    ChallReq challReq = Mockito.mock(ChallReq.class);
    Mockito.when(inputEvent.getData()).thenReturn(challReq);
    Mockito.when(inputEvent.getSource()).thenReturn(mockConnection);
    
    handler.handleInput(inputEvent);
    // TODO: Add assertion
  }
  
  @Test
  public void testHandleInput_Hero() {
    InputEvent inputEvent = Mockito.mock(InputEvent.class);
    Hero hero = Mockito.mock(Hero.class);
    Mockito.when(inputEvent.getData()).thenReturn(hero);
    Mockito.when(inputEvent.getSource()).thenReturn(mockConnection);
    
    handler.handleInput(inputEvent);
    // TODO: Add assertion
  }
  
  @Test
  public void testHandleInput_UserEvent() {
    InputEvent inputEvent = Mockito.mock(InputEvent.class);
    UserEvent userEvent = Mockito.mock(UserEvent.class);
    Mockito.when(inputEvent.getData()).thenReturn(userEvent);
    Mockito.when(inputEvent.getSource()).thenReturn(mockConnection);
    
    handler.handleInput(inputEvent);
    // TODO: Add assertion
  }
  
  @Test
  public void testSendHeros() {
    // TODO: Add test
  }
  
  @Test
  public void testIsGmConnected() {
    // TODO: Add test
  }
  
  @Test
  public void testSendOneSystemMsg() {
    // TODO: Add test
  }
  
  @Test
  public void testSendAllSystemMsg() {
    // TODO: Add test
  }
  
  @Test
  public void testWhisper() {
    // TODO: Add test
  }
  
  @Test
  public void testRollDice_plus() {
    // TODO: Add test
  }
  
  @Test
  public void testRollDice_regular() {
    // TODO: Add test
  }
  
  @Test
  public void testSendToOneStr() {
    // TODO: Add test
  }
  
  @Test
  public void testUpdateHero() {
    // TODO: Add test
  }
  
  @Test
  public void testSendAllStr() {
    // TODO: Add test
  }
  
  @Test
  public void testSendAllObj() {
    // TODO: Add test
  }
  
  @Test
  public void testGetName() {
    // TODO: Add test
  }
}