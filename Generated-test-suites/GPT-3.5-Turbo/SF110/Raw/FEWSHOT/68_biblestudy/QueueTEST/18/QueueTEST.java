package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    private Queue queue;

    @Before
    public void setup() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue("Item");
        
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals("Item 1", queue.dequeue());
        assertEquals("Item 2", queue.dequeue());
        assertEquals("Item 3", queue.dequeue());
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item"));

        queue.enqueue("Item 1");
        queue.enqueue("Item");
        queue.enqueue("Item 2");
        queue.enqueue("Item");

        assertEquals(2, queue.remove("Item"));
        assertNull(queue.dequeue());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        queue.refreshElement("Item 2");

        assertEquals("Item 2", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue("Item 3");

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        assertEquals(0, queue.getObjects().size());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("Item 1"));
        assertTrue(queue.getObjects().contains("Item 2"));
        assertTrue(queue.getObjects().contains("Item 3"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(2);

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item 3");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals("bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 1, Item 2, Item 3]\r\n]", queue.toString());
    }
}