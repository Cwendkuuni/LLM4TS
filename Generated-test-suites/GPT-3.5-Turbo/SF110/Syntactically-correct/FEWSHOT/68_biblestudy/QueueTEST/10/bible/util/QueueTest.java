package bible.util;

import static org.junit.Assert.*;

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
    public void testEnqueue_and_Dequeue() {
        queue.enqueue("item1");
        assertEquals("item1", queue.dequeue());
        
        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals("item2", queue.dequeue());
        assertEquals("item3", queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(2, queue.remove("item2"));
        assertEquals("item1", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        
        assertEquals(0, queue.remove("item4"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        queue.refreshElement("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
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
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("item1", objects.elementAt(0));
        assertEquals("item2", objects.elementAt(1));
        assertEquals("item3", objects.elementAt(2));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }

}