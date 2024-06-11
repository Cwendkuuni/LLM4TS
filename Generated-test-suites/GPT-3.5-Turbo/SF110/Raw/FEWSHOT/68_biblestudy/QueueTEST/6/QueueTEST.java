import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
        
        queue.enqueue("item1");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        
        assertEquals("item2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        
        assertEquals(0, queue.remove("item3"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        
        queue.refreshElement("item1");
        
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        assertNotNull(queue.getObjects());
        assertEquals(0, queue.getObjects().size());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        Vector objects = queue.getObjects();
        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.contains("item1"));
        assertTrue(objects.contains("item2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        Queue queueWithCapacity = new Queue(2);
        
        queueWithCapacity.enqueue("item1");
        queueWithCapacity.enqueue("item2");
        
        assertFalse(queueWithCapacity.maxCapacityExceeded());
        
        queueWithCapacity.enqueue("item3");
        
        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}