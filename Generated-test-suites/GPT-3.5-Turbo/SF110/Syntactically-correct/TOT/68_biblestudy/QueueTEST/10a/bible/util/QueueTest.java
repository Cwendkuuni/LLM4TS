import bible.util.Queue;
import org.junit.Before;
import org.junit.Test;
import java.util.Vector;
import static org.junit.Assert.*;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("First Element");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 3");
        
        assertEquals("Element 1", queue.dequeue());
        assertEquals("Element 2", queue.dequeue());
        
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Non-existent Element"));
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        queue.enqueue("Element 1");
        
        assertEquals(2, queue.remove("Element 1"));
        
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        queue.refreshElement("Element 1");
        
        Vector expectedVector = new Vector();
        expectedVector.addElement("Element 2");
        expectedVector.addElement("Element 1");
        
        assertEquals(expectedVector, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.enqueue("Element 3");
        queue.dequeue();
        
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Vector expectedVector = new Vector();
        
        assertEquals(expectedVector, queue.getObjects());
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        expectedVector.addElement("Element 1");
        expectedVector.addElement("Element 2");
        
        assertEquals(expectedVector, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("Element 1");
        queue.enqueue("Element 2");
        
        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Element 1, Element 2]\r\n]";
        
        assertEquals(expectedString, queue.toString());
    }
}