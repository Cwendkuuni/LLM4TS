package bible.util;

import java.util.Vector;
import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue q = new Queue();
        assertTrue(q.isEmpty());
        q.enqueue("A");
        assertFalse(q.isEmpty());
        q.dequeue();
        assertTrue(q.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        Queue q = new Queue();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        assertEquals("A", q.dequeue());
        assertEquals("B", q.dequeue());
        assertEquals("C", q.dequeue());
        assertNull(q.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue q = new Queue();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        assertEquals(1, q.remove("A"));
        assertNull(q.dequeue());
        assertEquals(1, q.remove("C"));
        assertNull(q.dequeue());
        assertEquals(0, q.remove("B"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue q = new Queue();
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        q.refreshElement("B");
        assertEquals("A", q.dequeue());
        assertEquals("C", q.dequeue());
        assertEquals("B", q.dequeue());
        assertNull(q.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue q = new Queue();
        assertEquals(0, q.getNumberItems());
        q.enqueue("A");
        q.enqueue("B");
        assertEquals(2, q.getNumberItems());
        q.dequeue();
        assertEquals(1, q.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue q = new Queue();
        assertEquals(0, q.getPeakNumberItems());
        q.enqueue("A");
        assertEquals(1, q.getPeakNumberItems());
        q.enqueue("B");
        assertEquals(2, q.getPeakNumberItems());
        q.dequeue();
        assertEquals(2, q.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue q = new Queue();
        Vector expected = new Vector();
        expected.addElement("A");
        expected.addElement("B");
        expected.addElement("C");
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");
        Vector actual = q.getObjects();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue q = new Queue(2);
        assertFalse(q.maxCapacityExceeded());
        q.enqueue("A");
        q.enqueue("B");
        assertTrue(q.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue q = new Queue(2);
        q.enqueue("A");
        q.enqueue("B");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=2, getObjects()=[A, B]\r\n]";
        String actual = q.toString();
        assertEquals(expected, actual);
    }

}