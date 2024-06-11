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
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(2, queue.remove("item1"));
        assertEquals(0, queue.remove("item1"));
        assertEquals(0, queue.remove("item2"));
        assertEquals(1, queue.remove("item3"));
        assertEquals(0, queue.getNumberItems());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.getObjects().get(0));
        queue.refreshElement("item1");
        assertEquals("item2", queue.getObjects().get(0));
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        
        Vector objects = queue.getObjects();
        assertEquals(0, objects.size());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]";
        
        assertEquals(expected, queue.toString());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        
        assertEquals(expected, queue.toString());
    }

}