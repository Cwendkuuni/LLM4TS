
package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount_singleCharacter() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
    }
    
    @Test
    public void testCount_multipleCharacters() {
        IndexedString indexedString = new IndexedString("abbc");
        assertEquals(1, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
    }
    
    @Test
    public void testIndexOf_singleCharacterInString() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals(0, indexedString.indexOf(new char[] {'a'}));
        assertEquals(1, indexedString.indexOf(new char[] {'b'}));
        assertEquals(2, indexedString.indexOf(new char[] {'c'}));
    }
    
    @Test
    public void testIndexOf_singleCharacterNotInString() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals(-1, indexedString.indexOf(new char[] {'d'}));
    }
    
    @Test
    public void testIndexOf_multipleCharactersNotFound() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals(-1, indexedString.indexOf(new char[] {'a', 'b', 'c', 'd'}));
    }
    
    @Test
    public void testIndexOf_multipleCharactersFound() {
        IndexedString indexedString = new IndexedString("abcabc");
        assertEquals(0, indexedString.indexOf(new char[] {'a', 'b', 'c'}));
        assertEquals(3, indexedString.indexOf(new char[] {'a', 'b', 'c'}));
    }
}
