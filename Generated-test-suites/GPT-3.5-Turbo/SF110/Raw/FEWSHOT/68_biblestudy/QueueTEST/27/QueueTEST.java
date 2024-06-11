package bible.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
        
        Vector expected = new Vector();
        expected.addElement("item1");
        expected.addElement("item2");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        Object item1 = queue.dequeue();
        assertEquals("item1", item1);
        assertEquals(1, queue.getNumberItems());
        
        Object item2 = queue.dequeue();
        assertEquals("item2", item2);
        assertEquals(0, queue.getNumberItems());
        
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        
        int removedCount = queue.remove("item1");
        assertEquals(2, removedCount);
        assertEquals(1, queue.getNumberItems());
        
        Vector expected = new Vector();
        expected.addElement("item2");
        assertEquals(expected, queue.getObjects());
        
        removedCount = queue.remove("item3");
        assertEquals(0, removedCount);
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        queue.refreshElement("item1");
        
        Vector expected = new Vector();
        expected.addElement("item2");
        expected.addElement("item3");
        expected.addElement("item1");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
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
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("item1");
        expected.addElement("item1");
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("item2");
        expected.addElement("item2");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=0, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}