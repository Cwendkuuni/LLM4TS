import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on("-");
        assertEquals("a-b-c-d", joiner.join(Arrays.asList("a", "b", "c", "d")));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('-');
        assertEquals("a-b-c-d", joiner.join(Arrays.asList("a", "b", "c", "d")));
    }

    @Test
    public void testAppendTo() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c", "d"));
        assertEquals("a-b-c-d", builder.toString());
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on("-");
        String result = joiner.join(Arrays.asList("a", "b", "c", "d"));
        assertEquals("a-b-c-d", result);
    }

    // Test edge cases and exceptions
    @Test
    public void testAppendTo_EmptyList() throws IOException {
        Joiner joiner = Joiner.on("-");
        List<String> emptyList = Arrays.asList();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, emptyList);
        assertEquals("", builder.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendTo_IOException() throws IOException {
        Joiner joiner = Joiner.on("-");
        List<String> listWithIOException = Arrays.asList("a", "b");
        joiner.appendTo(new FailingAppendable(), listWithIOException);
    }

    private static class FailingAppendable extends Appendable {
        @Override
        public Appendable append(CharSequence csq) {
            throw new IOException();
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) {
            throw new IOException();
        }

        @Override
        public Appendable append(char c) {
            throw new IOException();
        }
    }
}