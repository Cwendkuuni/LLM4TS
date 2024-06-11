import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = new Joiner("-");
    }

    @Test
    public void testOnStringSeparator() {
        assertEquals(Joiner.on("-"), joiner);
    }

    @Test
    public void testOnCharSeparator() {
        assertEquals(Joiner.on('-'), joiner);
    }

    @Test
    public void testAppendToWithIterable() {
        List<String> list = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("hello-world", builder.toString());
    }

    @Test
    public void testAppendToWithIterator() {
        List<String> list = Arrays.asList("hello", "world");
        Iterator<String> iterator = list.iterator();
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("hello-world", builder.toString());
    }

    @Test
    public void testAppendToWithObjectArray() {
        String[] array = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, array);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("hello-world", builder.toString());
    }

    @Test
    public void testAppendToWithObjects() {
        Object[] objects = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        try {
            joiner.appendTo(builder, objects[0], objects[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("hello-world", builder.toString());
    }

    @Test
    public void testJoinWithIterable() {
        List<String> list = Arrays.asList("hello", "world");
        String join = joiner.join(list);
        assertEquals("hello-world", join);
    }

    @Test
    public void testJoinWithIterator() {
        List<String> list = Arrays.asList("hello", "world");
        Iterator<String> iterator = list.iterator();
        String join = joiner.join(iterator);
        assertEquals("hello-world", join);
    }

    @Test
    public void testJoinWithObjectArray() {
        String[] array = {"hello", "world"};
        String join = joiner.join(array);
        assertEquals("hello-world", join);
    }

    @Test
    public void testJoinWithObjects() {
        Object[] objects = {"hello", "world"};
        String join = joiner.join(objects[0], objects[1]);
        assertEquals("hello-world", join);
    }

    @Test
    public void testUseForNull() {
        String nullText = "null";
        Joiner newJoiner = joiner.useForNull(nullText);
        assertEquals(newJoiner, joiner);
    }

    @Test
    public void testSkipNulls() {
        Joiner newJoiner = joiner.skipNulls();
        assertEquals(newJoiner, joiner);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMaps() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        mapJoiner.skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertEquals(mapJoiner, joiner);
    }
}