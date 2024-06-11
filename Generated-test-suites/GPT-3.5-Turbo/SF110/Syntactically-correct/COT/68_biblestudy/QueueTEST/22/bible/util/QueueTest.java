
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setup() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue(5);
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue(10);
        queue.enqueue(20);
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue(15);
        assertEquals(15, queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(10);
        assertEquals(2, queue.remove(10));
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue(10);
        queue.enqueue(20);
        queue.refreshElement(10);
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue(30);
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue(40);
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue(50);
        queue.enqueue(60);
        Vector<Integer> expected = new Vector<Integer>();
        expected.addElement(50);
        expected.addElement(60);
        assertEquals(expected, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue(70);
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue(80);
        assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[80]\r\n]", queue.toString());
    }
}
