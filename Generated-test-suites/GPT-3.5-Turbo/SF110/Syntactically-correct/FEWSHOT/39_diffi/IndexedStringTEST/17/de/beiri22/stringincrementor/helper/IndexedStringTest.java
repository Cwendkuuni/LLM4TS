
package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {
    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
    }
    
    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abc");
        assertEquals('a', indexedString.values[0]);
        assertEquals('b', indexedString.values[1]);
        assertEquals('c', indexedString.values[2]);
        
        assertEquals(1, indexedString.index['a'].length);
        assertEquals(0, indexedString.index['a'][0]);
        
        assertEquals(1, indexedString.index['b'].length);
        assertEquals(1, indexedString.index['b'][0]);
        
        assertEquals(1, indexedString.index['c'].length);
        assertEquals(2, indexedString.index['c'][0]);
        
        assertEquals(null, indexedString.index['d']);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abc");
        
        char[] target1 = {'a'};
        assertEquals(0, indexedString.indexOf(target1));
        
        char[] target2 = {'b'};
        assertEquals(1, indexedString.indexOf(target2));
        
        char[] target3 = {'c'};
        assertEquals(2, indexedString.indexOf(target3));
        
        char[] target4 = {'a', 'b'};
        assertEquals(0, indexedString.indexOf(target4));
        
        char[] target5 = {'b', 'c'};
        assertEquals(1, indexedString.indexOf(target5));
        
        char[] target6 = {'c', 'a'};
        assertEquals(-1, indexedString.indexOf(target6));
        
        char[] target7 = {'d'};
        assertEquals(-1, indexedString.indexOf(target7));
        
    }
}
