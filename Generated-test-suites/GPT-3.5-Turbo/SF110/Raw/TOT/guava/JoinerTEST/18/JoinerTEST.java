import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testAppendToWithIterable_Success() throws IOException {
        List<String> list = Arrays.asList("One", "Two", "Three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);

        assertEquals("One,Two,Three", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterable_WithNullElement() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("One");
        list.add(null);
        list.add("Three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);

        assertEquals("One,null,Three", builder.toString());
    }
    
    @Test(expected = IOException.class)
    public void testAppendToWithIterable_Error() throws IOException {
        List<String> list = Arrays.asList("One", "Two", "Three");
        OutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);
        
        joiner.appendTo(appendable, list);
    }

    @Test
    public void testJoinWithIterable_Success() {
        List<String> list = Arrays.asList("One", "Two", "Three");
        String result = joiner.join(list);

        assertEquals("One,Two,Three", result);
    }
    
    @Test
    public void testJoinWithIterable_EmptyList() {
        List<String> list = new ArrayList<>();
        String result = joiner.join(list);

        assertEquals("", result);
    }

    @Test
    public void testJoinWithIterable_NullElement() {
        List<String> list = new ArrayList<>();
        list.add("One");
        list.add(null);
        list.add("Three");
        String result = joiner.join(list);

        assertEquals("One,null,Three", result);
    }
    
    @Test
    public void testJoinWithIterable_NullList() {
        List<String> list = null;
        String result = joiner.join(list);

        assertEquals("", result);
    }

    @Test
    public void testAppendToWithIterator_Success() throws IOException {
        List<String> list = Arrays.asList("One", "Two", "Three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());

        assertEquals("One,Two,Three", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterator_EmptyList() throws IOException {
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());

        assertEquals("", builder.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithIterator_Error() throws IOException {
        List<String> list = Arrays.asList("One", "Two", "Three");
        OutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);
        
        joiner.appendTo(appendable, list.iterator());
    }

    @Test
    public void testJoinWithIterator_Success() {
        List<String> list = Arrays.asList("One", "Two", "Three");
        String result = joiner.join(list.iterator());

        assertEquals("One,Two,Three", result);
    }
    
    @Test
    public void testJoinWithIterator_EmptyList() {
        List<String> list = new ArrayList<>();
        String result = joiner.join(list.iterator());

        assertEquals("", result);
    }
    
    @Test
    public void testJoinWithIterator_NullList() {
        List<String> list = null;
        String result = joiner.join(list.iterator());

        assertEquals("", result);
    }

    @Test
    public void testAppendToWithArray_Success() throws IOException {
        String[] array = {"One", "Two", "Three"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);

        assertEquals("One,Two,Three", builder.toString());
    }
    
    @Test
    public void testAppendToWithArray_NullElement() throws IOException {
        String[] array = {"One", null, "Three"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);

        assertEquals("One,null,Three", builder.toString());
    }
    
    @Test
    public void testAppendToWithArray_EmptyArray() throws IOException {
        String[] array = {};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);

        assertEquals("", builder.toString());
    }

    @Test(expected = IOException.class)
    public void testAppendToWithArray_Error() throws IOException {
        String[] array = {"One", "Two", "Three"};
        OutputStream outputStream = new ByteArrayOutputStream();
        Appendable appendable = new OutputStreamWriter(outputStream);
        
        joiner.appendTo(appendable, array);
    }

    @Test
    public void testJoinWithArray_Success() {
        String[] array = {"One", "Two", "Three"};
        String result = joiner.join(array);

        assertEquals("One,Two,Three", result);
    }
    
    @Test
    public void testJoinWithArray_NullElement() {
        String[] array = {"One", null, "Three"};
        String result = joiner.join(array);

        assertEquals("One,null,Three", result);
    }
    
    @Test
    public void testJoinWithArray_EmptyArray() {
        String[] array = {};
        String result = joiner.join(array);

        assertEquals("", result);
    }

    @Test
    public void testJoinWithTwoObjects_Success() {
        String result = joiner.join("One", "Two");

        assertEquals("One,Two", result);
    }
    
    @Test
    public void testJoinWithTwoObjects_NullFirst() {
        String result = joiner.join(null, "Two");

        assertEquals("null,Two", result);
    }
    
    @Test
    public void testJoinWithTwoObjects_NullSecond() {
        String result = joiner.join("One", null);

        assertEquals("One,null", result);
    }
    
    @Test
    public void testJoinWithTwoObjects_BothNull() {
        String result = joiner.join(null, null);

        assertEquals("null,null", result);
    }
    
    @Test
    public void testJoinWithThreeObjects_Success() {
        String result = joiner.join("One", "Two", "Three");

        assertEquals("One,Two,Three", result);
    }
    
    @Test
    public void testJoinWithThreeObjects_NullFirst() {
        String result = joiner.join(null, "Two", "Three");

        assertEquals("null,Two,Three", result);
    }
    
    @Test
    public void testJoinWithThreeObjects_NullSecond() {
        String result = joiner.join("One", null, "Three");

        assertEquals("One,null,Three", result);
    }
    
    @Test
    public void testJoinWithThreeObjects_NullThird() {
        String result = joiner.join("One", "Two", null);

        assertEquals("One,Two,null", result);
    }
    
    @Test
    public void testJoinWithThreeObjects_AllNull() {
        String result = joiner.join(null, null, null);

        assertEquals("null,null,null", result);
    }

    @Test
    public void testUseForNull_Success() {
        Joiner nullJoiner = joiner.useForNull("null");
        String result = nullJoiner.join("One", null, "Three");

        assertEquals("One,null,Three", result);
    }

    @Test
    public void testSkipNulls_Success() {
        Joiner skipNullsJoiner = joiner.skipNulls();
        String result = skipNullsJoiner.join("One", null, "Three");

        assertEquals("One,Three", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_AlreadySpecifiedUseForNull() {
        Joiner nullJoiner = joiner.useForNull("null");
        nullJoiner.useForNull("null2");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_AlreadySpecifiedSkipNulls() {
        Joiner skipNullsJoiner = joiner.skipNulls();
        skipNullsJoiner.useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_AlreadySpecifiedUseForNull() {
        Joiner nullJoiner = joiner.useForNull("null");
        nullJoiner.skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_AlreadySpecifiedSkipNulls() {
        Joiner skipNullsJoiner = joiner.skipNulls();
        skipNullsJoiner.skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_WithKeyValueSeparator() {
        Map<String, String> map = new HashMap<>();
        map.put("One", "1");
        map.put("Two", null);
        map.put("Three", "3");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        mapJoiner.skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator_Success() {
        Map<String, String> map = new HashMap<>();
        map.put("One", "1");
        map.put("Two", "2");
        map.put("Three", "3");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        String result = mapJoiner.join(map);

        assertEquals("One=1,Two=2,Three=3", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithKeyValueSeparator_WithSkipNulls() {
        Map<String, String> map = new HashMap<>();
        map.put("One", "1");
        map.put("Two", null);
        map.put("Three", "3");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        mapJoiner.skipNulls();
    }

}