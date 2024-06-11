package bible.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue(1);
        Assert.assertFalse(queue.isEmpty());

        queue.dequeue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals(1, objects.get(0));
        Assert.assertEquals(2, objects.get(1));
        Assert.assertEquals(3, objects.get(2));
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(2, queue.dequeue());
        Assert.assertEquals(3, queue.dequeue());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(2);
        queue.enqueue(4);

        int numItemsRemoved = queue.remove(2);
        Assert.assertEquals(2, numItemsRemoved);

        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals(1, objects.get(0));
        Assert.assertEquals(3, objects.get(1));
        Assert.assertEquals(4, objects.get(2));
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        queue.refreshElement(2);

        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals(1, objects.get(0));
        Assert.assertEquals(3, objects.get(1));
        Assert.assertEquals(2, objects.get(2));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue(1);
        queue.enqueue(2);
        Assert.assertEquals(2, queue.getNumberItems());

        queue.dequeue();
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue(1);
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue(2);
        Assert.assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals(1, objects.get(0));
        Assert.assertEquals(2, objects.get(1));
        Assert.assertEquals(3, objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(1);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(2);
        Assert.assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[1, 2]\r\n]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}