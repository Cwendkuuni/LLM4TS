package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {
    Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        
        queue.enqueue("a");
        assertFalse(queue.isEmpty());
        
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        
        assertEquals("a", queue.dequeue());
        assertEquals("b", queue.dequeue());
        assertEquals("c", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.enqueue("a");
        
        assertEquals(2, queue.remove("a"));
        assertEquals(2, queue.getNumberItems());
        
        assertEquals(0, queue.remove("d"));
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        
        queue.refreshElement("b");
        
        Vector expected = new Vector();
        expected.add("a");
        expected.add("c");
        expected.add("b");
        
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("a");
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("b");
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.enqueue("c");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("a");
        queue.enqueue("b");
        expected.add("a");
        expected.add("b");
        assertEquals(expected, queue.getObjects());
        
        queue.dequeue();
        expected.remove(0);
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        Queue queue2 = new Queue(2);
        queue2.enqueue("a");
        queue2.enqueue("b");
        assertTrue(queue2.maxCapacityExceeded());
        
        queue2.dequeue();
        assertFalse(queue2.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("a");
        queue.enqueue("b");
        
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[a, b]\r\n]", queue.toString());
    }
}