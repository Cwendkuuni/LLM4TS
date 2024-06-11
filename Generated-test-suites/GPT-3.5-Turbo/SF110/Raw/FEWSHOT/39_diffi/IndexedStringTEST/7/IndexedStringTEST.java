package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("abcdefg");
        assertEquals(1, indexedString.count('a'));
        assertEquals(1, indexedString.count('b'));
        assertEquals(1, indexedString.count('c'));
        assertEquals(1, indexedString.count('d'));
        assertEquals(1, indexedString.count('e'));
        assertEquals(1, indexedString.count('f'));
        assertEquals(1, indexedString.count('g'));

        indexedString = new IndexedString("abcabc");
        assertEquals(2, indexedString.count('a'));
        assertEquals(2, indexedString.count('b'));
        assertEquals(2, indexedString.count('c'));
        assertEquals(0, indexedString.count('d'));
        assertEquals(0, indexedString.count('e'));
        assertEquals(0, indexedString.count('f'));
        assertEquals(0, indexedString.count('g'));
    }

    @Test
    public void testConstructor() {
        IndexedString indexedString = new IndexedString("abcdefg");
        assertEquals('a', indexedString.values[0]);
        assertEquals('b', indexedString.values[1]);
        assertEquals('c', indexedString.values[2]);
        assertEquals('d', indexedString.values[3]);
        assertEquals('e', indexedString.values[4]);
        assertEquals('f', indexedString.values[5]);
        assertEquals('g', indexedString.values[6]);

        assertEquals(7, indexedString.index.length);

        assertEquals(1, indexedString.index['a'][0]);
        assertEquals(1, indexedString.index['b'][0]);
        assertEquals(1, indexedString.index['c'][0]);
        assertEquals(1, indexedString.index['d'][0]);
        assertEquals(1, indexedString.index['e'][0]);
        assertEquals(1, indexedString.index['f'][0]);
        assertEquals(1, indexedString.index['g'][0]);

        indexedString = new IndexedString("abcabc");
        assertEquals(2, indexedString.index['a'].length);
        assertEquals(2, indexedString.index['b'].length);
        assertEquals(2, indexedString.index['c'].length);

        assertEquals(0, indexedString.index['d'].length);
        assertEquals(0, indexedString.index['e'].length);
        assertEquals(0, indexedString.index['f'].length);
        assertEquals(0, indexedString.index['g'].length);
    }

    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcdefg");
        assertEquals(0, indexedString.indexOf(new char[] { 'a' }));
        assertEquals(1, indexedString.indexOf(new char[] { 'b' }));
        assertEquals(2, indexedString.indexOf(new char[] { 'c' }));
        assertEquals(3, indexedString.indexOf(new char[] { 'd' }));
        assertEquals(4, indexedString.indexOf(new char[] { 'e' }));
        assertEquals(5, indexedString.indexOf(new char[] { 'f' }));
        assertEquals(6, indexedString.indexOf(new char[] { 'g' }));

        assertEquals(0, indexedString.indexOf(new char[] { 'a', 'b' }));
        assertEquals(1, indexedString.indexOf(new char[] { 'b', 'c' }));
        assertEquals(5, indexedString.indexOf(new char[] { 'f', 'g' }));

        assertEquals(-1, indexedString.indexOf(new char[] { 'd', 'e' }));
        assertEquals(-1, indexedString.indexOf(new char[] { 'b', 'a' }));
        assertEquals(-1, indexedString.indexOf(new char[] { 'h' }));
    }

}