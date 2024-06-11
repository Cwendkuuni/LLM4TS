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
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item 2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue(null);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals("Item 1", queue.dequeue());
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());

        assertEquals("Item 2", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove(null));

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 1");

        assertEquals(2, queue.remove("Item 1"));
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());

        assertEquals(1, queue.remove("Item 2"));
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.refreshElement("Item 1");
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        queue.refreshElement("Item 1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("Item 1", queue.dequeue());
        assertEquals("Item 2", queue.dequeue());

        queue.refreshElement("Item 2");
        assertEquals(2, queue.getNumberItems());
        assertEquals("Item 2", queue.dequeue());
        assertEquals("Item 1", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Item 1");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("Item 2");
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertEquals(0, queue.getObjects().size());

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
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(2);
        queue.enqueue("Item 1");
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
        queue.enqueue("Item 3");

        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 1, Item 2, Item 3]\r\n]", queue.toString());
    }
}