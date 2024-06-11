package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        
        queue.enqueue("foo");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("foo");
        assertEquals("foo", queue.dequeue());
        
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertEquals("foo", queue.dequeue());
        assertEquals("bar", queue.dequeue());
        
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("foo"));
        
        queue.enqueue("foo");
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("foo");
        assertEquals(3, queue.remove("foo"));
        assertEquals(1, queue.remove("bar"));
        assertEquals(0, queue.remove("foo"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        queue.refreshElement("bar");
        assertEquals("foo", queue.dequeue());
        assertEquals("baz", queue.dequeue());
        assertEquals("bar", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("foo");
        assertEquals(1, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("foo");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("bar");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.enqueue("baz");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("foo");
        expected.add("foo");
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("bar");
        expected.add("bar");
        assertEquals(expected, queue.getObjects());
        
        queue.dequeue();
        expected.remove(0);
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("baz");
        expected.add("baz");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        queue = new Queue(2);
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());
        
        queue.enqueue("foo");
        expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[foo]\r\n]";
        assertEquals(expected, queue.toString());
        
        queue.enqueue("bar");
        expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[foo, bar]\r\n]";
        assertEquals(expected, queue.toString());
    }
}