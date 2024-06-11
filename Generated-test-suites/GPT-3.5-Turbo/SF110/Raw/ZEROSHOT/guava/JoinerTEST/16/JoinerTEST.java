import org.junit.*;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testAppendTo_withIterable() throws IOException {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        Assert.assertEquals("Hello,World", builder.toString());
    }

    @Test
    public void testAppendTo_withIterator() throws IOException {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts.iterator());
        Assert.assertEquals("Hello,World", builder.toString());
    }

    @Test
    public void testAppendTo_withArray() throws IOException {
        String[] parts = {"Hello", "World"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        Assert.assertEquals("Hello,World", builder.toString());
    }

    @Test
    public void testAppendTo_withObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "Hello", "World", "!");
        Assert.assertEquals("Hello,World,!", builder.toString());
    }

    @Test
    public void testJoin_withIterable() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts);
        Assert.assertEquals("Hello,World", result);
    }

    @Test
    public void testJoin_withIterator() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts.iterator());
        Assert.assertEquals("Hello,World", result);
    }

    @Test
    public void testJoin_withArray() {
        String[] parts = {"Hello", "World"};
        String result = joiner.join(parts);
        Assert.assertEquals("Hello,World", result);
    }

    @Test
    public void testJoin_withObjects() {
        String result = joiner.join("Hello", "World", "!");
        Assert.assertEquals("Hello,World,!", result);
    }

    @Test
    public void testUseForNull() {
        Joiner customJoiner = joiner.useForNull("null");
        String result = customJoiner.join(Arrays.asList("Hello", null, "World"));
        Assert.assertEquals("Hello,null,World", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner customJoiner = joiner.skipNulls();
        String result = customJoiner.join(Arrays.asList("Hello", null, "World"));
        Assert.assertEquals("Hello,World", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").skipNulls();
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        String result = mapJoiner.join(Collections.singletonMap("Hello", "World"));
        Assert.assertEquals("Hello:World", result);
    }

}