import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(':');
        List<String> result = splitter.splitToList("a:b:c:d:e");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), result);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(":,"));
        List<String> result = splitter.splitToList("a,b:c,d:e,f");
        assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f"), result);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("/");
        List<String> result = splitter.splitToList("path/to/file");
        assertEquals(Arrays.asList("path", "to", "file"), result);
    }
    
    // Add tests for other methods in the Splitter class

}