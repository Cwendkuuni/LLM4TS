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
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }

    @Test
    public void testDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");

        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        int removedCount = queue.remove("item1");

        assertEquals(2, removedCount);
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        assertEquals("item2", objects.get(0));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        queue.refreshElement("item1");

        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        assertEquals("item2", objects.get(0));
        assertEquals("item1", objects.get(1));
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        Vector objects = queue.getObjects();
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        queue.enqueue("item1");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }

}