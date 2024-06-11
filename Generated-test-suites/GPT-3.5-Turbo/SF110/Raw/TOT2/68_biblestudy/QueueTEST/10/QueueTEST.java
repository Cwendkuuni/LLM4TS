import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {
    
    // Test isEmpty method when queue is empty
    @Test
    public void testIsEmptyEmptyQueue() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    // Test enqueue method to add an object to queue
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        assertFalse(queue.isEmpty());
    }
    
    // Test dequeue method when queue is empty
    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
    }
    
    // Test remove method to remove object from queue
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        assertEquals(1, queue.remove("Test Object"));
    }
    
    // Test refreshElement method to update object in queue
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Old Object");
        queue.refreshElement("New Object");
        assertTrue(queue.getObjects().contains("New Object"));
    }
    
    // Test getPeakNumberItems method
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    // Test maxCapacityExceeded method when max capacity is exceeded
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n[]\r\n]", queue.toString());
    }
}