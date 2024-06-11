import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('@');
        assertNotNull(joiner);
        assertEquals("@", joiner.separator);
    }

    // More test cases for other methods can be added here

}