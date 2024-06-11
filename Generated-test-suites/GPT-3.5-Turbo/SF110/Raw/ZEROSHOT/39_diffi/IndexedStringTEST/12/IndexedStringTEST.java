package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        // Test count() method for each character
        IndexedString indexedString = new IndexedString("");
        Assert.assertEquals(0, indexedString.count('a'));
        
        indexedString = new IndexedString("a");
        Assert.assertEquals(1, indexedString.count('a'));
        
        indexedString = new IndexedString("aa");
        Assert.assertEquals(2, indexedString.count('a'));
        
        indexedString = new IndexedString("abcde");
        Assert.assertEquals(0, indexedString.count('f'));
        Assert.assertEquals(1, indexedString.count('a'));
        Assert.assertEquals(1, indexedString.count('b'));
        Assert.assertEquals(1, indexedString.count('c'));
        Assert.assertEquals(1, indexedString.count('d'));
        Assert.assertEquals(1, indexedString.count('e'));
    }
    
    @Test
    public void testIndexOf() {
        // Test indexOf() method for different target strings
        IndexedString indexedString = new IndexedString("");
        Assert.assertEquals(-1, indexedString.indexOf("a".toCharArray()));
        
        indexedString = new IndexedString("a");
        Assert.assertEquals(0, indexedString.indexOf("a".toCharArray()));
        Assert.assertEquals(-1, indexedString.indexOf("b".toCharArray()));
        
        indexedString = new IndexedString("aa");
        Assert.assertEquals(0, indexedString.indexOf("a".toCharArray()));
        Assert.assertEquals(1, indexedString.indexOf("a".toCharArray()));
        Assert.assertEquals(-1, indexedString.indexOf("b".toCharArray()));
        
        indexedString = new IndexedString("abcde");
        Assert.assertEquals(0, indexedString.indexOf("abc".toCharArray()));
        Assert.assertEquals(1, indexedString.indexOf("bcd".toCharArray()));
        Assert.assertEquals(2, indexedString.indexOf("cde".toCharArray()));
        Assert.assertEquals(-1, indexedString.indexOf("def".toCharArray()));
    }
    
}