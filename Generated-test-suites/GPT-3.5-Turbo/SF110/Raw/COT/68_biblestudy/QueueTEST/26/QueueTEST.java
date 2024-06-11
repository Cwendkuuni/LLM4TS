import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Hello");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Hello");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue(123);
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue("Hello");
        assertEquals("Hello", queue.dequeue());
        
        queue.enqueue("Hello");
        queue.enqueue("World");
        assertEquals("Hello", queue.dequeue());
        assertEquals("World", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("Hello"));
        
        queue.enqueue("Hello");
        queue.enqueue("World");
        assertEquals(1, queue.remove("Hello"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(0, queue.remove("Hello"));
        assertEquals(0, queue.remove("World"));
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Hello");
        queue.enqueue("World");
        queue.enqueue("Hello");
        assertEquals(2, queue.remove("Hello"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Hello");
        queue.enqueue("World");
        
        queue.refreshElement("Hello");
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Hello");
        assertEquals(1, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Hello");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("World");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        assertEquals(0, queue.getObjects().size());
        
        queue.enqueue("Hello");
        assertEquals(1, queue.getObjects().size());
        
        queue.enqueue("World");
        assertEquals(2, queue.getObjects().size());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Hello");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("World");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        queue.enqueue("Hello");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("Hello");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Hello]\r\n]", queue.toString());
    }
}