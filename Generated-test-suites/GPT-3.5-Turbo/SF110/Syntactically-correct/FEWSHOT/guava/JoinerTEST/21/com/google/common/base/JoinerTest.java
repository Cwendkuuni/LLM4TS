package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new String[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, new String[]{"d", "e", "f"});
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(',');

        String result = joiner.join(new String[]{"a", "b", "c"});
        assertEquals("a,b,c", result);

        result = joiner.join(new String[]{"d", "e", "f"});
        assertEquals("d,e,f", result);
    }

    @Test
    public void testAppendToIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, Arrays.asList("d", "e", "f"));
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToIterator() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, Arrays.asList("d", "e", "f").iterator());
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToObjectArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{"d", "e", "f"});
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToObjects() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, "d", "e", "f");
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToWithNull() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{"a", null, "c"});
        assertEquals("a,null,c", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{"d", "e", null});
        assertEquals("a,null,c,d,e,null", builder.toString());
    }

    @Test
    public void testAppendToStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, Arrays.asList("d", "e", "f"));
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToIteratorWithStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, Arrays.asList("d", "e", "f").iterator());
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToObjectArrayWithStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{"d", "e", "f"});
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToObjectsWithStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());

        builder = joiner.appendTo(builder, "d", "e", "f");
        assertEquals("a,b,c,d,e,f", builder.toString());
    }

    @Test
    public void testAppendToWithNullWithStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{"a", null, "c"});
        assertEquals("a,null,c", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{"d", "e", null});
        assertEquals("a,null,c,d,e,null", builder.toString());
    }

    @Test
    public void testJoinIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", result);

        result = joiner.join(Arrays.asList("d", "e", "f"));
        assertEquals("d,e,f", result);
    }

    @Test
    public void testJoinIterator() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", result);

        result = joiner.join(Arrays.asList("d", "e", "f").iterator());
        assertEquals("d,e,f", result);
    }

    @Test
    public void testJoinObjectArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", result);

        result = joiner.join(new Object[]{"d", "e", "f"});
        assertEquals("d,e,f", result);
    }

    @Test
    public void testJoinObjects() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("a", "b", "c");
        assertEquals("a,b,c", result);

        result = joiner.join("d", "e", "f");
        assertEquals("d,e,f", result);
    }

    @Test
    public void testJoinWithNull() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"a", null, "c"});
        assertEquals("a,null,c", result);

        result = joiner.join(new Object[]{"d", "e", null});
        assertEquals("d,e,null", result);
    }

    @Test
    public void testUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder builder = new StringBuilder();

        builder = joiner.appendTo(builder, new Object[]{"a", null, "c"});
        assertEquals("a,null,c", builder.toString());

        builder = joiner.appendTo(builder, new Object[]{"d", "e", null});
        assertEquals("a,null,c,d,e,null", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAfterUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null");
        joiner.useForNull("null again");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsAfterUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null");
        joiner.skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() throws Exception {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator(":");
    }

}