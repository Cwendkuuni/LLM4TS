package bible.util;

import static org.junit.Assert.*;
import java.util.Vector;
import org.junit.Test;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("foo");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("bar");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        queue.enqueue("foo");
        assertEquals("foo", queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("foo"));
        queue.enqueue("foo");
        assertEquals(1, queue.remove("foo"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        assertEquals(0, queue.getNumberItems());
        queue.refreshElement("foo");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("foo");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("foo");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("bar");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Vector<Object> expected = new Vector<>();
        assertEquals(expected, queue.getObjects());
        queue.enqueue("foo");
        expected.add("foo");
        assertEquals(expected, queue.getObjects());
        queue.enqueue("bar");
        expected.add("bar");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]", queue.toString());
        queue.enqueue("foo");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=foo\r\n]", queue.toString());
        queue.enqueue("bar");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=foo, bar\r\n]", queue.toString());
    }
}