package bible.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void isEmpty_WhenQueueIsEmpty_ReturnsTrue() {
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void enqueue_WhenObjectNotNull_EnqueuesObject() {
        Object obj = "Test";
        queue.enqueue(obj);
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void dequeue_WhenQueueIsNotEmpty_ReturnsDequeuedObject() {
        Object obj = "Test";
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        assertEquals(obj, dequeuedObj);
    }
    
    @Test
    public void dequeue_WhenQueueIsEmpty_ReturnsNull() {
        assertNull(queue.dequeue());
    }
    
    @Test
    public void remove_WhenObjectInQueue_RemovesObjectFromQueue() {
        Object obj = "Test";
        queue.enqueue(obj);
        int removedItems = queue.remove(obj);
        assertEquals(1, removedItems);
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void remove_WhenObjectNotInQueue_ReturnsZero() {
        Object obj = "Test";
        int removedItems = queue.remove(obj);
        assertEquals(0, removedItems);
    }
    
    @Test
    public void refreshElement_WhenObjectInQueue_MovesObjectToEndOfQueue() {
        Object obj1 = "Test1";
        Object obj2 = "Test2";
        Object obj3 = "Test3";
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        queue.refreshElement(obj2);
        
        Vector expectedObjects = new Vector();
        expectedObjects.add(obj1);
        expectedObjects.add(obj3);
        expectedObjects.add(obj2);
        
        Vector actualObjects = queue.getObjects();
        
        assertEquals(expectedObjects, actualObjects);
    }
    
    @Test
    public void getNumberItems_WhenQueueIsEmpty_ReturnsZero() {
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void getPeakNumberItems_WhenQueueIsEmpty_ReturnsZero() {
        assertEquals(0, queue.getPeakNumberItems());
    }
    
    @Test
    public void getObjects_WhenQueueIsEmpty_ReturnsEmptyVector() {
        Vector expectedObjects = new Vector();
        Vector actualObjects = queue.getObjects();
        
        assertEquals(expectedObjects, actualObjects);
    }
    
    @Test
    public void maxCapacityExceeded_WhenQueueIsBelowMaxCapacity_ReturnsFalse() {
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void maxCapacityExceeded_WhenQueueIsAboveMaxCapacity_ReturnsTrue() {
        queue = new Queue(2);
        Object obj1 = "Test1";
        Object obj2 = "Test2";
        Object obj3 = "Test3";
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void toString_ReturnsCorrectStringRepresentation() {
        Object obj1 = "Test1";
        Object obj2 = "Test2";
        
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        
        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]";
        String actualString = queue.toString();
        
        assertEquals(expectedString, actualString);
    }
}