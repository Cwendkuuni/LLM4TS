package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        assertEquals(2, queue.getNumberItems());
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        assertEquals(2, queue.remove("Test2"));
        assertEquals(0, queue.remove("Test2"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        queue.refreshElement("Test1");
        assertEquals("Test2", queue.dequeue());
        assertEquals("Test1", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Test1");
        assertEquals(1, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Test1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("Test1", objects.get(0));
        assertEquals("Test2", objects.get(1));
        assertEquals("Test3", objects.get(2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Test3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}