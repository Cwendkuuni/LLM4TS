package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        Object dequeuedObject = queue.dequeue();
        assertEquals("Test Object", dequeuedObject);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        assertEquals(1, queue.remove("Test Object"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        queue.refreshElement("Test Object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertTrue(queue.maxCapacityExceeded());
    }

    // Add more test cases for edge cases and error scenarios as needed
    
}