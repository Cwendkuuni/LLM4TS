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
    public void setup() {
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

        queue.enqueue(obj1);
        queue.enqueue(obj2);

        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
    }

    @Test
    public void testRemove() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        Object obj4 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        queue.enqueue(obj4);

        assertEquals(2, queue.remove(obj2));
        assertNull(queue.dequeue());
    }

    @Test
    public void testRefreshElement() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.refreshElement(obj1);
        queue.enqueue(obj2);
        queue.refreshElement(obj1);
        queue.enqueue(obj3);
        queue.refreshElement(obj1);

        assertEquals(obj2, queue.dequeue());
        assertEquals(obj3, queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        Vector expectedObjects = new Vector();
        expectedObjects.addElement(obj1);
        expectedObjects.addElement(obj2);
        expectedObjects.addElement(obj3);

        assertEquals(expectedObjects, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithCapacity = new Queue(2);

        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queueWithCapacity.enqueue(obj1);
        queueWithCapacity.enqueue(obj2);
        queueWithCapacity.enqueue(obj3);

        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);

        String expectedString = "bible.util.Queue:[numItems=3, maxNumItems=0, maxCapacity=-1, getObjects()=[Object@hashcode, Object@hashcode, Object@hashcode]\r\n]";

        assertEquals(expectedString, queue.toString());
    }
}