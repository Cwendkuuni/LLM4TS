import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
        
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        
        assertNull(queue.dequeue());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(1, queue.remove("item1"));
        assertEquals(2, queue.getNumberItems());
        
        assertEquals(1, queue.remove("item2"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(0, queue.remove("item3"));
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        queue.refreshElement("item2");
        
        assertEquals("item1", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(3, queue.getNumberItems());
    }
    
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
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getObjects().size());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals("item1", queue.getObjects().get(0));
        assertEquals("item2", queue.getObjects().get(1));
        assertEquals("item3", queue.getObjects().get(2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals("bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[item1, item2, item3]\r\n]", queue.toString());
    }
}