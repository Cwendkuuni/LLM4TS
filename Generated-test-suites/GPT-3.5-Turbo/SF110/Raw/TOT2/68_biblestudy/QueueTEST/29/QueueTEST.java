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
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Hello");
        assertEquals(1, queue.getNumberItems());
        assertEquals("Hello", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("A");
        assertEquals(2, queue.remove("A"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.refreshElement("A");
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.dequeue();
        queue.enqueue("C");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("A");
        queue.enqueue("B");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("A");
        queue.enqueue("B");
        assertTrue(queue.maxCapacityExceeded());
    }

}