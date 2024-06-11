
import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        
        queue.enqueue("Test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        Assert.assertEquals(1, queue.getNumberItems());
        
        queue.enqueue(null);
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        Object obj = queue.dequeue();
        Assert.assertEquals("Test", obj);
        Assert.assertEquals(1, queue.getNumberItems());
        
        obj = queue.dequeue();
        Assert.assertEquals("Test2", obj);
        Assert.assertEquals(0, queue.getNumberItems());
        
        obj = queue.dequeue();
        Assert.assertNull(obj);
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        int numRemoved = queue.remove("Test");
        Assert.assertEquals(1, numRemoved);
        Assert.assertEquals(2, queue.getNumberItems());
        
        numRemoved = queue.remove("Test2");
        Assert.assertEquals(1, numRemoved);
        Assert.assertEquals(1, queue.getNumberItems());
        
        numRemoved = queue.remove("Test3");
        Assert.assertEquals(1, numRemoved);
        Assert.assertEquals(0, queue.getNumberItems());
        
        numRemoved = queue.remove("NonExisting");
        Assert.assertEquals(0, numRemoved);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        queue.refreshElement("Test2");
        
        Assert.assertEquals(3, queue.getNumberItems());
        Vector objects = queue.getObjects();
        Assert.assertTrue(objects.contains("Test2"));
        Assert.assertEquals("Test2", objects.lastElement());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        Assert.assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Test");
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        Assert.assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Test");
        Assert.assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Test2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains("Test"));
        Assert.assertTrue(objects.contains("Test2"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        Assert.assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        String expected = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test, Test2]\r\n]";
        Assert.assertEquals(expected, queue.toString());
    }
}
