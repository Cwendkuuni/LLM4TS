import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals("Item1", queue.dequeue());
        assertEquals("Item2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item"));
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item1");
        assertEquals(2, queue.remove("Item1"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(1, queue.remove("Item2"));
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals("Item1", queue.dequeue());
        
        queue.refreshElement("Item1");
        assertEquals("Item1", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Item1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Item2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals(2, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("Item1"));
        assertTrue(queue.getObjects().contains("Item2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(1);
        queue.enqueue("Item1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Item2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("Item");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Item]\r\n]", queue.toString());
    }
}