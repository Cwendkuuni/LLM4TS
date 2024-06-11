import static org.junit.Assert.*;
import org.junit.*;
import java.util.Vector;
import bible.util.Queue;

public class QueueTest {
    
    Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue(3);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        queue.enqueue("Item 4");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertEquals("Item 1", queue.dequeue());
        assertEquals("Item 2", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Item 1"));
        queue.enqueue("Item 1");
        assertEquals(1, queue.remove("Item 1"));
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        assertEquals(2, queue.remove("Item 1"));
        assertEquals(0, queue.remove("Item 4"));
    }
    
    @Test
    public void testRefreshElement() {
        queue.refreshElement("Item 1");
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        queue.refreshElement("Item 2");
        Vector expected = new Vector();
        expected.addElement("Item 1");
        expected.addElement("Item 3");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Item 1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("Item 2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Item 1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Item 2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.enqueue("Item 3");
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
        queue.enqueue("Item 1");
        expected.addElement("Item 1");
        assertEquals(expected, queue.getObjects());
        queue.enqueue("Item 2");
        expected.addElement("Item 2");
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=3, getObjects()=\r\n]";
        assertEquals(expected, queue.toString());
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");
        expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()= [Item 1, Item 2, Item 3]\r\n]";
        assertEquals(expected, queue.toString());
    }
}