package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        // Test case 1: When both strings are empty
        RelativeString result1 = StringIncrementor.diff("", "");
        assertTrue(result1.isEmpty());
        
        // Test case 2: When both strings are identical
        RelativeString result2 = StringIncrementor.diff("abc", "abc");
        assertTrue(result2.isEmpty());
        
        // Test case 3: When a string is empty
        RelativeString result3 = StringIncrementor.diff("", "abc");
        assertEquals("abc", result3.getAbsolute());
        assertEquals(0, result3.linksCount());
        
        // Test case 4: When b string is empty
        RelativeString result4 = StringIncrementor.diff("abc", "");
        assertTrue(result4.isEmpty());
        
        // Test case 5: When a string is a substring of b string
        RelativeString result5 = StringIncrementor.diff("abc", "abcd");
        assertEquals("d", result5.getAbsolute());
        assertEquals(1, result5.linksCount());
        assertEquals(new StringLink(0, 3, 3), result5.getLink(0));
        
        // Test case 6: When b string is a substring of a string
        RelativeString result6 = StringIncrementor.diff("abcd", "abc");
        assertEquals("", result6.getAbsolute());
        assertEquals(0, result6.linksCount());
        
        // Test case 7: When both strings have common substring
        RelativeString result7 = StringIncrementor.diff("abcde", "abfde");
        assertEquals("f", result7.getAbsolute());
        assertEquals(1, result7.linksCount());
        assertEquals(new StringLink(2, 2, 1), result7.getLink(0));
    }
    
    @Test
    public void testPatch() {
        // Test case 1: When relative string is empty
        String result1 = StringIncrementor.patch("abc", new RelativeString());
        assertEquals("abc", result1);
        
        // Test case 2: When relative string has one link
        RelativeString r2 = new RelativeString();
        r2.addLink(new StringLink(0, 3, 3));
        String result2 = StringIncrementor.patch("xyz", r2);
        assertEquals("xyzabc", result2);
        
        // Test case 3: When relative string has multiple links
        RelativeString r3 = new RelativeString();
        r3.addLink(new StringLink(0, 2, 1));
        r3.addLink(new StringLink(4, 7, 3));
        String result3 = StringIncrementor.patch("aeiou", r3);
        assertEquals("abceiou", result3);
    }
}