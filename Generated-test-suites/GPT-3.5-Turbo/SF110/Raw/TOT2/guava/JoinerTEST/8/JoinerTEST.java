package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on("@");
        assertNotNull(joiner);
        assertEquals("@", joiner.separator);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('*');
        assertNotNull(joiner);
        assertEquals("*", joiner.separator);
    }

    // Add more test cases for other public methods here
    
    @Test
    public void testAppendToAppendable() {
        // Test case for appendTo method with Appendable input
    }

    @Test
    public void testAppendToIterator() {
        // Test case for appendTo method with Iterator input
    }

    // Add more test cases for other public methods here

    @Test
    public void testUseForNull() {
        // Test case for useForNull method
    }

    @Test
    public void testSkipNulls() {
        // Test case for skipNulls method
    }

    @Test
    public void testWithKeyValueSeparator() {
        // Test case for withKeyValueSeparator method
    }

    // Add more test cases for other public methods here
    
    @Test
    public void testAppendToMap() {
        // Test case for appendTo method with Map input
    }

    // Add more test cases for inner classes if needed

}