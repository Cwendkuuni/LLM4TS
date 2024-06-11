import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        // Create an empty queue and check if it is empty
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        // Enqueue an element and check if the queue is not empty
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        // Enqueue an element and check if it is added to the queue
        Queue queue = new Queue();
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testDequeue() {
        // Create a queue with an element and dequeue it
        Queue queue = new Queue();
        queue.enqueue("item");
        Object item = queue.dequeue();
        
        // Check if the dequeued item is equal to the original item 
        assertEquals("item", item);
        
        // Check if the queue is empty after dequeuing the only element
        assertTrue(queue.isEmpty());
        
        // Check if dequeueing from an empty queue returns null
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        // Remove an element from the queue
        int countRemoved = queue.remove("item2");
        
        // Check if the item was removed and count matches
        assertEquals(1, countRemoved);
        assertFalse(queue.isEmpty());
        
        // Remove an element that does not exist in the queue
        countRemoved = queue.remove("item4");
        
        // Check that no items were removed
        assertEquals(0, countRemoved);
    }
    
    @Test
    public void testRefreshElement() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        // Refresh an element in the queue
        queue.refreshElement("item2");
        
        // Check if the item has moved to the end of the queue
        assertEquals("item1", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        // Check if the number of items in the queue is correct
        assertEquals(2, queue.getNumberItems());
        
        // Dequeue an element and check if the number decrements
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
        
        // Check if the number of items in an empty queue is 0
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        // Check if the peak number of items is the current number of items
        assertEquals(2, queue.getPeakNumberItems());
        
        // Add more items to increase the peak number
        queue.enqueue("item3");
        queue.enqueue("item4");
        
        // Check if the peak number is updated
        assertEquals(4, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        // Check if the getObjects() method returns the correct vector
        Vector expected = new Vector();
        expected.add("item1");
        expected.add("item2");
        expected.add("item3");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        // Create a queue with a maximum capacity of 2
        Queue queue = new Queue(2);
        
        // Enqueue two items, which should not exceed the capacity
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertFalse(queue.maxCapacityExceeded());
        
        // Enqueue one more item, which should exceed the capacity
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testtoString() {
        // Create a queue with multiple elements
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        // Check if the toString() method returns the expected string representation of the queue
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}