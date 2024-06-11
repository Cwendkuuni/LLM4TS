
package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.*;
import dsachat.share.hero.HeroInfo;
import dsachat.share.hero.Race;
import dsachat.share.hero.Attribute;
import dsachat.share.hero.Skill;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connection;

    @Before
    public void setUp() {
        handler = new Handler();
        connection = new ConnectionEndpoint();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connection);
        Vector<Entry> clients = handler.getEntryVector();
        assertEquals(1, clients.size());
        assertEquals(connection, clients.get(0).ce);
        assertEquals("", clients.get(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connection);
        handler.removeConnection(connection);
        Vector<Entry> clients = handler.getEntryVector();
        assertEquals(0, clients.size());
    }

    @Test
    public void testHandleInputWithValidStringCommand() {
        handler.addConnection(connection);
        connectionStub(true, "Test Message");
        InputEvent inputEvent = new InputEvent(connection, "Test Message");
        handler.handleInput(inputEvent);
        // assert implementation
    }

    @Test
    public void testHandleInputWithInvalidStringCommand() {
        handler.addConnection(connection);
        connectionStub(true, "/invalid");
        InputEvent inputEvent = new InputEvent(connection, "/invalid");
        handler.handleInput(inputEvent);
        // assert implementation
    }

    @Test
    public void testHandleInputWithAttributeHero() {
        handler.addConnection(connection);
        connectionStub(true, new Hero(new UserEvent(false, "Test Player"), "Test Hero", new HeroInfo(Race.HUMAN, Attribute.KNOWLEDGE, 0, 0, 0, 0), new Skill[0]));
        InputEvent inputEvent = new InputEvent(connection, new Hero(new UserEvent(false, "Test Player"), "Test Hero", new HeroInfo(Race.HUMAN, Attribute.KNOWLEDGE, 0, 0, 0, 0), new Skill[0]));
        handler.handleInput(inputEvent);
        // assert implementation
    }

    @Test
    public void testHandleInputWithChallReq() {
        handler.addConnection(connection);
        connectionStub(true, new ChallReq("Test Player"));
        InputEvent inputEvent = new InputEvent(connection, new ChallReq("Test Player"));
        handler.handleInput(inputEvent);
        // assert implementation
    }

    private void connectionStub(boolean connected, Object input) {
        connectionStub(connected);
        try {
            connection.input(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectionStub(boolean connected) {
        connection.setConnected(connected);
    }
}
