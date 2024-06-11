import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Object");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Object");
        Object obj = queue.dequeue();
        assertEquals("Object", obj);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Object1");
        queue.enqueue("Object2");
        int count = queue.remove("Object1");
        assertEquals(1, count);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Object1");
        queue.enqueue("Object2");
        queue.refreshElement("Object1");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Object");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Object1");
        queue.enqueue("Object2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Object1");
        queue.enqueue("Object2");
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertTrue(objects.contains("Object1"));
        assertTrue(objects.contains("Object2"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Object1");
        queue.enqueue("Object2");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Object3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Object");
        String expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Object]\r\n]";
        assertEquals(expected, queue.toString());
    }
}