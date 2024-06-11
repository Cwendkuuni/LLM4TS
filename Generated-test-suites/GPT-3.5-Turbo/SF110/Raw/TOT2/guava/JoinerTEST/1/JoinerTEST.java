import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
    
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on("_");
        assertNotNull(joiner);
        assertEquals("_", joiner.separator);
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(' ');
        assertNotNull(joiner);
        assertEquals(" ", joiner.separator);
    }
    
    @Test
    public void testToString() {
        Joiner joiner = Joiner.on("_");
        String result = joiner.toString("test");
        assertEquals("test", result);
    }
    
    @Test
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on("_");
        StringBuilder builder = new StringBuilder();
        builder = joiner.appendTo(builder, new String[]{"one", "two", "three"});
        assertEquals("one_two_three", builder.toString());
    }
    
    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on("_");
        String result = joiner.join(new String[]{"one", "two", "three"});
        assertEquals("one_two_three", result);
    }
    
    @Test(expected = IOException.class)
    public void testAppendToWithException() throws IOException {
        Joiner joiner = Joiner.on("_");
        joiner.appendTo(new MockAppendable(), new String[]{"one", "two", "three"});
    }
    
    class MockAppendable implements Appendable {
        public Appendable append(CharSequence csq) throws IOException {
            throw new IOException();
        }
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            throw new IOException();
        }
        public Appendable append(char c) throws IOException {
            throw new IOException();
        }
    }
}