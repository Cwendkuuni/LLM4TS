import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Item1");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("Item1", objects.get(0));
        assertEquals("Item2", objects.get(1));
        assertEquals("Item3", objects.get(2));
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        Object dequeuedItem = queue.dequeue();
        assertEquals("Item1", dequeuedItem);
        
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertEquals("Item2", objects.get(0));
        assertEquals("Item3", objects.get(1));
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        int numRemoved = queue.remove("Item2");
        assertEquals(1, numRemoved);
        
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertEquals("Item1", objects.get(0));
        assertEquals("Item3", objects.get(1));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        queue.refreshElement("Item2");
        
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("Item1", objects.get(0));
        assertEquals("Item3", objects.get(1));
        assertEquals("Item2", objects.get(2));
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals(3, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals(3, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Item1, Item2, Item3]\r\n]";
        assertEquals(expected, queue.toString());
    }
}