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
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("item4");
        assertEquals(4, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertEquals("item1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("item3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        assertEquals(1, queue.remove("item1"));
        assertEquals(2, queue.getNumberItems());
        assertEquals(0, queue.remove("item4"));
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.refreshElement("item2");
        assertEquals(3, queue.getNumberItems());
        Vector expected = new Vector();
        expected.addElement("item1");
        expected.addElement("item3");
        expected.addElement("item2");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        expected.addElement("item1");
        expected.addElement("item2");
        expected.addElement("item3");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(2);
        queue.enqueue("item4");
        queue.enqueue("item5");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[item1, item2, item3]\r\n]";
        assertEquals(expected, queue.toString());
    }
}