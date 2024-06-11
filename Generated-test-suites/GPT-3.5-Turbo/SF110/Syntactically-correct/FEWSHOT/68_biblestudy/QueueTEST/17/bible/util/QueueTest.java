package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(2, queue.getNumberItems());
        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(2, queue.getNumberItems());
        
        int removedCount = queue.remove(obj1);
        assertEquals(1, removedCount);
        assertEquals(1, queue.getNumberItems());
        
        removedCount = queue.remove(obj2);
        assertEquals(1, removedCount);
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement(obj1);
        assertEquals(2, queue.getNumberItems());
        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertTrue(objects.contains(obj1));
        assertTrue(objects.contains(obj2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue(obj2);
        assertTrue(queue.maxCapacityExceeded());
    }
}