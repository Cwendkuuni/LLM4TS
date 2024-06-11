package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("a");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("a");
        assertEquals(1, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("a"));
        queue.enqueue("b");
        assertEquals(2, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("b"));
        queue.enqueue(null);
        assertEquals(2, queue.getNumberItems());
        assertFalse(queue.getObjects().contains(null));
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.dequeue());
        assertEquals("b", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("a"));
        queue.enqueue("a");
        assertEquals(1, queue.remove("a"));
        assertFalse(queue.getObjects().contains("a"));
        queue.enqueue("a");
        queue.enqueue("a");
        assertEquals(2, queue.remove("a"));
        assertFalse(queue.getObjects().contains("a"));
        queue.enqueue("b");
        queue.enqueue("c");
        queue.enqueue("d");
        assertEquals(0, queue.remove("e"));
        assertEquals(1, queue.remove("b"));
        assertFalse(queue.getObjects().contains("b"));
        assertEquals(2, queue.remove("c"));
        assertFalse(queue.getObjects().contains("c"));
        assertEquals(1, queue.remove("d"));
        assertFalse(queue.getObjects().contains("d"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.refreshElement("b");
        assertTrue(queue.getObjects().get(queue.getNumberItems() - 1).equals("b"));
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        queue.refreshElement("d");
        assertTrue(queue.getObjects().get(queue.getNumberItems() - 1).equals("d"));
        assertEquals(4, queue.getNumberItems());
        assertEquals(4, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals(2, queue.getNumberItems());
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector vector = queue.getObjects();
        assertTrue(vector.isEmpty());
        queue.enqueue("a");
        queue.enqueue("b");
        vector = queue.getObjects();
        assertEquals(2, vector.size());
        assertTrue(vector.contains("a"));
        assertTrue(vector.contains("b"));
        queue.enqueue("c");
        vector = queue.getObjects();
        assertEquals(3, vector.size());
        assertTrue(vector.contains("a"));
        assertTrue(vector.contains("b"));
        assertTrue(vector.contains("c"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(2);
        queue.enqueue("a");
        queue.enqueue("b");
        assertTrue(queue.maxCapacityExceeded());
        queue.enqueue("c");
        assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("a");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[a]\r\n]", queue.toString());
        queue.enqueue("b");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[a, b]\r\n]", queue.toString());
    }
}