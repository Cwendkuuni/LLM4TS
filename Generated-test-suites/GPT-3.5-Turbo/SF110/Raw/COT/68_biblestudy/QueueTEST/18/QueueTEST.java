
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        assertEquals("item1", queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(1, queue.remove("item1"));
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.remove("item2"));
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals("item2", queue.dequeue());
        assertEquals("item1", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
        assertEquals("item3", objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    }

}
