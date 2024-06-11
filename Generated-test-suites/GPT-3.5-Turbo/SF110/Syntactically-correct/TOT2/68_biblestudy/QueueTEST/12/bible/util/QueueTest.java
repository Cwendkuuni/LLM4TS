package bible.util;
import static org.junit.Assert.*;
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
        Object obj = new Object();
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        assertEquals(obj, dequeuedObj);
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        int removeCount = queue.remove(obj);
        assertEquals(1, removeCount);
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        queue.refreshElement(obj);
        assertTrue(queue.getObjects().contains(obj));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        queue.enqueue(obj2);
        assertFalse(queue.maxCapacityExceeded());
    }

}