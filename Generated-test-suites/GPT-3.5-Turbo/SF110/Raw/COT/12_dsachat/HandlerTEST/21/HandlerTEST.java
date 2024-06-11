
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class HandlerTest {

    Handler handler;

    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        assertEquals(1, handler.clients.size());
        assertEquals(cl, handler.clients.get(0).ce);
        assertEquals("", handler.clients.get(0).name);
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = new ConnectionEndpoint();
        handler.addConnection(cl);
        handler.removeConnection(cl);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput_String_wuerfel() {
        ConnectionEndpoint con = new ConnectionEndpoint();
        InputEvent e = new InputEvent(con, "wuerfel 6");
        handler.handleInput(e);
        // TODO: verify the actions performed by the method
    }

    // TODO: Add test cases for other public methods in Handler class

}

