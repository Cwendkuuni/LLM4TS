import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

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
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner modifiedJoiner = joiner.useForNull("NULL");
        assertNotNull(modifiedJoiner);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner modifiedJoiner = joiner.skipNulls();
        assertNotNull(modifiedJoiner);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
    }
    
    // More test cases can be added for other methods
    
}