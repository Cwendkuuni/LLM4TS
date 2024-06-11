import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        Object dequeuedItem = queue.dequeue();
        
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertEquals("Item 1", dequeuedItem);
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        int numRemoved = queue.remove("Item 1");
        
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertEquals(1, numRemoved);
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        queue.refreshElement("Item 2");
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("Item 2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        Vector objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertEquals("Item 1", objects.get(0));
        assertEquals("Item 2", objects.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=2, getObjects()=[Item 1, Item 2]\r\n]";
        
        assertEquals(expected, queue.toString());
    }
}