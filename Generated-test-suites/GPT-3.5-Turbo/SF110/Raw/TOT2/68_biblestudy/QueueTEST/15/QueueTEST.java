import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {
    
    Queue queue;
    
    @Test
    public void testIsEmpty() {
        queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        queue = new Queue();
        Object obj = "test";
        queue.enqueue(obj);
        assertEquals(obj, queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue = new Queue();
        Object obj1 = "test1";
        Object obj2 = "test2";
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(1, queue.remove(obj1));
        assertEquals(0, queue.remove(obj2));
    }
    
    @Test
    public void testPeakNumberItems() {
        queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testRefreshElement() {
        queue = new Queue();
        queue.enqueue("test1");
        queue.refreshElement("test2");
        assertEquals(1, queue.getNumberItems());
    }
}