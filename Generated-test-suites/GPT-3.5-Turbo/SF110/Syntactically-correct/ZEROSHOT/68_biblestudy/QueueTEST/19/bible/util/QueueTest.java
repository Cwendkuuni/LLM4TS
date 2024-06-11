import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bible.util.Queue;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("Item 2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals("Item 1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item 1"));
        
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals(1, queue.remove("Item 1"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Item 1");
        queue.refreshElement("Item 1");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Item 1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Item 2");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        assertEquals(0, queue.getObjects().size());
        
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        assertEquals(2, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("Item 1"));
        assertTrue(queue.getObjects().contains("Item 2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        Queue queueWithCapacity = new Queue(2);
        queueWithCapacity.enqueue("Item 1");
        queueWithCapacity.enqueue("Item 2");
        assertFalse(queueWithCapacity.maxCapacityExceeded());
        
        queueWithCapacity.enqueue("Item 3");
        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]", queue.toString());
        
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item 1, Item 2]\r\n]", queue.toString());
    }
}