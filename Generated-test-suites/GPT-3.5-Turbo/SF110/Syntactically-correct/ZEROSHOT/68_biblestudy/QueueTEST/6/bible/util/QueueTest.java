
package bible.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

        queue.enqueue("Element");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Element");

        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals("Element", queue.getObjects().get(0));
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("Element");
        assertEquals("Element", queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Element"));

        queue.enqueue("Element");
        assertEquals(1, queue.remove("Element"));

        queue.enqueue("Element");
        queue.enqueue("Element");
        assertEquals(2, queue.remove("Element"));

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Element");
        queue.refreshElement("Element");

        assertFalse(queue.isEmpty());
        assertEquals("Element", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Element");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Element");
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector vector = new Vector();

        assertEquals(vector, queue.getObjects());

        queue.enqueue("Element");
        vector.add("Element");
        assertEquals(vector, queue.getObjects());

        queue.enqueue("Element2");
        vector.add("Element2");
        assertEquals(vector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        queue.enqueue("Element");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("Element");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Element]\r\n]", queue.toString());
    }
}
