
package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
    
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("aabbcc");
        char c = 'a';
        int expectedResult = 2;
        int actualResult = indexedString.count(c);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testIndexedString() {
        String s = "aabbcc";
        IndexedString indexedString = new IndexedString(s);
        assertNotNull(indexedString);
    }
    
    @Test
    public void testIndexOf() {
        String s = "aabbcc";
        IndexedString indexedString = new IndexedString(s);
        char[] target = {'a', 'b'};
        int expectedResult = 1;
        int actualResult = indexedString.indexOf(target);
        assertEquals(expectedResult, actualResult);
    }
}
