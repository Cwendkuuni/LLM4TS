package gnu.trove.impl.hash;

import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();

        // Test when hash is empty
        assertFalse(hash.contains('a'));

        // Test when hash contains the value
        hash.insertKey('a');
        assertTrue(hash.contains('a'));

        // Test when hash does not contain the value
        assertFalse(hash.contains('b'));

    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();

        // Test when hash contains some values
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');

        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));

        // Test when procedure returns false
        TCharProcedure procedure2 = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return false;
            }
        };

        assertFalse(hash.forEach(procedure2));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();

        // Test when hash is empty
        hash.removeAt(0);

        // Test when hash contains values
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');

        hash.removeAt(1);

        // Test if value is removed
        assertFalse(hash.contains('b'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();

        // Test when value is not found
        assertEquals(-1, hash.index('a'));

        // Test when value is found
        hash.insertKey('a');
        assertEquals(0, hash.index('a'));

        // Test when multiple values have the same hash
        hash.insertKey('\u0000');
        hash.insertKey('\u0000');
        assertEquals(0, hash.index('\u0000'));

        // Test when value is hashed to a different index
        hash.insertKey('b');
        assertEquals(1, hash.index('b'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();

        // Test when hash is empty
        assertEquals(0, hash.insertKey('a'));

        // Test when value is already in the hash
        assertEquals(-1, hash.insertKey('a'));

        // Test when hash is full
        hash.insertKey('b');
        hash.insertKey('c');
        hash.insertKey('d');
        hash.insertKey('e');
        hash.insertKey('f');
        hash.insertKey('g');
        hash.insertKey('h');
        hash.insertKey('i');
        hash.insertKey('j');
        hash.insertKey('k');
        hash.insertKey('l');
        hash.insertKey('m');
        hash.insertKey('n');
        hash.insertKey('o');
        hash.insertKey('p');
        hash.insertKey('q');
        hash.insertKey('r');
        hash.insertKey('s');
        hash.insertKey('t');
        hash.insertKey('u');
        hash.insertKey('v');
        hash.insertKey('w');
        hash.insertKey('x');
        hash.insertKey('y');
        hash.insertKey('z');

        try {
            hash.insertKey('a');
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

}