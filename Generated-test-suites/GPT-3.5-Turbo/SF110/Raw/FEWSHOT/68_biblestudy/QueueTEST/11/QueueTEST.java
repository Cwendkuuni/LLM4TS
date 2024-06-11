package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        Vector objects = queue.getObjects();
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }
    
    @Test
    public void testDequeue() {
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
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        queue.enqueue("item3");
        assertEquals(2, queue.remove("item1"));
        assertEquals(2, queue.getNumberItems());
        Vector objects = queue.getObjects();
        assertEquals("item2", objects.get(0));
        assertEquals("item3", objects.get(1));
        assertEquals(0, queue.remove("item4"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item4");
        queue.refreshElement("item2");
        Vector objects = queue.getObjects();
        assertEquals("item1", objects.get(0));
        assertEquals("item3", objects.get(1));
        assertEquals("item4", objects.get(2));
        assertEquals("item2", objects.get(3));
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
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
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Vector objects = queue.getObjects();
        assertEquals("item1", objects.get(0));
        assertEquals("item2", objects.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
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
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}