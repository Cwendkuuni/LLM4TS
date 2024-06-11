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
        queue.enqueue("Item1");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertEquals("Item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        
        assertEquals("Item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item1");
        queue.enqueue("Item3");
        
        assertEquals(2, queue.remove("Item1"));
        assertEquals(2, queue.getNumberItems());
        
        assertEquals(1, queue.remove("Item2"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(1, queue.remove("Item3"));
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        queue.refreshElement("Item1");
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement("Item2");
        assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement("Item3");
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Item2");
        assertEquals(2, queue.getNumberItems());
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
        
        Vector<String> objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertTrue(objects.contains("Item1"));
        assertTrue(objects.contains("Item2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item1");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}