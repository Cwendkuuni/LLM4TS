import org.junit.*;

import static org.junit.Assert.*;

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
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue(2);
        assertEquals(2, queue.getNumberItems());
        
        queue.enqueue(3);
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        
        assertEquals(2, queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(3, queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        
        assertEquals(2, queue.remove(2));
        assertEquals(2, queue.getNumberItems());
        
        assertEquals(0, queue.remove(4));
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        
        queue.refreshElement(1);
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement(2);
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement(3);
        assertEquals(3, queue.getNumberItems());
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
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        
        Vector expected = new Vector();
        expected.addElement(1);
        expected.addElement(2);
        expected.addElement(3);
        
        assertEquals(expected, queue.getObjects());
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
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[1, 2]\r\n]", queue.toString());
        
        queue.dequeue();
        assertEquals("Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()=[2]\r\n]", queue.toString());
    }
}