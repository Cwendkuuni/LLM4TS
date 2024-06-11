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

        queue.enqueue("item1");

        assertFalse(queue.isEmpty());

        queue.dequeue();

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertEquals("item3", queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item1");

        assertEquals(2, queue.remove("item1"));
        assertEquals(2, queue.getNumberItems());

        queue.remove("item2");

        assertEquals(1, queue.getNumberItems());

        queue.remove("item3");

        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        queue.refreshElement("item2");

        assertEquals(3, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals(3, queue.getPeakNumberItems());

        queue.dequeue();

        assertEquals(3, queue.getPeakNumberItems());
        
        queue.enqueue("item4");

        assertEquals(4, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        Vector expectedVector = new Vector();
        expectedVector.add("item1");
        expectedVector.add("item2");
        expectedVector.add("item3");

        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        Queue queueWithMaxCapacity = new Queue(2);

        queueWithMaxCapacity.enqueue("item1");
        queueWithMaxCapacity.enqueue("item2");

        assertTrue(queueWithMaxCapacity.maxCapacityExceeded());

        queueWithMaxCapacity.dequeue();

        assertFalse(queueWithMaxCapacity.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        String expectedString = "Queue:[numItems=2, maxNumItems=0, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";

        assertEquals(expectedString, queue.toString());
    }
}