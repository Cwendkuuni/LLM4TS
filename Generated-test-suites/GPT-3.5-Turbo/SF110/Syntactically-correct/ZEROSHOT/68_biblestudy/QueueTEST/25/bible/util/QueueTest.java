package bible.util;

import org.junit.Assert;
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
        Assert.assertTrue(queue.isEmpty());   
        queue.enqueue("item");
        Assert.assertFalse(queue.isEmpty());      
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("item2");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Vector objects = new Vector();
        objects.add("item1");
        objects.add("item2");
        Assert.assertEquals(objects, queue.getObjects());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Assert.assertEquals("item1", queue.dequeue());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals("item2", queue.dequeue());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        Assert.assertEquals(2, queue.remove("item1"));
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        Vector objects = new Vector();
        objects.add("item2");
        objects.add("item1");
        Assert.assertEquals(objects, queue.getObjects());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Vector objects = new Vector();
        objects.add("item2");
        objects.add("item1");
        Assert.assertEquals(objects, queue.getObjects());
    }
    
    @Test
    public void testGetNumberItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        Assert.assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        queue.dequeue();
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Vector objects = new Vector();
        objects.add("item1");
        objects.add("item2");
        Assert.assertEquals(objects, queue.getObjects());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        Assert.assertTrue(queue.maxCapacityExceeded());
        queue.enqueue("item3");
        Assert.assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        Assert.assertEquals(expected, queue.toString());
    }
}