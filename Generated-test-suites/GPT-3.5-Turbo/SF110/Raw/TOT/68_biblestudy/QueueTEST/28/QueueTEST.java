import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Test"));
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test");
        assertEquals(2, queue.remove("Test"));
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        queue.refreshElement("Test2");
        assertEquals(1, queue.remove("Test2"));
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Test");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertNull(queue.getObjects());
        queue.enqueue("Test");
        assertNotNull(queue.getObjects());
        assertEquals(1, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(2);
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
        queue.enqueue("Test3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("Test");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test]\r\n]", queue.toString());
        queue.enqueue("Test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test, Test2]\r\n]", queue.toString());
    }
}