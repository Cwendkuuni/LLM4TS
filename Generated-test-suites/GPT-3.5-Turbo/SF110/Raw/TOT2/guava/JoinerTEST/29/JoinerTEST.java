import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {
    
    @Test
    public void testJoinerOn_StringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoinerOn_CharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertEquals("a;b;c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoinerAppendTo() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        assertEquals("a-b-c", builder.toString());
    }
    
    // Additional test cases can be added here to cover edge cases and exceptions
    
}