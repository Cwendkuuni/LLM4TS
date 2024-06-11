package bible.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        Object item3 = "item3";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        queue.enqueue(item3);
        
        assertEquals(item1, queue.dequeue());
        assertEquals(item2, queue.dequeue());
        assertEquals(item3, queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        Object item3 = "item3";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        queue.enqueue(item3);
        
        assertEquals(2, queue.remove(item1));
        assertEquals(1, queue.remove(item2));
        assertEquals(0, queue.remove(item3));
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        
        queue.refreshElement(item1);
        queue.dequeue();
        
        assertFalse(queue.getObjects().contains(item1));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Object item1 = "item1";
        Object item2 = "item2";
        
        queue.enqueue(item1);
        queue.enqueue(item2);
        
        Vector expected = new Vector();
        expected.addElement(item1);
        expected.addElement(item2);
        
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        Object item1 = "item1";
        Object item2 = "item2";
        
        queue.enqueue(item1);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue(item2);
        assertTrue(queue.maxCapacityExceeded());
    }
}