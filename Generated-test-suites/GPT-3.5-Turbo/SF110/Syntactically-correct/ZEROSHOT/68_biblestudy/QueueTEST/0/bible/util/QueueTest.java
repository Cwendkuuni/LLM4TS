package bible.util;

import org.junit.Assert;
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
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(1);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Assert.assertEquals(3, queue.getNumberItems());
        Vector expected = new Vector();
        expected.addElement(1);
        expected.addElement(2);
        expected.addElement(3);
        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(2, queue.getNumberItems());
        Vector expected = new Vector();
        expected.addElement(2);
        expected.addElement(3);
        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testRemove() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(1);
        Assert.assertEquals(2, queue.remove(1));
        Assert.assertEquals(1, queue.getNumberItems());
        Vector expected = new Vector();
        expected.addElement(2);
        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.refreshElement(2);
        Assert.assertEquals(3, queue.getNumberItems());
        Vector expected = new Vector();
        expected.addElement(1);
        expected.addElement(3);
        expected.addElement(2);
        Assert.assertEquals(expected, queue.getObjects());
        queue.refreshElement(4);
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testGetNumberItems() {
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Assert.assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Assert.assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Assert.assertEquals(3, queue.getPeakNumberItems());
        queue.enqueue(4);
        Assert.assertEquals(4, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Vector expected = new Vector();
        expected.addElement(1);
        expected.addElement(2);
        expected.addElement(3);
        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue(1);
        queue.enqueue(2);
        Assert.assertFalse(queue.maxCapacityExceeded());
        queue.enqueue(3);
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Assert.assertEquals("bible.util.Queue:[numItems=3, maxNumItems=0, maxCapacity=-1, getObjects()=[1, 2, 3]\r\n]", queue.toString());
    }
}