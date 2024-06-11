package bible.util;

import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        assertEquals("Item1", queue.dequeue());
        assertEquals("Item2", queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        
        assertNull(queue.dequeue());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertEquals("Item1", queue.dequeue());
        assertEquals("Item2", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        assertEquals(2, queue.remove("Item2"));
        assertEquals(1, queue.remove("Item3"));
        assertEquals(0, queue.remove("Item1"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        
        queue.refreshElement("Item2");
        assertEquals("Item1", queue.dequeue());
        assertEquals("Item3", queue.dequeue());
        assertEquals("Item2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("Item2");
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
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
        Queue queue = new Queue();
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        Vector expected = new Vector();
        expected.addElement("Item1");
        expected.addElement("Item2");
        
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]";
        
        assertEquals(expected, queue.toString());
    }
}