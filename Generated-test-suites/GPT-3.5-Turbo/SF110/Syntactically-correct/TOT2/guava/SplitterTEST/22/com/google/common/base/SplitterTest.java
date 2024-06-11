package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        int count = 0;
        for (String str : result) {
            count++;
        }
        assertEquals(3, count);
    }

    // Additional test cases for the methods in Splitter class can be added here
}