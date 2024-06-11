import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
    @Test
    public void testIsEmpty() {
        TokenQueue tokenQueue = new TokenQueue("");
        assertTrue(tokenQueue.isEmpty());
    }
}