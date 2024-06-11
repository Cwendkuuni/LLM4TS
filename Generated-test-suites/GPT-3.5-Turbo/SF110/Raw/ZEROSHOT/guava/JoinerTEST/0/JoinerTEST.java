import org.junit.*;
import java.io.*;
import javax.annotation.*;
import java.util.*;
import com.google.common.annotations.*;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        Assert.assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        Assert.assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3));
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3).iterator());
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{1, 2, 3});
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3));
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3).iterator());
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{1, 2, 3});
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToStringBuilderWithObjects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        Assert.assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        Assert.assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        Assert.assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3).iterator()));
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        Assert.assertEquals("1,2,3", joiner.join(new Object[]{1, 2, 3}));
    }

    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        Assert.assertEquals("1,2,3", joiner.join(1, 2, 3));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        Assert.assertEquals("1,NULL,3", joiner.join(Arrays.asList(1, null, 3)));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Assert.assertEquals("1,3", joiner.join(Arrays.asList(1, null, 3)));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Assert.assertEquals("key1=value1,key2=value2", mapJoiner.join(getTestMap()));
    }

    private Map<String, String> getTestMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        return map;
    }
}