package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testAppendTo() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testJoin() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"a", "b", "c"});
        assertEquals("a,b,c", result);
    }

    @Test
    public void testUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"a", null, "c"});
        assertEquals("a,null,c", builder.toString());
    }

    @Test
    public void testSkipNulls() throws Exception {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new String[]{"a", null, "c"});
        assertEquals("a,c", builder.toString());
    }

    @Test
    public void testWithKeyValueSeparator() throws Exception {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        joiner.withKeyValueSeparator("=").appendTo(builder, new String[]{"a:1", "b:2", "c:3"});
        assertEquals("a:1=b:2=c:3", builder.toString());
    }
}