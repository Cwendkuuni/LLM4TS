package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setUp() {
        indexedString = new IndexedString("abcabc");
    }

    @Test
    public void testCount() {
        Assert.assertEquals(2, indexedString.count('a'));
        Assert.assertEquals(2, indexedString.count('b'));
        Assert.assertEquals(2, indexedString.count('c'));
        Assert.assertEquals(0, indexedString.count('d'));
    }

    @Test
    public void testIndexOf() {
        Assert.assertEquals(0, indexedString.indexOf("abc".toCharArray()));
        Assert.assertEquals(3, indexedString.indexOf("abc".toCharArray()));
        Assert.assertEquals(-1, indexedString.indexOf("def".toCharArray()));
    }

    // Add more test cases for comprehensive testing of all methods

    // ...

    // 
    // ...

    // 
}