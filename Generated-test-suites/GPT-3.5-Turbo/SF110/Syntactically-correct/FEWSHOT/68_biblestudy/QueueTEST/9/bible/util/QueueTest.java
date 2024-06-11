
package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    private Queue queue;
    
    @Before
    public void setup() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        int removed = queue.remove("item2");
        assertEquals(1, removed);
        assertFalse(queue.getObjects().contains("item2"));
        
        removed = queue.remove("item1");
        assertEquals(1, removed);
        assertFalse(queue.getObjects().contains("item1"));
        
        removed = queue.remove("item3");
        assertEquals(1, removed);
        assertFalse(queue.getObjects().contains("item3"));
        
        removed = queue.remove("item4");
        assertEquals(0, removed);
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        queue.refreshElement("item1");
        assertTrue(queue.getObjects().firstElement().equals("item1"));
        assertTrue(queue.getObjects().lastElement().equals("item2"));
        
        queue.refreshElement("item2");
        assertTrue(queue.getObjects().firstElement().equals("item1"));
        assertTrue(queue.getObjects().lastElement().equals("item2"));
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
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
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertTrue(objects.contains("item1"));
        assertTrue(objects.contains("item2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}
