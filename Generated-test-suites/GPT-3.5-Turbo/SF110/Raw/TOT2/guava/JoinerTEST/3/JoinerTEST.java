import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new String[]{"a", "b", "c"}));
    }

    @Test
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendToWithNullAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner.appendTo(null, Arrays.asList("a", "b", "c").iterator());
    }

    @Test
    public void testAppendToWithNullParts() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "c").iterator());
        assertEquals("a,c", builder.toString());
    }

    @Test
    public void testJoinWithNull() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b", joiner.join("a", null, "b"));
    }

    // Add more test cases as needed

}