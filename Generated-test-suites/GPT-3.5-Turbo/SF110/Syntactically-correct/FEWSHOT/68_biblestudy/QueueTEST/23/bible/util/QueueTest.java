package bible.util;

import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();

        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(null);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();

        assertNull(queue.dequeue());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());

        assertEquals("item2", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();

        assertEquals(0, queue.remove("item"));

        queue.enqueue("item1");
        queue.enqueue("item");
        queue.enqueue("item2");
        queue.enqueue("item");

        assertEquals(2, queue.remove("item"));
        assertEquals(2, queue.getNumberItems());

        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());

        assertEquals(0, queue.remove("nonexistent"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();

        queue.refreshElement("item");

        queue.enqueue("item1");
        queue.enqueue("item");

        assertEquals(2, queue.getNumberItems());

        queue.refreshElement("item");

        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals("item", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();

        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
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
        queue.enqueue("item2");

        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();

        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();

        Vector vector = new Vector();

        assertEquals(vector, queue.getObjects());

        queue.enqueue("item1");
        queue.enqueue("item2");

        vector.addElement("item1");
        vector.addElement("item2");

        assertEquals(vector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue();

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(2);

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("item3");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();

        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}