
package bible.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue(5);
    }

    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    // Test enqueue() method
    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals(1, queue.getObjects().get(0));

        queue.enqueue("abc");
        assertEquals(2, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals("abc", queue.getObjects().get(1));
    }

    // Test dequeue() method
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    // Test remove() method
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove(1));

        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);

        assertEquals(3, queue.remove(1));
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(2, queue.getObjects().get(0));
        assertEquals(3, queue.getObjects().get(1));
    }

    // Test refreshElement() method
    @Test
    public void testRefreshElement() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        queue.refreshElement(2);

        assertEquals(3, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertEquals(1, queue.getObjects().get(0));
        assertEquals(3, queue.getObjects().get(1));
        assertEquals(2, queue.getObjects().get(2));
    }

    // Test maxCapacityExceeded() method
    @Test
    public void testMaxCapacityExceeded() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    // Test toString() method
    @Test
    public void testToString() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        String expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=Vector(1, 2, 3)\r\n]";
        assertEquals(expected, queue.toString());
    }
}
