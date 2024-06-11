import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {
    
    // Test case for isEmpty() method
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        
        // Test case: Queue is empty
        assertTrue(queue.isEmpty());
        
        // Test case: Queue is not empty
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    // Test case for enqueue() method
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        
        // Test case: Enqueue null object
        queue.enqueue(null);
        assertTrue(queue.isEmpty());
        
        // Test case: Enqueue valid object
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    // Test case for dequeue() method
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        
        // Test case: Dequeue from empty queue
        assertNull(queue.dequeue());
        
        // Test case: Dequeue from non-empty queue
        queue.enqueue("item");
        assertEquals("item", queue.dequeue());
    }
    
    // Test case for remove() method
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        // Test case: Remove from empty queue
        assertEquals(0, queue.remove("item"));
        
        // Test case: Remove from non-empty queue
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.remove("item2"));
    }
    
    // Test case for refreshElement() method
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        // Test case: Refresh non-existing object
        queue.enqueue("item1");
        queue.refreshElement("item2");
        assertEquals(1, queue.getNumberItems());
        
        // Test case: Refresh existing object
        queue.enqueue("item1");
        queue.refreshElement("item1");
        assertEquals(1, queue.getNumberItems());
    }
    
    // Test case for getNumberItems() method
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        // Test case: Get number of items from empty queue
        assertEquals(0, queue.getNumberItems());
        
        // Test case: Get number of items from non-empty queue
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }
    
    // Test case for getPeakNumberItems() method
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        // Test case: Get peak number of items with no insertions
        assertEquals(0, queue.getPeakNumberItems());
        
        // Test case: Get peak number of items with insertions
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    // Test case for getObjects() method
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        
        // Test case: Get objects from empty queue
        assertEquals(0, queue.getObjects().size());
        
        // Test case: Get objects from non-empty queue
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("item1"));
        assertTrue(queue.getObjects().contains("item2"));
    }
    
    // Test case for maxCapacityExceeded() method
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        // Test case: Max capacity not exceeded
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertFalse(queue.maxCapacityExceeded());
        
        // Test case: Max capacity exceeded
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Test case for toString() method
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        
        // Test case: Check toString() output
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]";
        assertEquals(expected, queue.toString());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
    
}