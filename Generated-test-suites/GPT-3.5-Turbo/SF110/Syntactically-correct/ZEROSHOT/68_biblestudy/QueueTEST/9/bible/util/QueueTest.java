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
        queue.enqueue(10);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        assertNull(queue.dequeue());
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertEquals(10, queue.dequeue());
        assertEquals(20, queue.dequeue());
        assertEquals(30, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(10, queue.dequeue());
        assertFalse(queue.isEmpty());
        assertEquals(20, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove(10));
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(1, queue.remove(10));
        assertEquals(20, queue.dequeue());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.refreshElement(20);
        assertEquals(20, queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(2, queue.getNumberItems());
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue(10);
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue(20);
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        Vector expected = new Vector();
        expected.add(10);
        expected.add(20);
        expected.add(30);
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[10, 20, 30]\r\n]";
        assertEquals(expected, queue.toString());
    }
}