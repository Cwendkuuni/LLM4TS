import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test");
        queue.enqueue("Test");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.remove("Test"));
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        queue.refreshElement("Test2");
        assertEquals(3, queue.getNumberItems());
        assertEquals(4, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        queue.enqueue("Test");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Test");
        queue.enqueue("Test");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals("[Test1, Test2, Test3]", queue.getObjects().toString());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        queue.enqueue("Test4");
        queue.enqueue("Test5");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]", queue.toString());
    }
}