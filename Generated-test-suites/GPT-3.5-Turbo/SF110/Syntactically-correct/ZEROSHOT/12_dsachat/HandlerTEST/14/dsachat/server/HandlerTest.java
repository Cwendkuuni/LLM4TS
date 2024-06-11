package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {
   
   private Handler handler;
   
   @Before
   public void setUp() {
      handler = new Handler();
   }
   
   @Test
   public void testAddConnection() {
      ConnectionEndpoint cl = new ConnectionEndpoint();
      handler.addConnection(cl);
      assertEquals(1, handler.clients.size());
   }
   
   @Test
   public void testRemoveConnection() {
      ConnectionEndpoint cl = new ConnectionEndpoint();
      handler.addConnection(cl);
      handler.removeConnection(cl);
      assertEquals(0, handler.clients.size());
   }
   
   @Test
   public void testHandleInput_String() {
      InputEvent e = new InputEvent(new Handler(), "Test String");
      handler.handleInput(e);
      // Add assertions here
   }
   
   @Test
   public void testHandleInput_Challenge() {
      InputEvent e = new InputEvent(new Handler(), new Challenge());
      handler.handleInput(e);
      // Add assertions here
   }
   
   @Test
   public void testHandleInput_ChallReq() {
      InputEvent e = new InputEvent(new Handler(), new ChallReq());
      handler.handleInput(e);
      // Add assertions here
   }
   
   @Test
   public void testHandleInput_Hero() {
      InputEvent e = new InputEvent(new Handler(), new Hero());
      handler.handleInput(e);
      // Add assertions here
   }
   
   @Test
   public void testHandleInput_UserEvent() {
      InputEvent e = new InputEvent(new Handler(), new UserEvent());
      handler.handleInput(e);
      // Add assertions here
   }
   
   @Test
   public void testSendHeros() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      handler.sendHeros(con);
      // Add assertions here
   }
   
   @Test
   public void testIsGmConnected() {
      boolean result = handler.isGmConnected();
      assertFalse(result);
   }
   
   @Test
   public void testSendOneSystemMsg() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String string = "Test Message";
      handler.sendOneSystemMsg(con, string);
      // Add assertions here
   }
   
   @Test
   public void testSendAllSystemMsg() {
      String string = "Test Message";
      handler.sendAllSystemMsg(string);
      // Add assertions here
   }
   
   @Test
   public void testWhisper() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String cmd = "Test Command";
      handler.whisper(con, cmd);
      // Add assertions here
   }
   
   @Test
   public void testRollDice_withModifier() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String cmd = "2w6+3";
      handler.rollDice(con, cmd);
      // Add assertions here
   }
   
   @Test
   public void testRollDice_withoutModifier() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String cmd = "2w6";
      handler.rollDice(con, cmd);
      // Add assertions here
   }
   
   @Test
   public void testSendToOneStr() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String to = "Test User";
      String send = "Test Message";
      handler.sendToOneStr(con, to, send);
      // Add assertions here
   }
   
   @Test
   public void testUpdateHero() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      Hero h = new Hero();
      String result = handler.updateHero(con, h);
      assertNull(result);
   }
   
   @Test
   public void testSendAllStr() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String string = "Test Message";
      handler.sendAllStr(con, string);
      // Add assertions here
   }
   
   @Test
   public void testSendAllObj() {
      Object o = new Object();
      handler.sendAllObj(o);
      // Add assertions here
   }
   
   @Test
   public void testGetName() {
      ConnectionEndpoint con = new ConnectionEndpoint();
      String result = handler.getName(con);
      assertNull(result);
   }
}