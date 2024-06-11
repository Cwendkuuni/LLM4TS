import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue(5);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue("Item 1");

        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals(2, queue.getNumberItems());
        assertEquals("Item 1", queue.getObjects().elementAt(0));
        assertEquals("Item 2", queue.getObjects().elementAt(1));

        queue.enqueue(null);

        assertEquals(2, queue.getNumberItems());
        assertEquals("Item 1", queue.getObjects().elementAt(0));
        assertEquals("Item 2", queue.getObjects().elementAt(1));
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals("Item 1", queue.dequeue());

        assertEquals(1, queue.getNumberItems());
        assertEquals("Item 2", queue.getObjects().elementAt(0));
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item 1"));

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 1");

        assertEquals(2, queue.remove("Item 1"));
        assertEquals(1, queue.getNumberItems());
        assertEquals("Item 2", queue.getObjects().elementAt(0));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        queue.refreshElement("Item 1");

        assertEquals(2, queue.getNumberItems());
        assertEquals("Item 2", queue.getObjects().elementAt(0));
        assertEquals("Item 1", queue.getObjects().elementAt(1));
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue("Item 3");
        queue.enqueue("Item 4");
        queue.enqueue("Item 5");
        queue.enqueue("Item 6");

        assertEquals(6, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        queue.enqueue("Item 4");
        queue.enqueue("Item 5");

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item 6");

        assertTrue(queue.maxCapacityExceeded());

        queue = new Queue();

        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals("Queue:[numItems=2, maxNumItems=0, maxCapacity=5, getObjects()=[Item 1, Item 2]\r\n]", queue.toString());
    }
}