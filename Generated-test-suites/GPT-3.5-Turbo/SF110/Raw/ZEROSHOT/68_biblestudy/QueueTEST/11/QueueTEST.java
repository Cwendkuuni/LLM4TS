import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Item 2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Item 1");
        assertEquals("Item 1", queue.dequeue());
        assertTrue(queue.isEmpty());
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        assertEquals("Item 2", queue.dequeue());
        assertEquals("Item 3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item 1"));
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 1");
        assertEquals(2, queue.remove("Item 1"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        queue.refreshElement("Item 2");
        assertEquals("Item 2", queue.dequeue());
        assertEquals("Item 1", queue.dequeue());
        assertEquals("Item 3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Item 2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Item 1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        assertEquals(3, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(3, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertNotNull(queue.getObjects());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        assertEquals(3, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("Item 1"));
        assertTrue(queue.getObjects().contains("Item 2"));
        assertTrue(queue.getObjects().contains("Item 3"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Item 1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Item 2");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Item 3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item 1, Item 2]\r\n]", queue.toString());
    }
}