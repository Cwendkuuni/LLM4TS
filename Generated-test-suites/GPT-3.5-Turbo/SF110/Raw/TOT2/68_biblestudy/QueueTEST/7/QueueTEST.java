import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {
    
    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        assertNotNull(queue);
    }
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(1, queue.remove("Test1"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.refreshElement("Test1");
        assertEquals("Test1", queue.dequeue());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getObjects().size());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("Test1");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertNotNull(queue.toString());
    }
}