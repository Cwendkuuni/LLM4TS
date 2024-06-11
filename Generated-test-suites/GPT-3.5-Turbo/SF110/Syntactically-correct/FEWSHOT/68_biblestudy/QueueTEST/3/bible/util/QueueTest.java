
package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(1, queue.remove("item2"));
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.remove("item1"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.refreshElement("item");
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        Vector objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertEquals("item2", objects.elementAt(0));
        assertEquals("item1", objects.elementAt(1));
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
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
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Vector objects = queue.getObjects();
        
        assertNotNull(objects);
        assertEquals(0, objects.size());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertEquals("item1", objects.elementAt(0));
        assertEquals("item2", objects.elementAt(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        String expectedString = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        
        assertEquals(expectedString, queue.toString());
        
        queue.enqueue("item1");
        expectedString = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item1]\r\n]";
        assertEquals(expectedString, queue.toString());
        
        queue.enqueue("item2");
        expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}
