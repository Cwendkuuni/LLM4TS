package bible.util;

import org.junit.*;
import java.util.*;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        Assert.assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("Object 1");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(1, queue.getNumberItems());
        
        queue.enqueue("Object 2");
        Assert.assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        
        Object obj1 = queue.dequeue();
        Assert.assertEquals("Object 1", obj1);
        Assert.assertEquals(1, queue.getNumberItems());
        
        Object obj2 = queue.dequeue();
        Assert.assertEquals("Object 2", obj2);
        Assert.assertEquals(0, queue.getNumberItems());
        
        Object obj3 = queue.dequeue();
        Assert.assertNull(obj3);
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 1");
        queue.enqueue("Object 3");
        
        int removedCount = queue.remove("Object 1");
        Assert.assertEquals(2, removedCount);
        Assert.assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        
        queue.refreshElement("Object 1");
        Assert.assertEquals(2, queue.getNumberItems());
        
        queue.refreshElement("Object 3");
        Assert.assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");
        
        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertTrue(objects.contains("Object 1"));
        Assert.assertTrue(objects.contains("Object 2"));
        Assert.assertTrue(objects.contains("Object 3"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        
        Assert.assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        queue.enqueue("Object 3");
        Assert.assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Object 1, Object 2]\r\n]";
        Assert.assertEquals(expected, queue.toString());
    }
    
    @After
    public void tearDown() {
        queue = null;
    }
}