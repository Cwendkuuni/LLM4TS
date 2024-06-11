import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue q = new Queue();
        assertTrue(q.isEmpty());
        
        q.enqueue("Test");
        assertFalse(q.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue q = new Queue();
        assertEquals(0, q.getNumberItems());
        
        q.enqueue("Test");
        assertEquals(1, q.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue q = new Queue();
        assertNull(q.dequeue());
        
        q.enqueue("Test1");
        q.enqueue("Test2");
        assertEquals("Test1", q.dequeue());
        assertEquals("Test2", q.dequeue());
        
        assertNull(q.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue q = new Queue();
        assertEquals(0, q.remove("Test"));
        
        q.enqueue("Test1");
        q.enqueue("Test2");
        q.enqueue("Test1");
        
        assertEquals(2, q.remove("Test1"));
        assertEquals(1, q.getNumberItems());
        
        assertEquals(1, q.remove("Test2"));
        assertEquals(0, q.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue q = new Queue();
        q.enqueue("Test1");
        q.enqueue("Test2");
        
        q.refreshElement("Test1");
        assertEquals("Test1", q.dequeue());
        
        q.refreshElement("Test2");
        assertEquals("Test2", q.dequeue());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue q = new Queue();
        assertEquals(0, q.getPeakNumberItems());
        
        q.enqueue("Test1");
        assertEquals(1, q.getPeakNumberItems());
        
        q.enqueue("Test2");
        assertEquals(2, q.getPeakNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue q = new Queue(2);
        assertFalse(q.maxCapacityExceeded());
        
        q.enqueue("Test1");
        q.enqueue("Test2");
        assertTrue(q.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue q = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", q.toString());
        
        q.enqueue("Test1");
        q.enqueue("Test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]", q.toString());
    }
}