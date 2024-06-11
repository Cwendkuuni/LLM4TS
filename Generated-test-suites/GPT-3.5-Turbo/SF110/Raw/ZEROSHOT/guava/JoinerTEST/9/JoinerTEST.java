import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = new Joiner(":");
    }

    @Test
    public void testOn() {
        Joiner result = Joiner.on(":");
        Assert.assertEquals(result.separator, ":");
    }

    @Test
    public void testOnChar() {
        Joiner result = Joiner.on(':');
        Assert.assertEquals(result.separator, ":");
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:two:three");
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        Iterator<String> parts = Arrays.asList("one", "two", "three").iterator();
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:two:three");
    }

    @Test
    public void testAppendToWithArray() throws IOException {
        StringBuilder builder = new StringBuilder();
        String[] parts = {"one", "two", "three"};
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:two:three");
    }

    @Test
    public void testAppendToWithObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"one", "two", "three"};
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:two:three");
    }

    @Test
    public void testJoinWithIterable() {
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts);

        Assert.assertEquals(result, "one:two:three");
    }

    @Test
    public void testJoinWithIterator() {
        Iterator<String> parts = Arrays.asList("one", "two", "three").iterator();
        String result = joiner.join(parts);

        Assert.assertEquals(result, "one:two:three");
    }

    @Test
    public void testJoinWithArray() {
        String[] parts = {"one", "two", "three"};
        String result = joiner.join(parts);

        Assert.assertEquals(result, "one:two:three");
    }

    @Test
    public void testJoinWithObjects() {
        Object[] parts = {"one", "two", "three"};
        String result = joiner.join(parts);

        Assert.assertEquals(result, "one:two:three");
    }

    @Test
    public void testUseForNull() {
        joiner = joiner.useForNull("null");

        StringBuilder builder = new StringBuilder();
        String[] parts = {"one", null, "three"};
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:null:three");
    }

    @Test
    public void testSkipNulls() {
        joiner = joiner.skipNulls();

        StringBuilder builder = new StringBuilder();
        String[] parts = {"one", null, "three"};
        joiner.appendTo(builder, parts);

        Assert.assertEquals(builder.toString(), "one:three");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        joiner = joiner.skipNulls().useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithKeyValueSeparator() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void testMapJoinerAppendToWithMap() throws IOException {
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        joiner.withKeyValueSeparator("=").appendTo(builder, map);

        Assert.assertEquals(builder.toString(), "one=1:two=2:three=3");
    }

    @Test
    public void testMapJoinerAppendToWithEntries() throws IOException {
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        joiner.withKeyValueSeparator("=").appendTo(builder, map.entrySet());

        Assert.assertEquals(builder.toString(), "one=1:two=2:three=3");
    }

    @Test
    public void testMapJoinerAppendToWithIterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        joiner.withKeyValueSeparator("=").appendTo(builder, map.entrySet().iterator());

        Assert.assertEquals(builder.toString(), "one=1:two=2:three=3");
    }

    @Test
    public void testMapJoinerJoinWithMap() {
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        String result = joiner.withKeyValueSeparator("=").join(map);

        Assert.assertEquals(result, "one=1:two=2:three=3");
    }

    @Test
    public void testMapJoinerJoinWithEntries() {
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        String result = joiner.withKeyValueSeparator("=").join(map.entrySet());

        Assert.assertEquals(result, "one=1:two=2:three=3");
    }

    @Test
    public void testMapJoinerJoinWithIterator() {
        Map<String, String> map = Map.of("one", "1", "two", "2", "three", "3");
        String result = joiner.withKeyValueSeparator("=").join(map.entrySet().iterator());

        Assert.assertEquals(result, "one=1:two=2:three=3");
    }
}