package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test");

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test");

        Object obj = queue.dequeue();
        assertEquals("Test", obj);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        obj = queue.dequeue();
        assertNull(obj);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test");
        queue.enqueue("Test");

        int removed = queue.remove("Test");
        assertEquals(3, removed);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");

        queue.refreshElement("Test");
        assertFalse(queue.isEmpty());

        queue.refreshElement("New Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();

        int numberItems = queue.getNumberItems();
        assertEquals(0, numberItems);

        queue.enqueue("Test");
        numberItems = queue.getNumberItems();
        assertEquals(1, numberItems);
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();

        int peakNumberItems = queue.getPeakNumberItems();
        assertEquals(0, peakNumberItems);

        queue.enqueue("Test");
        peakNumberItems = queue.getPeakNumberItems();
        assertEquals(1, peakNumberItems);
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        Vector expected = new Vector();
        expected.addElement("Test1");
        expected.addElement("Test2");

        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Vector objects = queue.getObjects();
        assertEquals(expected, objects);
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());

        queue.enqueue("Test3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n[]\r\n]";
        
        String str = queue.toString();
        assertEquals(expected, str);

        queue.enqueue("Test");
        expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=\r\n[Test]\r\n]";
        str = queue.toString();
        assertEquals(expected, str);
    }
}