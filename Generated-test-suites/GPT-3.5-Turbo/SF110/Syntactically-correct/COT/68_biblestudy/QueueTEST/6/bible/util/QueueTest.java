import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("item");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("another item");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("item");
        assertEquals("item", queue.dequeue());
        
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item");
        assertEquals(1, queue.remove("item"));
        
        queue.enqueue("item");
        queue.enqueue("item");
        assertEquals(2, queue.remove("item"));
        
        queue.enqueue("another item");
        assertEquals(0, queue.remove("item"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.refreshElement("item");
         assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item");
        queue.enqueue("another item");
        queue.refreshElement("item");
        assertEquals(1, queue.remove("item"));
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("item");
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item 1");
        queue.enqueue("item 2");
        
        Vector expected = new Vector();
        expected.addElement("item 1");
        expected.addElement("item 2");
        
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        queue = new Queue(1);
        
        queue.enqueue("item 1");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("item 1");
        queue.enqueue("item 2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item 1, item 2]\r\n]";
        
        assertEquals(expected, queue.toString());
    }
}