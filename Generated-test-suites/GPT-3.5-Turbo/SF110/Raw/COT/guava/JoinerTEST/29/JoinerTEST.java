
import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setup() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        StringBuffer buffer = new StringBuffer();
        Iterable<String> iterable = Arrays.asList("one", "two", "three");
        
        joiner.appendTo(buffer, iterable);
        
        assertEquals("one,two,three", buffer.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        
        joiner.appendTo(buffer, iterator);
        
        assertEquals("one,two,three", buffer.toString());
    }
    
    @Test
    public void testAppendToArray() throws IOException {
        StringBuffer buffer = new StringBuffer();
        String[] array = {"one", "two", "three"};
        
        joiner.appendTo(buffer, array);
        
        assertEquals("one,two,three", buffer.toString());
    }
    
    @Test
    public void testAppendToObjects() throws IOException {
        StringBuffer buffer = new StringBuffer();
        
        joiner.appendTo(buffer, "one", "two", "three");
        
        assertEquals("one,two,three", buffer.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Iterable<String> iterable = Arrays.asList("one", "two", "three");
        
        String result = joiner.join(iterable);
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinIterator() {
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();
        
        String result = joiner.join(iterator);
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinArray() {
        String[] array = {"one", "two", "three"};
        
        String result = joiner.join(array);
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinObjects() {
        String result = joiner.join("one", "two", "three");
        
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testUseForNull() throws IOException {
        StringBuffer buffer = new StringBuffer();
        joiner.useForNull("NULL").appendTo(buffer, "one", null, "three");
        
        assertEquals("one,NULL,three", buffer.toString());
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        StringBuffer buffer = new StringBuffer();
        joiner.skipNulls().appendTo(buffer, "one", null, "three");
        
        assertEquals("one,three", buffer.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMap() {
        joiner.skipNulls().withKeyValueSeparator("=").join(Arrays.asList("one=1", "two=null", "three=3"));
    }
    
    @Test
    public void testMapJoinerAppendToMap() throws IOException {
        StringBuffer buffer = new StringBuffer();
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        mapJoiner.appendTo(buffer, Arrays.asList("one=1", "two=2", "three=3"));
        
        assertEquals("one=1,two=2,three=3", buffer.toString());
    }
    
    @Test
    public void testMapJoinerAppendToStringBuilder() {
        StringBuilder builder = new StringBuilder();
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        mapJoiner.appendTo(builder, Arrays.asList("one=1", "two=2", "three=3"));
        
        assertEquals("one=1,two=2,three=3", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        String result = mapJoiner.join(Arrays.asList("one=1", "two=2", "three=3"));
        
        assertEquals("one=1,two=2,three=3", result);
    }
}

