package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("test"));
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test1");
        queue.enqueue("test3");
        assertEquals(2, queue.remove("test1"));
        assertEquals(1, queue.remove("test3"));
        assertEquals(0, queue.remove("test4"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals(2, queue.getNumberItems());
        queue.refreshElement("test1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("test2", queue.dequeue());
        assertEquals("test1", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("test1");
        assertEquals(1, queue.getNumberItems());
        queue.dequeue();
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        queue.enqueue("test3");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expectedVector = new Vector(2, 10);
        assertEquals(expectedVector, queue.getObjects());
        queue.enqueue("test1");
        expectedVector.addElement("test1");
        assertEquals(expectedVector, queue.getObjects());
        queue.enqueue("test2");
        expectedVector.addElement("test2");
        assertEquals(expectedVector, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(2);
        queue.enqueue("test1");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]", queue.toString());
    }
}