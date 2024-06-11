import static org.junit.Assert.*;
import org.junit.Test;

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
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        Object obj = queue.dequeue();
        assertEquals("Test Object", obj);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        int removed = queue.remove("Test Object");
        assertEquals(1, removed);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        queue.refreshElement("Test Object");
        assertFalse(queue.isEmpty());
    }

    // Additional test cases for edge cases and error scenarios

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("Test Object 1");
        assertTrue(queue.maxCapacityExceeded());
        queue.enqueue("Test Object 2");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test Object 1, Test Object 2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}