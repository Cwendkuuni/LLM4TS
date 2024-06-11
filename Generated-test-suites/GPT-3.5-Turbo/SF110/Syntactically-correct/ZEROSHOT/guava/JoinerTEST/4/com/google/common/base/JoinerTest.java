import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String expectedResult = "one,two,three";
        String actualResult = joiner.join("one", "two", "three");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAppendTo_withIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"one", "two", "three"}.iterator());
        String expectedResult = "one,two,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAppendTo_withArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"one", "two", "three"});
        String expectedResult = "one,two,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAppendTo_withIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("one", "two", "three"));
        String expectedResult = "one,two,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAppendTo_withObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "one", "two", "three");
        String expectedResult = "one,two,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        joiner.withKeyValueSeparator("=").appendTo(builder, Arrays.asList("one", "two", "three"));
        String expectedResult = "one=two,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("one", null, "three"));
        String expectedResult = "one,null,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("one", null, "three"));
        String expectedResult = "one,three";
        String actualResult = builder.toString();
        assertEquals(expectedResult, actualResult);
    }
}