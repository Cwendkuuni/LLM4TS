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
        queue.enqueue("foo");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertEquals("foo", queue.dequeue());
        assertEquals("bar", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("foo");
        queue.remove("foo");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.refreshElement("foo");
        Vector expected = new Vector();
        expected.addElement("bar");
        expected.addElement("foo");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("foo");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("foo");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("bar");
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
        queue.enqueue("foo");
        expected.addElement("foo");
        assertEquals(expected, queue.getObjects());
        queue.enqueue("bar");
        expected.addElement("bar");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(2);
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("foo");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[foo]\r\n]", queue.toString());
    }
}