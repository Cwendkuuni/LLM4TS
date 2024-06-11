package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {
    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("item1");
        assertTrue(!queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals("item1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());

        assertEquals("item2", queue.dequeue());
        assertEquals(1, queue.getNumberItems());

        assertEquals("item3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item1"));

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item1");

        assertEquals(2, queue.remove("item1"));
        assertEquals(2, queue.getNumberItems());

        assertEquals(1, queue.remove("item2"));
        assertEquals(1, queue.getNumberItems());

        assertEquals(0, queue.remove("item2"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals(3, queue.getNumberItems());

        queue.refreshElement("item1");

        assertEquals(4, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");

        assertEquals(1, queue.getNumberItems());

        queue.dequeue();

        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector objects = new Vector();

        assertEquals(objects, queue.getObjects());

        queue.enqueue("item1");
        objects.addElement("item1");
        assertEquals(objects, queue.getObjects());

        queue.enqueue("item2");
        objects.addElement("item2");
        assertEquals(objects, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();

        assertTrue(!queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}