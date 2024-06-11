package bible.util;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("object");
        assertEquals("object", queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("object");
        assertEquals(1, queue.remove("object"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.refreshElement("object1");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue("object1");
        queue.enqueue("object2");
    }

    @Test
    public void testToString() {
        queue.enqueue("object1");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[object1]\r\n]", queue.toString());
    }

}