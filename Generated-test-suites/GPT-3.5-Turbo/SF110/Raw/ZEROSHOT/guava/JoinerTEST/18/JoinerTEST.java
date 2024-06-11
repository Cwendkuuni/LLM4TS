import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    @Test
    public void testAppendTo_withIterable() {
        Joiner joiner = Joiner.on(", ");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("1, 2, 3", sb.toString());
    }
    
    @Test
    public void testAppendTo_withIterator() {
        Joiner joiner = Joiner.on(", ");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, list.iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("1, 2, 3", sb.toString());
    }
    
    @Test
    public void testAppendTo_withObjectArray() {
        Joiner joiner = Joiner.on(", ");
        Object[] array = new Object[]{"a", "b", "c"};
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, array);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("a, b, c", sb.toString());
    }
    
    @Test
    public void testAppendTo_withObjects() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, "a", "b", "c");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("a, b, c", sb.toString());
    }
    
    @Test
    public void testJoin_withIterable() {
        Joiner joiner = Joiner.on(", ");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        String result = joiner.join(list);
        assertEquals("1, 2, 3", result);
    }
    
    @Test
    public void testJoin_withIterator() {
        Joiner joiner = Joiner.on(", ");
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        String result = joiner.join(list.iterator());
        assertEquals("1, 2, 3", result);
    }
    
    @Test
    public void testJoin_withObjectArray() {
        Joiner joiner = Joiner.on(", ");
        Object[] array = new Object[]{"a", "b", "c"};
        String result = joiner.join(array);
        assertEquals("a, b, c", result);
    }
    
    @Test
    public void testJoin_withObjects() {
        Joiner joiner = Joiner.on(", ");
        String result = joiner.join("a", "b", "c");
        assertEquals("a, b, c", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(", ").useForNull("null");
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("c");
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("a, null, c", sb.toString());
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("c");
        StringBuilder sb = new StringBuilder();
        try {
            joiner.appendTo(sb, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("a, c", sb.toString());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(", ").withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        String result = joiner.join(map);
        assertEquals("a=1, b=2, c=3", result);
    }

}