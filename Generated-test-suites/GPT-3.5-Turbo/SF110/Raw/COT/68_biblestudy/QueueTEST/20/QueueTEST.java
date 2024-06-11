
import org.junit.Test;
import java.util.Vector;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        assertEquals("Item 1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        
        assertEquals("Item 2", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        
        assertEquals("Item 3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        int removed1 = queue.remove("Item 1");
        assertEquals(1, removed1);
        assertEquals(2, queue.getNumberItems());
        
        int removed2 = queue.remove("Item 2");
        assertEquals(1, removed2);
        assertEquals(1, queue.getNumberItems());
        
        int removed3 = queue.remove("Item 3");
        assertEquals(1, removed3);
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Item 1");
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 2");
        
        int removed4 = queue.remove("Item 1");
        assertEquals(2, removed4);
        assertEquals(2, queue.getNumberItems());
        
        int removed5 = queue.remove("Item 2");
        assertEquals(2, removed5);
        assertEquals(0, queue.getNumberItems());

        int removed6 = queue.remove("Item 3");
        assertEquals(0, removed6);
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        queue.refreshElement("Item 2");
        
        Vector objects = queue.getObjects();
        assertEquals("Item 1", objects.elementAt(0));
        assertEquals("Item 3", objects.elementAt(1));
        assertEquals("Item 2", objects.elementAt(2));
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        assertEquals(3, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        Vector objects = queue.getObjects();
        
        assertEquals(3, objects.size());
        assertEquals("Item 1", objects.elementAt(0));
        assertEquals("Item 2", objects.elementAt(1));
        assertEquals("Item 3", objects.elementAt(2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item 3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 1, Item 2, Item 3]\r\n]";
        
        assertEquals(expected, queue.toString());
    }
}
