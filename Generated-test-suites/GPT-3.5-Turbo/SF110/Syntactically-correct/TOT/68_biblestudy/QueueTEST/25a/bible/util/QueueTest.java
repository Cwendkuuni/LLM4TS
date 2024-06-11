import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Item");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        
        Object item = queue.dequeue();
        assertNull(item);
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        item = queue.dequeue();
        assertEquals("Item1", item);
        assertEquals(1, queue.getNumberItems());
        
        item = queue.dequeue();
        assertEquals("Item2", item);
        assertEquals(0, queue.getNumberItems());
        
        item = queue.dequeue();
        assertNull(item);
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        int removedCount = queue.remove("Item");
        assertEquals(0, removedCount);
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        removedCount = queue.remove("Item");
        assertEquals(0, removedCount);
        assertEquals(2, queue.getNumberItems());
        
        removedCount = queue.remove("Item1");
        assertEquals(1, removedCount);
        assertEquals(1, queue.getNumberItems());
        
        removedCount = queue.remove("Item2");
        assertEquals(1, removedCount);
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        queue.refreshElement("Item");
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        queue.refreshElement("Item");
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement("Item1");
        assertEquals(2, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("Item1"));
        
        queue.refreshElement("Item2");
        assertEquals(2, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("Item2"));
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Item");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
}