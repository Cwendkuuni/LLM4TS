package bible.util;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue(5);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("a");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("b");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("b", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        queue.enqueue("a");
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals(2, queue.remove("a"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.refreshElement("a");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.getObjects().get(0));
        assertEquals("b", queue.getObjects().get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.enqueue("d");
        queue.enqueue("e");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]\r\n]", queue.toString());
    }
}