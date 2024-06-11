import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import dsachat.server.Handler;
import dsachat.share.ChallReq;
import dsachat.share.Challenge;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {
    Handler handler;

    @Before
    public void setup() {
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
    public void testHandleInput() {
        // TODO: Implement test for handleInput method
    }

    @Test
    public void testSendHeros() {
        // TODO: Implement test for sendHeros method
    }

    @Test
    public void testIsGmConnected() {
        // TODO: Implement test for isGmConnected method
    }

    @Test
    public void testSendOneSystemMsg() throws IOException {
        // TODO: Implement test for sendOneSystemMsg method
    }

    @Test
    public void testSendAllSystemMsg() throws IOException {
        // TODO: Implement test for sendAllSystemMsg method
    }

    @Test
    public void testWhisper() {
        // TODO: Implement test for whisper method
    }

    @Test
    public void testRollDice() {
        // TODO: Implement test for rollDice method
    }

    @Test
    public void testSendToOneStr() throws IOException {
        // TODO: Implement test for sendToOneStr method
    }

    @Test
    public void testUpdateHero() {
        // TODO: Implement test for updateHero method
    }

    @Test
    public void testSendAllStr() throws IOException {
        // TODO: Implement test for sendAllStr method
    }

    @Test
    public void testSendAllObj() throws IOException {
        // TODO: Implement test for sendAllObj method
    }

    @Test
    public void testGetName() {
        // TODO: Implement test for getName method
    }
}