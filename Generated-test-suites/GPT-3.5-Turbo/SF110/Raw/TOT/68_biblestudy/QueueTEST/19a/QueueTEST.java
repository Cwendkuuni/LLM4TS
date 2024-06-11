import static org.junit.Assert.*;

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
        queue.enqueue("Item");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        assertFalse(queue.isEmpty());
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals("Item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("Item2", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item"));

        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item1");
        queue.enqueue("Item3");
        
        assertEquals(2, queue.remove("Item1"));
        assertEquals(2, queue.getNumberItems());
        assertEquals(1, queue.remove("Item3"));
        assertEquals(1, queue.getNumberItems());
        
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());

        assertEquals(0, queue.remove("Item"));

        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals("Item1", queue.dequeue());
        queue.enqueue("Item3");
        queue.refreshElement("Item1");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.remove("Item1"));
        assertEquals(1, queue.remove("Item3"));
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        assertEquals(3, queue.getNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Item1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Item2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector expectedVector = new Vector();
        Vector actualVector = queue.getObjects();

        assertEquals(expectedVector, actualVector);

        queue.enqueue("Item1");
        expectedVector.addElement("Item1");
        actualVector = queue.getObjects();

        assertEquals(expectedVector, actualVector);

        queue.enqueue("Item2");
        expectedVector.addElement("Item2");
        actualVector = queue.getObjects();

        assertEquals(expectedVector, actualVector);

        queue.dequeue();
        expectedVector.removeElementAt(0);
        actualVector = queue.getObjects();

        assertEquals(expectedVector, actualVector);
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("Item1");
        assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Item1]\r\n]", queue.toString());

        queue.enqueue("Item2");
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]", queue.toString());

        queue.dequeue();
        assertEquals("Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()=[Item2]\r\n]", queue.toString());

        queue.dequeue();
        assertEquals("Queue:[numItems=0, maxNumItems=2, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    }

}