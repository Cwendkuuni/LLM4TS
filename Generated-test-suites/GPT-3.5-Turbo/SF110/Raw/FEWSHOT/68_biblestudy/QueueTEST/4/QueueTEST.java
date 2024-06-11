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
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("item1");
        assertEquals("item1", queue.dequeue());
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals("item2", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
        assertFalse(queue.getObjects().contains("item1"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertTrue(queue.isEmpty());
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
        
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expectedObjects = new Vector();
        
        assertEquals(expectedObjects, queue.getObjects());
        
        queue.enqueue("item1");
        expectedObjects.addElement("item1");
        assertEquals(expectedObjects, queue.getObjects());
        
        queue.enqueue("item2");
        expectedObjects.addElement("item2");
        assertEquals(expectedObjects, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}