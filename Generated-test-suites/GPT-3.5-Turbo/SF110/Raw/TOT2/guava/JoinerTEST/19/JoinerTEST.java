import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assertNotNull(joiner);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSeparator() {
        Joiner joiner = Joiner.on(null);
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    // Add more test cases for other methods as needed

}