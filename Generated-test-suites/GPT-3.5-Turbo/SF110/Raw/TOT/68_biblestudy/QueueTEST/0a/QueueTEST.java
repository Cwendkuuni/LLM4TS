import org.junit.*;
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
        queue.enqueue("item1");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item1"));

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item1");

        assertEquals(2, queue.remove("item1"));
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());

        queue.refreshElement("item3");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());

        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertEquals(0, queue.getObjects().size());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals(3, queue.getObjects().size());
        assertEquals("item1", queue.getObjects().get(0));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }

}