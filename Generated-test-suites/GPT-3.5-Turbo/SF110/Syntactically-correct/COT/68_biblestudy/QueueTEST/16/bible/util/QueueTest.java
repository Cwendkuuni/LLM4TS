
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
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());

        queue.enqueue(null);
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());

        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item"));

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");

        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.remove("item2"));

        assertEquals(0, queue.remove("item"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        queue.refreshElement("item1");
        
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());

        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));

        queue.dequeue();
        objects = queue.getObjects();
        assertEquals(1, objects.size());
        assertEquals("item2", objects.get(0));

        queue.dequeue();
        objects = queue.getObjects();
        assertEquals(0, objects.size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        queue.enqueue("item1");
        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        String expected = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[" + "item1" + ", " + "item2" + "]\r\n]";
        assertEquals(expected, queue.toString());
    }
}
