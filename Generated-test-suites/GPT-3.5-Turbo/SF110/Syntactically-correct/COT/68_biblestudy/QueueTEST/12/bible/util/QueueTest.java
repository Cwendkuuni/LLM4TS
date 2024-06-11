
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import bible.util.Queue;

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
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        assertEquals("element", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("anotherElement", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("element");
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        assertEquals(2, queue.remove("element"));
        assertEquals(1, queue.getNumberItems());
        assertEquals("anotherElement", queue.dequeue());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        queue.refreshElement("element");
        assertEquals("anotherElement", queue.dequeue());
        assertEquals("element", queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        queue.enqueue("element");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("anotherElement");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("element");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("anotherElement");
        queue.enqueue("thirdElement");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertEquals("element", objects.get(0));
        assertEquals("anotherElement", objects.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue("element");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("anotherElement");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("element");
        queue.enqueue("anotherElement");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[element, anotherElement]\r\n]", queue.toString());
    }
}
