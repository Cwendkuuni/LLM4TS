
package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

    @Test
    public void testCount_WhenNoMatchingCharacter_ThenReturnZero() {
        IndexedString indexedString = new IndexedString("abcdefg");
        int count = indexedString.count('h');
        
        assertEquals(0, count);
    }
    
    @Test
    public void testCount_WhenMatchingCharacterExists_ThenReturnCount() {
        IndexedString indexedString = new IndexedString("Hello World!");
        int count = indexedString.count('o');
        
        assertEquals(2, count);
    }
    
    @Test
    public void testIndexOf_WhenTargetNotFound_ThenReturnMinusOne() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "xyz".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetFoundAtStart_ThenReturnStartIndex() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "Hello".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetFoundAtMiddle_ThenReturnMiddleIndex() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "World".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(6, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetFoundAtEnd_ThenReturnEndIndex() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "d!".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(11, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetLengthIsZero_ThenReturnMinusOne() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetLengthIsGreaterThanIndexedStringLength_ThenReturnMinusOne() {
        IndexedString indexedString = new IndexedString("Hello World!");
        char[] target = "Hello World!!".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_WhenTargetContainsSpecialCharacters_ThenReturnIndex() {
        IndexedString indexedString = new IndexedString("!@#$%^&*()");
        char[] target = "!@#$%^&*()".toCharArray();
        int index = indexedString.indexOf(target);
        
        assertEquals(0, index);
    }
}
