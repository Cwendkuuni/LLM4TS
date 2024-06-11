
package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Item");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertEquals("Item1", queue.dequeue());
        assertEquals("Item2", queue.dequeue());
        
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals(2, queue.remove("Item1"));
        assertFalse(queue.isEmpty());
        
        assertEquals(1, queue.remove("Item2"));
        assertEquals(0, queue.remove("Item3"));
        
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        queue.refreshElement("Item1");
        assertEquals("Item2", queue.dequeue());
        assertEquals("Item1", queue.dequeue());
        
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Item2");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        Vector vector = new Vector();
        vector.addElement("Item1");
        vector.addElement("Item2");
        vector.addElement("Item3");
        
        assertEquals(vector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}
