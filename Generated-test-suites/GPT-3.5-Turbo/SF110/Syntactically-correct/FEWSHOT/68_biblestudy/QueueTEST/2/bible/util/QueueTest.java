package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    private Queue queue;
    
    @Before
    public void setup() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        Object obj = new Object();
        queue.enqueue(obj);
        
        assertEquals(obj, queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        assertEquals(2, queue.remove(obj2));
    }
    
    @Test
    public void testRefreshElement() {
        Object obj = new Object();
        
        queue.enqueue(obj);
        queue.refreshElement(obj);
        
        assertEquals(obj, queue.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        assertEquals(3, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        Vector objects = queue.getObjects();
        
        assertEquals(3, objects.size());
        assertTrue(objects.contains(obj1));
        assertTrue(objects.contains(obj2));
        assertTrue(objects.contains(obj3));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queueWithCapacity = new Queue(2);
        
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queueWithCapacity.enqueue(obj1);
        queueWithCapacity.enqueue(obj2);
        
        assertTrue(queueWithCapacity.maxCapacityExceeded());
        queueWithCapacity.enqueue(obj3);
        assertTrue(queueWithCapacity.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        String expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=" + queue.getObjects() + "\r\n]";
        
        assertEquals(expected, queue.toString());
    }
}