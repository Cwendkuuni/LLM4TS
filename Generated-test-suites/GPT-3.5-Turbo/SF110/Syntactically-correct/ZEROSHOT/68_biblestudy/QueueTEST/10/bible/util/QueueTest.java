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
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(3, queue.getNumberItems());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.remove("item2"));
        assertEquals(0, queue.remove("item3"));
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.refreshElement("item1");
        
        Vector expected = new Vector();
        expected.addElement("item2");
        expected.addElement("item3");
        expected.addElement("item1");
        
        assertEquals(expected, queue.getObjects());
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
        Vector expected = new Vector();
        
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("item1");
        expected.addElement("item1");
        
        assertEquals(expected, queue.getObjects());
        
        queue.enqueue("item2");
        expected.addElement("item2");
        
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("item1");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item2");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item3");
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("item4");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("item1");
        
        assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item1]\r\n]", queue.toString());
        
        queue.enqueue("item2");
        
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]", queue.toString());
    }
}