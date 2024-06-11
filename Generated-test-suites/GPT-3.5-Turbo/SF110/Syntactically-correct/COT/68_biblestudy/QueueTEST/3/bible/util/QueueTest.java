import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("test2");
        assertEquals(2, queue.getNumberItems());
        
        queue.enqueue(null);
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());

        queue.enqueue("test");
        assertEquals("test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("test"));

        queue.enqueue("test");
        assertEquals(1, queue.remove("test"));
        assertTrue(queue.isEmpty());

        queue.enqueue("test");
        queue.enqueue("test");
        assertEquals(2, queue.remove("test"));
        assertTrue(queue.isEmpty());

        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test1");
        assertEquals(2, queue.remove("test1"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.refreshElement("test1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("test2");
        queue.enqueue("test1");
        queue.refreshElement("test1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("test1", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("test1");
        assertEquals(1, queue.getNumberItems());

        queue.enqueue("test2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("test1");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("test2");
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        assertEquals(0, queue.getObjects().size());

        queue.enqueue("test1");
        assertEquals(1, queue.getObjects().size());

        queue.enqueue("test2");
        assertEquals(2, queue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("test1");
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]", queue.toString());
    }
}