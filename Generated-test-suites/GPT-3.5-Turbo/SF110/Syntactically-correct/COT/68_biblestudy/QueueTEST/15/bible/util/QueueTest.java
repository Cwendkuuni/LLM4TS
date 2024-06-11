import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

    // Test isEmpty()
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }

    // Test enqueue()
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
    }

    // Test dequeue()
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());

        queue.enqueue("Item 1");
        assertEquals("Item 1", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    // Test remove()
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("Item 1"));

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 1");
        assertEquals(2, queue.remove("Item 1"));
        assertEquals(1, queue.getNumberItems());
    }

    // Test refreshElement()
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        queue.refreshElement("Item 2");
        assertEquals("Item 2", queue.dequeue());
    }

    // Test getNumberItems()
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
    }

    // Test getPeakNumberItems()
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    // Test getObjects()
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        assertTrue(queue.getObjects().isEmpty());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals(2, queue.getObjects().size());
    }

    // Test maxCapacityExceeded()
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue();
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertTrue(queue.maxCapacityExceeded());
    }

    // Test toString()
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item 1, Item 2]\r\n]", queue.toString());
    }
}