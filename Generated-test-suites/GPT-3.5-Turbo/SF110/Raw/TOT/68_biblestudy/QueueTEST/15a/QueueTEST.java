import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    // Test case for isEmpty method
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("First Element");
        assertFalse(queue.isEmpty());
    }

    // Test case for enqueue method
    @Test
    public void testEnqueue() {
        Queue queue = new Queue(5);
        queue.enqueue("Element 1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Element 2");
        assertEquals(2, queue.getNumberItems());
        queue.enqueue("Element 3");
        assertEquals(3, queue.getNumberItems());
        queue.enqueue("Element 4");
        assertEquals(4, queue.getNumberItems());
        queue.enqueue("Element 5");
        assertEquals(5, queue.getNumberItems());

        // Edge case: Adding an element to a full queue
        queue.enqueue("Element 6");
        assertEquals(5, queue.getNumberItems());
        assertTrue(queue.maxCapacityExceeded());
    }

    // Test case for dequeue method
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");

        // Typical use case
        assertEquals("Element 1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());

        // Empty queue case
        assertNull(queue.dequeue());
    }

    // Test case for remove method
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");

        // Typical use case
        assertEquals(2, queue.remove("Element 2"));
        assertEquals(2, queue.getNumberItems());

        // Removing non-existing element
        assertEquals(0, queue.remove("Not in queue"));
        assertEquals(2, queue.getNumberItems());
    }

    // Test case for refreshElement method
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");

        // Typical use case
        queue.refreshElement("Element 2");
        assertEquals(3, queue.getNumberItems());
        assertEquals("Element 2", queue.dequeue());

        // Refreshing non-existing element
        queue.refreshElement("Element 4");
        assertEquals(2, queue.getNumberItems());
    }

    // Test case for getPeakNumberItems method
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Element 1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Element 2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
        queue.enqueue("Element 3");
        assertEquals(3, queue.getPeakNumberItems());
    }

    // Test case for getObjects method
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");

        // Typical use case
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("Element 1", objects.get(0));
        assertEquals("Element 2", objects.get(1));
        assertEquals("Element 3", objects.get(2));
    }

    // Test case for maxCapacityExceeded method
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        assertTrue(queue.maxCapacityExceeded());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");

        // Typical use case
        String expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Element 1, Element 2, Element 3\r\n]]";
        assertEquals(expected, queue.toString());
    }

}