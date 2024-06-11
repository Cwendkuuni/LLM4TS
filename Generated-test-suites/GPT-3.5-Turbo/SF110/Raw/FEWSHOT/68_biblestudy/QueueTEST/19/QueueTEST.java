package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item");

        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("item");
        assertEquals("item", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item"));

        queue.enqueue("item");
        assertEquals(1, queue.remove("item"));
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.remove("item1"));
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.refreshElement("item");
        assertTrue(queue.isEmpty());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item");
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
    }

    @Test
    public void testGetObjects() {
        Vector vector = new Vector();
        assertEquals(vector, queue.getObjects());

        queue.enqueue("item");
        vector.addElement("item");
        assertEquals(vector, queue.getObjects());

        queue.enqueue("item2");
        vector.addElement("item2");
        assertEquals(vector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());

        queue.enqueue("item");
        expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item]\r\n]";
        assertEquals(expected, queue.toString());
    }
}