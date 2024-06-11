import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
import com.google.common.base.Joiner;

public class JoinerTest {

    @Test
    public void testAppendTo_withIterable_shouldReturnAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable result = joiner.appendTo(outputStream, list);
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withIterator_shouldReturnAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable result = joiner.appendTo(outputStream, list.iterator());
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withObjectArray_shouldReturnAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] array = {"Hello", "World"};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable result = joiner.appendTo(outputStream, array);
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withObjects_shouldReturnAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Appendable result = joiner.appendTo(outputStream, "Hello", "World");
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterable_shouldReturnStringBuilder() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, list);
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterator_shouldReturnStringBuilder() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, list.iterator());
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjectArray_shouldReturnStringBuilder() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"Hello", "World"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, array);
        assertNotNull(result);
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjects_shouldReturnStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "Hello", "World");
        assertNotNull(result);
    }

    @Test
    public void testJoin_withIterable_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        String result = joiner.join(list);
        assertNotNull(result);
    }

    @Test
    public void testJoin_withIterator_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        String result = joiner.join(list.iterator());
        assertNotNull(result);
    }

    @Test
    public void testJoin_withObjectArray_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"Hello", "World"};
        String result = joiner.join(array);
        assertNotNull(result);
    }

    @Test
    public void testJoin_withObjects_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("Hello", "World");
        assertNotNull(result);
    }

    @Test
    public void testUseForNull_withNullText_shouldReturnJoinerInstance() {
        Joiner joiner = Joiner.on(",");
        Joiner result = joiner.useForNull("NULL");
        assertNotNull(result);
    }

    @Test
    public void testSkipNulls_shouldReturnJoinerInstance() {
        Joiner joiner = Joiner.on(",");
        Joiner result = joiner.skipNulls();
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparator_shouldReturnMapJoinerInstance() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner result = joiner.withKeyValueSeparator("=");
        assertNotNull(result);
    }
}