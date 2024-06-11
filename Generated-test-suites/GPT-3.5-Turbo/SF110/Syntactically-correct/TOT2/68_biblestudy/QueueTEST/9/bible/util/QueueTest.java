package bible.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
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
        queue.enqueue("Test");
        assertEquals(1, queue.remove("Test"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.refreshElement("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
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
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
}