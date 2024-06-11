package bible.util;
import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue("item2");
        
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getObjects().size());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithMaxCapacity = new Queue(1);
        queueWithMaxCapacity.enqueue("item1");
        assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    }
}