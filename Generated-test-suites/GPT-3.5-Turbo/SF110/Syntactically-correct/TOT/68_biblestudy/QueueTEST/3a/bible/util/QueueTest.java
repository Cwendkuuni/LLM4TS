import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(10);
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue(10);
        assertEquals(10, queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove(null));
        
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        assertEquals(2, queue.remove(10));
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.refreshElement(20);
        assertEquals(3, queue.getNumberItems());
        assertEquals(30, queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(10);
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(10);
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue(20);
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        Vector expectedVector = new Vector();
        expectedVector.addElement(10);
        expectedVector.addElement(20);
        expectedVector.addElement(30);
        assertEquals(expectedVector, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue(10);
        queue.enqueue(20);
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    }
}