package bible.util;

import static org.junit.Assert.assertEquals;
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
    }

    @Test
    public void testEnqueueAndDequeue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
        assertEquals(obj3, queue.dequeue());
    }

    @Test
    public void testRemove() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        assertEquals(2, queue.remove(obj1));
        assertNull(queue.dequeue());
        assertEquals(1, queue.remove(obj3));
        assertNull(queue.dequeue());
        assertEquals(0, queue.remove(obj2));
    }

    @Test
    public void testRefreshElement() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);

        queue.refreshElement(obj1);

        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertEquals(0, queue.getNumberItems());

        queue.enqueue(obj1);
        queue.enqueue(obj2);

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue(obj1);
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue(obj2);
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);

        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertTrue(objects.contains(obj1));
        assertTrue(objects.contains(obj2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithMaxCapacity = new Queue(2);

        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queueWithMaxCapacity.enqueue(obj1);
        queueWithMaxCapacity.enqueue(obj2);

        assertTrue(queueWithMaxCapacity.maxCapacityExceeded());

        queueWithMaxCapacity.enqueue(obj3);

        assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
    }
}