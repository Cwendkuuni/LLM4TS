import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("item");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue("item");
        assertEquals("item", queue.dequeue());
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("item"));
        
        queue.enqueue("item");
        assertEquals(1, queue.remove("item"));
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(1, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.refreshElement("item");
        assertTrue(queue.isEmpty());
        
        queue.enqueue("item1");
        queue.refreshElement("item1");
        assertEquals(1, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("item");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("item1");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Vector expectedVector = new Vector();
        assertEquals(expectedVector, queue.getObjects());

        queue.enqueue("item1");
        expectedVector.add("item1");
        assertEquals(expectedVector, queue.getObjects());

        queue.enqueue("item2");
        expectedVector.add("item2");
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        String expectedString = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expectedString, queue.toString());
        
        queue.enqueue("item1");
        expectedString = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item1]\r\n]";
        assertEquals(expectedString, queue.toString());
        
        queue.enqueue("item2");
        expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}