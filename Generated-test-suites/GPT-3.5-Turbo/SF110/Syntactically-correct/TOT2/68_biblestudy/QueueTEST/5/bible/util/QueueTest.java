package bible.util;
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
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("Test1");
        assertEquals("Test1", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.remove("Test1"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test1");
        queue.refreshElement("Test1");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue("Test1");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("Test1");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test1]\r\n]", queue.toString());
    }
}