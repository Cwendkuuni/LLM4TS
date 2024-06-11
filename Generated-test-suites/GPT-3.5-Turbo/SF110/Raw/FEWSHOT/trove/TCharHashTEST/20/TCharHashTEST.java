package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class TCharHashTest {
    @Test
    public void testConstructor() {
        TCharHash t1 = new TCharHash();
        assertEquals('\u0000', t1.getNoEntryValue());
        
        TCharHash t2 = new TCharHash(10);
        assertEquals('\u0000', t2.getNoEntryValue());
        
        TCharHash t3 = new TCharHash(10, 0.5f);
        assertEquals('\u0000', t3.getNoEntryValue());
        
        TCharHash t4 = new TCharHash(10, 0.5f, 'x');
        assertEquals('x', t4.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash t1 = new TCharHash();
        int capacity = t1.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, t1._set.length);
        
        TCharHash t2 = new TCharHash(10);
        capacity = t2.setUp(15);
        assertEquals(15, capacity);
        assertEquals(15, t2._set.length);
    }
    
    @Test
    public void testContains() {
        TCharHash t1 = new TCharHash();
        t1._set = new char[] {'a', 'b', 'c', 'd', 'e'};
        t1._states = new byte[] {1, 1, 1, 1, 1};
        assertTrue(t1.contains('a'));
        assertTrue(t1.contains('b'));
        
        TCharHash t2 = new TCharHash();
        t2._set = new char[] {'a', 'b', 'c', 'd', 'e'};
        t2._states = new byte[] {0, 0, 0, 0, 0};
        assertFalse(t2.contains('a'));
        assertFalse(t2.contains('f'));
    }
    
    @Test
    public void testForEach() {
        TCharHash t1 = new TCharHash();
        t1._set = new char[] {'a', 'b', 'c', 'd', 'e'};
        t1._states = new byte[] {1, 1, 1, 1, 1};
        
        final List<Character> values = new ArrayList<>();
        
        assertTrue(t1.forEach((val) -> {
            values.add(val);
            return true;
        }));
        
        assertEquals(5, values.size());
        assertTrue(values.contains('a'));
        assertTrue(values.contains('b'));
        assertTrue(values.contains('c'));
        assertTrue(values.contains('d'));
        assertTrue(values.contains('e'));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash t1 = new TCharHash();
        t1._set = new char[] {'a', 'b', 'c', 'd', 'e'};
        t1._states = new byte[] {1, 1, 1, 1, 1};
        
        t1.removeAt(2);
        assertEquals('\u0000', t1._set[2]);
        assertEquals(0, t1._states[2]);
    }
    
    @Test
    public void testIndex() {
        TCharHash t1 = new TCharHash();
        t1._set = new char[] {'a', 'b', 'c', 'd', 'e'};
        t1._states = new byte[] {1, 1, 1, 1, 1};
        
        int index = t1.index('a');
        assertEquals(0, index);
        
        index = t1.index('f');
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash t1 = new TCharHash();
        t1._set = new char[10];
        t1._states = new byte[10];
        
        int index = t1.insertKey('a');
        assertEquals(0, index);
        
        index = t1.insertKey('b');
        assertEquals(1, index);
        
        index = t1.insertKey('c');
        assertEquals(2, index);
        
        index = t1.insertKey('a');
        assertEquals(-1, index);
    }
}