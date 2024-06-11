import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {
    
    // Test case for isEmpty method
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    // Test case for enqueue method - typical use case
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    // Test case for enqueue method - edge case (empty queue)
    @Test
    public void testEnqueueEmptyQueue() {
        Queue queue = new Queue();
        
        queue.enqueue("item");
        
        assertFalse(queue.isEmpty());
    }
    
    // Test case for enqueue method - error scenario (null object)
    @Test
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        
        queue.enqueue(null);
        
        assertTrue(queue.isEmpty());
    }
    
    // Test case for dequeue method - typical use case
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        Object item = queue.dequeue();
        
        assertEquals("item1", item);
        assertEquals(1, queue.getNumberItems());
    }
    
    // Test case for dequeue method - edge case (queue with only one item)
    @Test
    public void testDequeueSingleItem() {
        Queue queue = new Queue();
        queue.enqueue("item");
        
        Object item = queue.dequeue();
        
        assertEquals("item", item);
        assertTrue(queue.isEmpty());
    }
    
    // Test case for dequeue method - error scenario (queue is empty)
    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        
        Object item = queue.dequeue();
        
        assertNull(item);
    }
    
    // Test case for remove method - typical use case
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        
        int numRemoved = queue.remove("item1");
        
        assertEquals(2, numRemoved);
        assertEquals(1, queue.getNumberItems());
    }
    
    // Test case for remove method - edge case (remove from empty queue)
    @Test
    public void testRemoveFromEmptyQueue() {
        Queue queue = new Queue();
        
        int numRemoved = queue.remove("item");
        
        assertEquals(0, numRemoved);
    }
    
    // Test case for remove method - error scenario (remove non-existing item)
    @Test
    public void testRemoveNonExistingItem() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        int numRemoved = queue.remove("item3");
        
        assertEquals(0, numRemoved);
        assertEquals(2, queue.getNumberItems());
    }
    
    // Test case for refreshElement method - typical use case
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        queue.refreshElement("item1");
        
        assertEquals(2, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals("item1", queue.dequeue());
    }
    
    // Test case for getNumberItems method
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        int numItems = queue.getNumberItems();
        
        assertEquals(2, numItems);
    }
    
    // Test case for getPeakNumberItems method
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    // Test case for getObjects method
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        Vector objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }
    
    // Test case for maxCapacityExceeded method - typical use case
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Test case for maxCapacityExceeded method - edge case (maximum capacity not exceeded)
    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("item1");
        
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for maxCapacityExceeded method - edge case (unlimited capacity)
    @Test
    public void testUnlimitedCapacity() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for toString method
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}