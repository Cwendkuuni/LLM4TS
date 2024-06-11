package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test1");
        assertEquals(2, queue.remove("Test1"));
        assertEquals(1, queue.getNumberItems());
        assertEquals("Test2", queue.dequeue());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.refreshElement("Test1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("Test2", queue.dequeue());
        assertEquals("Test1", queue.dequeue());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}