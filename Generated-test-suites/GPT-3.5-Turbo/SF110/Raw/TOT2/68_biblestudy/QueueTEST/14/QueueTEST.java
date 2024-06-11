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
    public void testEnqueueAndDequeue() {
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("Test");
        queue.enqueue("Test");
        queue.enqueue("Test2");
        int removedCount = queue.remove("Test");
        assertEquals(2, removedCount);
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        queue.refreshElement("Test");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("Test");
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithCapacity = new Queue(2);
        queueWithCapacity.enqueue("Test1");
        queueWithCapacity.enqueue("Test2");
        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("Test");
        assertNotNull(queue.toString());
    }
}