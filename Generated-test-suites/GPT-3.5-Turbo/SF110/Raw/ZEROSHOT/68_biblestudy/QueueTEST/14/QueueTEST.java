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

        Vector expected = new Vector();
        expected.add(1);
        expected.add(2);
        expected.add(3);

        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(2, queue.dequeue());
        Assert.assertEquals(3, queue.dequeue());
    }

    @Test
    public void testRemove() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);

        Assert.assertEquals(2, queue.remove(1));

        Vector expected = new Vector();
        expected.add(2);
        expected.add(3);

        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        queue.refreshElement(2);

        Vector expected = new Vector();
        expected.add(1);
        expected.add(3);
        expected.add(2);

        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Vector expected = new Vector();
        expected.add(1);
        expected.add(2);
        expected.add(3);

        Assert.assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);

        queue.enqueue(1);
        queue.enqueue(2);

        Assert.assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();

        Assert.assertFalse(queue.maxCapacityExceeded());
    }
}