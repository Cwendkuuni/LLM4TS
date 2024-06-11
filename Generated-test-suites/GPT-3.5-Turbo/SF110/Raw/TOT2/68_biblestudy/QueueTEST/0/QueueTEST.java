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
        queue.enqueue("test object");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("test object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("test object");
        assertEquals("test object", queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("test object");
        queue.enqueue("test object");
        assertEquals(2, queue.remove("test object"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("test object");
        queue.refreshElement("test object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue("test object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("test object");
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("test object");
        assertEquals("test object", queue.getObjects().get(0));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithMaxCap = new Queue(1);
        queueWithMaxCap.enqueue("test object 1");
        assertTrue(queueWithMaxCap.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("test object");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[test object]\r\n]", queue.toString());
    }

}