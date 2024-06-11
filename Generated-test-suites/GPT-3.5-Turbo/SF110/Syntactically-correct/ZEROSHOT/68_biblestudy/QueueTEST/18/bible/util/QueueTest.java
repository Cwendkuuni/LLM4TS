package bible.util;

import static org.junit.Assert.*;
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
        queue.enqueue("element");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("element");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("element");
        assertEquals("element", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("element1");
        queue.enqueue("element2");
        queue.enqueue("element3");
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(2, queue.remove("element1"));
        assertEquals(2, queue.getNumberItems());
        assertNull(queue.remove("element1"));
        assertEquals(2, queue.getNumberItems());
        assertEquals(1, queue.remove("element2"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("element1");
        queue.enqueue("element2");
        
        assertEquals(2, queue.getNumberItems());
        queue.refreshElement("element1");
        assertEquals(2, queue.getNumberItems());
        queue.refreshElement("element2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("element1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("element2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("element1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("element2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        Vector objects = queue.getObjects();
        assertEquals(expected, objects);
        
        queue.enqueue("element1");
        expected.add("element1");
        objects = queue.getObjects();
        assertEquals(expected, objects);
        
        queue.enqueue("element2");
        expected.add("element2");
        objects = queue.getObjects();
        assertEquals(expected, objects);
        
        queue.dequeue();
        expected.remove(0);
        objects = queue.getObjects();
        assertEquals(expected, objects);
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        
        Queue queueWithMaxCapacity = new Queue(2);
        queueWithMaxCapacity.enqueue("element1");
        queueWithMaxCapacity.enqueue("element2");
        assertFalse(queueWithMaxCapacity.maxCapacityExceeded());
        queueWithMaxCapacity.enqueue("element3");
        assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]";
        assertEquals(expected, queue.toString());
        
        queue.enqueue("element");
        expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[element]\r\n]";
        assertEquals(expected, queue.toString());
    }
}