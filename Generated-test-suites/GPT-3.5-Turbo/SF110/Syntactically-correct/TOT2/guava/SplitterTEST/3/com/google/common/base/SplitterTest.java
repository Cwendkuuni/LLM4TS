package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        char separator = '.';
        Splitter splitter = Splitter.on(separator);
        assertNotNull(splitter);
        // Add more assertions as needed
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
        // Add more assertions as needed
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertNotNull(splitter);
        // Add more assertions as needed
    }

    // Add more test cases as provided by the experts

}

