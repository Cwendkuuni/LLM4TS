package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
    public void testQueueConstructor() {
        Queue queue = new Queue();
        assertNotNull(queue);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueConstructorWithCapacity() {
        Queue queue = new Queue(10);
        assertNotNull(queue);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Object obj = new Object();
        queue.enqueue(obj);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        Object obj = new Object();
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        assertNotNull(dequeuedObj);
        assertEquals(obj, dequeuedObj);
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        int removedItems = queue.remove(obj2);
        assertEquals(1, removedItems);
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Object obj = new Object();
        queue.enqueue(obj);
        queue.refreshElement(obj);
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        Object obj = new Object();
        queue.enqueue(obj);
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        Object obj = new Object();
        queue.enqueue(obj);
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector objects = queue.getObjects();
        assertNotNull(objects);
        assertEquals(0, objects.size());

        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        objects = queue.getObjects();
        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.contains(obj1));
        assertTrue(objects.contains(obj2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        Queue queueWithCapacity = new Queue(2);
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        String queueString = queue.toString();
        assertNotNull(queueString);
        assertTrue(queueString.startsWith("bible.util.Queue"));
        assertTrue(queueString.contains("numItems=0"));
        assertTrue(queueString.contains("maxNumItems=0"));
        assertTrue(queueString.contains("maxCapacity=-1"));
        assertTrue(queueString.contains("getObjects()="));
        assertTrue(queueString.contains("[]"));
    }

}