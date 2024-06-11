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
        assertEquals(true, queue.isEmpty());
        queue.enqueue("test");
        assertEquals(false, queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("test");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("test1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test1");
        
        assertEquals(2, queue.remove("test1"));
        assertEquals(1, queue.getNumberItems());
        
        assertEquals(1, queue.remove("test2"));
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        
        queue.refreshElement("test1");
        Vector testVector = queue.getObjects();
        
        assertEquals(2, testVector.size());
        assertEquals("test2", testVector.get(0));
        assertEquals("test1", testVector.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]", queue.toString());
    }
}