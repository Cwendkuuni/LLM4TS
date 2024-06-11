package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TCharHashTest {
    // Test contains method
    @Test
    public void testContains() {
        TCharHash testObj = new TCharHash();
        testObj.insertKey('a');
        testObj.insertKey('b');
        testObj.insertKey('c');

        assertTrue(testObj.contains('a'));
        assertTrue(testObj.contains('b'));
        assertTrue(testObj.contains('c'));
        assertFalse(testObj.contains('d'));
        assertFalse(testObj.contains('e'));
        assertFalse(testObj.contains('f'));
    }
    
    // Test forEach method
    @Test
    public void testForEach() {
        TCharHash testObj = new TCharHash();
        testObj.insertKey('a');
        testObj.insertKey('b');
        testObj.insertKey('c');

        StringBuilder result = new StringBuilder();
        testObj.forEach(val -> {
            result.append(val);
            return true;
        });

        assertEquals("abc", result.toString());
    }

    // Test removeAt method
    @Test
    public void testRemoveAt() {
        TCharHash testObj = new TCharHash();
        testObj.insertKey('a');
        testObj.insertKey('b');
        testObj.insertKey('c');

        assertEquals(3, testObj.size());

        testObj.removeAt(1);

        assertEquals(2, testObj.size());
        assertTrue(testObj.contains('a'));
        assertFalse(testObj.contains('b'));
        assertTrue(testObj.contains('c'));
    }

    // Test index method
    @Test
    public void testIndex() {
        TCharHash testObj = new TCharHash();
        testObj.insertKey('a');
        testObj.insertKey('b');
        testObj.insertKey('c');

        assertEquals(0, testObj.index('a'));
        assertEquals(1, testObj.index('b'));
        assertEquals(2, testObj.index('c'));
        assertEquals(-1, testObj.index('d'));
        assertEquals(-1, testObj.index('e'));
        assertEquals(-1, testObj.index('f'));
    }

    // Test insertKey method
    @Test
    public void testInsertKey() {
        TCharHash testObj = new TCharHash();
        testObj.insertKey('a');
        testObj.insertKey('b');
        testObj.insertKey('c');

        assertTrue(testObj.contains('a'));
        assertTrue(testObj.contains('b'));
        assertTrue(testObj.contains('c'));
    }
}