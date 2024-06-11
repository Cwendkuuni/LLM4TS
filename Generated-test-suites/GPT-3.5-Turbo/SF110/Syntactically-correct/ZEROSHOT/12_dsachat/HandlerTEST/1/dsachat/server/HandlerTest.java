package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Vector;

import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.Hero;
import dsachat.share.InputEvent;
import dsachat.share.UserEvent;

public class HandlerTest {
    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private InputEvent inputEvent;
    private Vector<Entry> clients;

    private class Entry {
        ConnectionEndpoint ce;
        String name;
        Hero hero;
    }

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = mock(ConnectionEndpoint.class);
        inputEvent = mock(InputEvent.class);
        clients = new Vector<>();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        verify(connectionEndpoint, atLeastOnce()).isConnected();
    }

    @Test
    public void testRemoveConnection() {
        Entry entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "";
        handler.clients.addElement(entry);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput_StringCommandRollDice() {
        String command = "/dice 6";
        when(inputEvent.getData()).thenReturn(command);
        when(inputEvent.getSource()).thenReturn(connectionEndpoint);

        handler.handleInput(inputEvent);

        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testHandleInput_Challenge() throws IOException {
        Challenge challenge = mock(Challenge.class);
        when(inputEvent.getData()).thenReturn(challenge);
        when(inputEvent.getSource()).thenReturn(connectionEndpoint);

        handler.handleInput(inputEvent);

        verify(challenge, times(1)).roll();
        verify(challenge, times(1)).getTo();
        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testHandleInput_ChallReq() throws IOException {
        ChallReq challReq = mock(ChallReq.class);
        when(challReq.getTo()).thenReturn("to");
        when(inputEvent.getData()).thenReturn(challReq);

        handler.clients = clients;
        Entry entry = new Entry();
        entry.name = "to";
        entry.ce = connectionEndpoint;
        handler.clients.addElement(entry);

        handler.handleInput(inputEvent);

        verify(challReq, times(1)).getTo();
        verify(connectionEndpoint, times(1)).send(challReq);
    }

    @Test
    public void testHandleInput_Hero() throws IOException {
        Hero hero = mock(Hero.class);
        when(hero.getName()).thenReturn("name");
        when(inputEvent.getData()).thenReturn(hero);
        when(inputEvent.getSource()).thenReturn(connectionEndpoint);

        handler.handleInput(inputEvent);

        verify(hero, times(1)).getName();
        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testHandleInput_UserEvent() throws IOException {
        UserEvent userEvent = mock(UserEvent.class);
        when(inputEvent.getData()).thenReturn(userEvent);
        when(inputEvent.getSource()).thenReturn(connectionEndpoint);

        handler.handleInput(inputEvent);
        
        verify(userEvent, times(1)).getName();
        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testSendHeros() throws IOException {
        Entry entry = new Entry();
        entry.hero = mock(Hero.class);
        handler.clients.addElement(entry);
        when(connectionEndpoint.isConnected()).thenReturn(true);

        handler.sendHeros(connectionEndpoint);

        verify(connectionEndpoint, times(1)).send(any());
    }

    @Test
    public void testIsGmConnected() {
        Entry entry = new Entry();
        entry.name = "Gamemaster";
        handler.clients.addElement(entry);

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() throws IOException {
        String message = "message";

        handler.sendOneSystemMsg(connectionEndpoint, message);

        verify(connectionEndpoint, times(1)).send(message);
    }

    @Test
    public void testSendAllSystemMsg() throws IOException {
        Entry entry1 = new Entry();
        Entry entry2 = new Entry();
        entry1.ce = mock(ConnectionEndpoint.class);
        entry2.ce = mock(ConnectionEndpoint.class);
        handler.clients.addElement(entry1);
        handler.clients.addElement(entry2);
        String message = "message";

        handler.sendAllSystemMsg(message);

        verify(entry1.ce, times(1)).send(message);
        verify(entry2.ce, times(1)).send(message);
    }

    @Test
    public void testWhisper() throws IOException {
        Entry entry = new Entry();
        entry.name = "to";
        entry.ce = mock(ConnectionEndpoint.class);
        handler.clients.addElement(entry);
        String cmd = "to message";

        handler.whisper(connectionEndpoint, cmd);

        verify(entry.ce, times(1)).send(anyString());
        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testRollDice_WithMod() throws IOException {
        String cmd = "2w6+3";

        handler.rollDice(connectionEndpoint, cmd);

        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testRollDice_WithoutMod() throws IOException {
        String cmd = "2w6";

        handler.rollDice(connectionEndpoint, cmd);

        verify(connectionEndpoint, times(1)).send(anyString());
    }

    @Test
    public void testSendToOneStr() throws IOException {
        Entry entry = new Entry();
        entry.name = "to";
        entry.ce = mock(ConnectionEndpoint.class);
        handler.clients.addElement(entry);
        String send = "send";

        handler.sendToOneStr(connectionEndpoint, "to", send);

        verify(entry.ce, times(1)).send(anyString());
    }

    @Test
    public void testUpdateHero_HeroIsNull() {
        Entry entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "";
        handler.clients.addElement(entry);
        Hero hero = mock(Hero.class);

        assertNull(handler.updateHero(connectionEndpoint, hero));
    }

    @Test
    public void testUpdateHero() {
        Entry entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "";
        handler.clients.addElement(entry);
        Hero hero = mock(Hero.class);

        String name = "name";
        when(hero.getName()).thenReturn(name);
        
        assertEquals(name, handler.updateHero(connectionEndpoint, hero));
    }

    @Test
    public void testSendAllStr() throws IOException {
        Entry entry = new Entry();
        entry.ce = mock(ConnectionEndpoint.class);
        handler.clients.addElement(entry);
        String string = "string";

        handler.sendAllStr(connectionEndpoint, string);

        verify(entry.ce, times(1)).send(anyString());
    }

    @Test
    public void testSendAllObj() throws IOException {
        Entry entry = new Entry();
        entry.ce = mock(ConnectionEndpoint.class);
        handler.clients.addElement(entry);
        Object object = mock(Object.class);

        handler.sendAllObj(object);

        verify(entry.ce, times(1)).send(object);
    }

    @Test
    public void testGetName() {
        Entry entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "name";
        handler.clients.addElement(entry);

        assertEquals("name", handler.getName(connectionEndpoint));
    }
}