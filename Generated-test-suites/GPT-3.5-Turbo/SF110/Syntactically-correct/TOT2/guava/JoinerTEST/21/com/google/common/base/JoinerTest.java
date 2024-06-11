package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('-');
        assertEquals("1-2-3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.useForNull("null");
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.skipNulls();
        assertEquals("a,c", joiner.join(new String[]{"a", null, "c"}));
    }

    // Add more test cases for other methods as needed

}