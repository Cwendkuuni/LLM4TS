package bible.util;

import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Object obj = new Object();
        queue.enqueue(obj);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(obj, queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(obj1, queue.dequeue());
        assertEquals(obj2, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(1, queue.remove(obj1));
        assertFalse(queue.isEmpty());
        assertEquals(obj2, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRefreshElement() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(new Object());
        queue.refreshElement(obj1);
        assertEquals(obj2, queue.dequeue());
        assertEquals(obj1, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue(new Object());
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertEquals(3, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        assertEquals(2, queue.getObjects().size());
        assertTrue(queue.getObjects().contains(obj1));
        assertTrue(queue.getObjects().contains(obj2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());
        queue.enqueue(new Object());
        expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[java.lang.Object@xxxxxx]\r\n]";
        assertEquals(expected, queue.toString());
    }
}