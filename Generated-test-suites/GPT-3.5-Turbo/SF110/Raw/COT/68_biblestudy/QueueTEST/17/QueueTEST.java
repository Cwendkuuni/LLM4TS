import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Object dequeuedObj = queue.dequeue();
        assertEquals(1, dequeuedObj);
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        int removedItems = queue.remove(2);
        assertEquals(1, removedItems);
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.refreshElement(2);
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        Vector objects = queue.getObjects();
        assertTrue(objects.contains(2));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue(1);
        assertEquals(1, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue(1);
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue(2);
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Vector objects = queue.getObjects();
        assertEquals(0, objects.size());
        
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertTrue(objects.contains(1));
        assertTrue(objects.contains(2));
        assertTrue(objects.contains(3));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        String expected = "Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());
        
        queue.enqueue(1);
        expected = "Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[1]\r\n]";
        assertEquals(expected, queue.toString());
    }
}