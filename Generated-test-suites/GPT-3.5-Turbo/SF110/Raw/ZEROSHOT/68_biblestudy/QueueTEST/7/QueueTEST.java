import bible.util.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Test"));
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(2, queue.remove("Test"));
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.remove("Test2"));
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        queue.refreshElement("Test");
        assertEquals(1, queue.getNumberItems());
        assertEquals("Test", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
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
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertEquals(0, queue.getObjects().size());
        queue.enqueue("Test");
        assertEquals(1, queue.getObjects().size());
        assertEquals("Test", queue.getObjects().get(0));
        queue.enqueue("Test2");
        assertEquals(2, queue.getObjects().size());
        assertEquals("Test", queue.getObjects().get(0));
        assertEquals("Test2", queue.getObjects().get(1));
        queue.dequeue();
        assertEquals(1, queue.getObjects().size());
        assertEquals("Test2", queue.getObjects().get(0));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        Queue queueWithCapacity = new Queue(2);
        queueWithCapacity.enqueue("Test");
        assertFalse(queueWithCapacity.maxCapacityExceeded());
        queueWithCapacity.enqueue("Test2");
        assertTrue(queueWithCapacity.maxCapacityExceeded());
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