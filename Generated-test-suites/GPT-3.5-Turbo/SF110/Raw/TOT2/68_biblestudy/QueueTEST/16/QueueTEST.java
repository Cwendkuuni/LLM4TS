import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @After
    public void tearDown() {
        queue = null;
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        queue.enqueue("Test");
        assertEquals(1, queue.remove("Test"));
        assertTrue(queue.isEmpty());
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
        queue.enqueue("Test");
        assertEquals(1, queue.getObjects().size());
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

    @Test
    public void testEmptyQueueDequeue() {
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemoveNonExistingObject() {
        queue.enqueue("Test");
        assertEquals(0, queue.remove("NotTest"));
    }
}