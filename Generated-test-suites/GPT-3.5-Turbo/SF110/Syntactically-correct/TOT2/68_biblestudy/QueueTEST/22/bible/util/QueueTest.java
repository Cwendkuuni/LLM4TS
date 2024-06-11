package bible.util;
import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Test Object");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Test Object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("Test Object");
        Object removedObject = queue.dequeue();
        assertEquals("Test Object", removedObject);
    }

    @Test
    public void testRemove() {
        queue.enqueue("Test Object");
        assertEquals(1, queue.remove("Test Object"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test Object");
        queue.refreshElement("Test Object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue("Test Object 1");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[Test Object]\r\n]";
        queue.enqueue("Test Object");
        assertEquals(expected, queue.toString());
    }
}