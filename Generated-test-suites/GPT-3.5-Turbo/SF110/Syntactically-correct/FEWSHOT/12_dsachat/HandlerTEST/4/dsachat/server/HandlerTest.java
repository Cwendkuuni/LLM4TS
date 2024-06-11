import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Vector;

public class HandlerTest {
    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private ConnectionEndpoint connectionEndpoint2;
    private UserEvent userEvent;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        connectionEndpoint2 = Mockito.mock(ConnectionEndpoint.class);
        userEvent = new UserEvent(false, "name");
        hero = new Hero("heroName");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testRemoveConnectionWithName() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        UserEvent newCon = new UserEvent(true, "name");
        handler.removeConnection(connectionEndpoint);
        assertEquals(newCon, handler.sendAllObj());
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(connectionEndpoint);
        InputEvent inputEvent = new InputEvent("message", connectionEndpoint);
        handler.handleInput(inputEvent);
        assertEquals("message", handler.clients.get(0).ce.sentMessage);
    }

    @Test
    public void testHandleInputChallenge() {
        handler.addConnection(connectionEndpoint);
        Challenge challenge = Mockito.mock(Challenge.class);
        InputEvent inputEvent = new InputEvent(challenge, connectionEndpoint);
        handler.handleInput(inputEvent);
        Mockito.verify(challenge, Mockito.times(1)).roll();
    }

    @Test
    public void testHandleInputChallReq() {
        handler.addConnection(connectionEndpoint);
        ChallReq challReq = Mockito.mock(ChallReq.class);
        InputEvent inputEvent = new InputEvent(challReq, connectionEndpoint);
        handler.handleInput(inputEvent);
        Mockito.verify(challReq, Mockito.times(1)).getTo();
    }

    @Test
    public void testHandleInputHero() {
        handler.addConnection(connectionEndpoint);
        InputEvent inputEvent = new InputEvent(hero, connectionEndpoint);
        handler.handleInput(inputEvent);
        assertNull(handler.getName(connectionEndpoint));
    }

    @Test
    public void testHandleInputUserEvent() {
        handler.addConnection(connectionEndpoint);
        InputEvent inputEvent = new InputEvent(userEvent, connectionEndpoint);
        handler.handleInput(inputEvent);
        assertEquals("Gamemaster", handler.clients.get(0).name);
    }

    @Test
    public void testSendHeros() throws IOException {
        Handler handlerSpy = Mockito.spy(handler);
        Vector<Hero> heroes = new Vector<>();
        heroes.add(hero);
        handlerSpy.clients.addElement(new Entry(null, hero));
        handlerSpy.clients.addElement(new Entry(null, null));
        handlerSpy.sendHeros(connectionEndpoint);
        Mockito.verify(connectionEndpoint).send(heroes);
    }

    @Test
    public void testIsGmConnected() {
        handler.clients.add(new Entry("Gamemaster", null));
        assertEquals(true, handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() throws IOException {
        String message = "Message";
        handler.addConnection(connectionEndpoint);
        handler.sendOneSystemMsg(connectionEndpoint, message);
        assertEquals(message, connectionEndpoint.sentMessage);
    }

    @Test
    public void testSendAllSystemMsg() throws IOException {
        String message = "Message";
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        handler.sendAllSystemMsg(message);
        assertEquals(message, connectionEndpoint.sentMessage);
        assertEquals(message, connectionEndpoint2.sentMessage);
    }

    @Test
    public void testWhisper() throws IOException {
        String cmd = "to message";
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        handler.whisper(connectionEndpoint, cmd);
        assertEquals("name whispers: message", connectionEndpoint2.sentMessage);
        assertEquals("You whisper to name: message", connectionEndpoint.sentMessage);
    }

    @Test
    public void testRollDiceWithMod() throws IOException {
        String cmd = "1w6+5";
        int diceNumber = 5;
        handler.addConnection(connectionEndpoint);
        handler.rollDice(connectionEndpoint, cmd);
        assertEquals(diceNumber, connectionEndpoint.sentMessage);
    }

    @Test
    public void testRollDiceNoMod() throws IOException {
        String cmd = "1w6";
        handler.addConnection(connectionEndpoint);
        handler.rollDice(connectionEndpoint, cmd);
        assertEquals("5  ", connectionEndpoint.sentMessage);
    }

    @Test
    public void testUpdateHeroWithNameNull() throws IOException {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        handler.clients.get(0).name = "name";
        handler.updateHero(connectionEndpoint2, hero);
        assertNull(handler.getName(connectionEndpoint2));
    }

    @Test
    public void testSendAllStr() throws IOException {
        String message = "Message";
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        handler.sendAllStr(connectionEndpoint, message);
        assertEquals("name: Message", connectionEndpoint2.sentMessage);
        assertEquals("name: Message", connectionEndpoint.sentMessage);
    }

    @Test
    public void testSendAllObj() throws IOException {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);
        handler.sendAllObj(userEvent);
        assertEquals(userEvent, connectionEndpoint.sentObject);
        assertEquals(userEvent, connectionEndpoint2.sentObject);
    }

    @Test
    public void testGetName() {
        handler.addConnection(connectionEndpoint);
        assertEquals(null, handler.getName(connectionEndpoint2));
        assertEquals("", handler.getName(connectionEndpoint));
    }
}