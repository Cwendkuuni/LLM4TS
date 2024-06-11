package bible.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Vector;

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
        queue.enqueue("Item3");
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals("Item1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item1"));
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item1");
        
        assertEquals(2, queue.remove("Item1"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        queue.refreshElement("Item2");
        
        assertEquals(3, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("Item2"));
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector objects = queue.getObjects();
        
        assertEquals(0, objects.size());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertTrue(objects.contains("Item1"));
        assertTrue(objects.contains("Item2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue = new Queue(2);
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]", queue.toString());
    }
}