import static org.junit.Assert.*;
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
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("test");
        assertEquals("test", queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("test");
        assertEquals(1, queue.remove("test"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("test");
        queue.refreshElement("test");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue("test");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("test");
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("test");
        assertTrue(queue.getObjects().contains("test"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());
    }
}