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
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
        assertEquals("Test", queue.getObjects().get(0));
        assertEquals("Test2", queue.getObjects().get(1));
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertEquals("Test", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Test"));
        queue.enqueue("Test");
        assertEquals(1, queue.remove("Test"));
        assertEquals(0, queue.remove("Test"));
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertEquals(2, queue.remove("Test"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.refreshElement("Test");
        assertEquals("Test2", queue.getObjects().get(0));
        assertEquals("Test", queue.getObjects().get(1));
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
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
        queue.enqueue("Test");
        queue.enqueue("Test2");
        assertEquals(2, queue.getObjects().size());
        assertEquals("Test", queue.getObjects().get(0));
        assertEquals("Test2", queue.getObjects().get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("Test");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test]\r\n]", queue.toString());
    }
}