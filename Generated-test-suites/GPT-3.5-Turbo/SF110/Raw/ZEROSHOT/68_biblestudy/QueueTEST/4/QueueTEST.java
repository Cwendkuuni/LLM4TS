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
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("Item1");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("Item1", queue.getObjects().elementAt(0));
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("Item1");
        assertEquals("Item1", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        queue.enqueue("Item2");
        assertEquals(2, queue.remove("Item2"));
        assertTrue(queue.getObjects().contains("Item1"));
        assertFalse(queue.getObjects().contains("Item2"));
        assertTrue(queue.getObjects().contains("Item3"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.refreshElement("Item1");
        assertFalse(queue.getObjects().contains("Item1"));
        assertTrue(queue.getObjects().contains("Item2"));
        assertEquals("Item1", queue.getObjects().elementAt(1));
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item1");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Item1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Item2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        Vector tempVector = new Vector();
        tempVector.addElement("Item1");
        tempVector.addElement("Item2");
        tempVector.addElement("Item3");
        assertEquals(tempVector, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(3);
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item1, Item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}