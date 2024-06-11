import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setup() {
        queue = new Queue(3);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.getObjects().size());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("First");
        queue.enqueue("Second");
        Object removed = queue.dequeue();
        assertEquals("First", removed);
        assertEquals(1, queue.getObjects().size());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("First");
        int count = queue.remove("First");
        assertEquals(2, count);
        assertEquals(1, queue.getObjects().size());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.refreshElement("First");
        assertEquals(2, queue.getObjects().size());
        assertEquals("First", queue.getObjects().get(1));
    }
    
    @Test
    public void testGetNumberItems() {
        queue.enqueue("First");
        queue.enqueue("Second");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("First");
        queue.enqueue("Second");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("First");
        queue.enqueue("Second");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=3, getObjects()=[First, Second]\r\n]";
        assertEquals(expected, queue.toString());
    }
}