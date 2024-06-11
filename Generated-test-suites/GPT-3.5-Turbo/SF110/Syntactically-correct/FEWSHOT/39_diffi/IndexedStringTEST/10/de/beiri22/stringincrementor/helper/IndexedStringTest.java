package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString str1 = new IndexedString("abc");
        assertEquals(1, str1.count('a'));
        assertEquals(1, str1.count('b'));
        assertEquals(1, str1.count('c'));
        assertEquals(0, str1.count('d'));
        
        IndexedString str2 = new IndexedString("aaabbbccc");
        assertEquals(3, str2.count('a'));
        assertEquals(3, str2.count('b'));
        assertEquals(3, str2.count('c'));
        assertEquals(0, str2.count('d'));
    }
    
    @Test
    public void testIndexOf() {
        IndexedString str = new IndexedString("abcdeabc");
        assertEquals(0, str.indexOf("abc".toCharArray()));
        assertEquals(5, str.indexOf("abc".toCharArray()));
        assertEquals(-1, str.indexOf("xyz".toCharArray()));
    }
}