import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {
  
  @Test
  public void testEmptyQueue() {
    Queue queue = new Queue();
    assertTrue(queue.isEmpty());
    assertEquals(0, queue.getNumberItems());
    assertEquals(0, queue.getPeakNumberItems());
    assertTrue(queue.getObjects().isEmpty());
  }
  
  @Test
  public void testEnqueue() {
    Queue queue = new Queue();
    Object obj1 = new Object();
    Object obj2 = new Object();
    
    queue.enqueue(obj1);
    assertEquals(1, queue.getNumberItems());
    assertEquals(1, queue.getPeakNumberItems());
    assertEquals(obj1, queue.getObjects().get(0));
    
    queue.enqueue(obj2);
    assertEquals(2, queue.getNumberItems());
    assertEquals(2, queue.getPeakNumberItems());
    assertTrue(queue.getObjects().contains(obj1));
    assertTrue(queue.getObjects().contains(obj2));
  }
  
  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    Object obj1 = new Object();
    
    // Dequeue from an empty queue
    assertNull(queue.dequeue());
    
    queue.enqueue(obj1);
    assertEquals(obj1, queue.dequeue());
  }
  
  @Test
  public void testRemove() {
    Queue queue = new Queue();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();
    
    // Remove from an empty queue
    assertEquals(0, queue.remove(obj1));
    
    queue.enqueue(obj1);
    queue.enqueue(obj2);
    queue.enqueue(obj3);
    
    // Remove non-existing object
    assertEquals(0, queue.remove(new Object()));
    assertEquals(3, queue.getNumberItems());
    
    // Remove existing object
    assertEquals(1, queue.remove(obj1));
    assertEquals(2, queue.getNumberItems());
    assertFalse(queue.getObjects().contains(obj1));
  }
  
  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    Object obj1 = new Object();
    Object obj2 = new Object();
    
    queue.enqueue(obj1);
    queue.enqueue(obj2);
    
    // Refresh non-existing object
    queue.refreshElement(new Object());
    assertEquals(2, queue.getNumberItems());
    
    // Refresh existing object
    queue.refreshElement(obj1);
    assertEquals(2, queue.getNumberItems());
    assertEquals(obj1, queue.getObjects().get(1));
  }
  
  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(2);
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();
    
    assertFalse(queue.maxCapacityExceeded());
    
    queue.enqueue(obj1);
    assertFalse(queue.maxCapacityExceeded());
    
    queue.enqueue(obj2);
    assertFalse(queue.maxCapacityExceeded());
    
    queue.enqueue(obj3);
    assertTrue(queue.maxCapacityExceeded());
  }
  
  @Test
  public void testToString() {
    Queue queue = new Queue();
    Object obj1 = new Object();
    Object obj2 = new Object();
    
    assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]", queue.toString());
    
    queue.enqueue(obj1);
    queue.enqueue(obj2);
    
    assertTrue(queue.toString().contains("numItems=2"));
    assertTrue(queue.toString().contains("maxNumItems=2"));
    assertTrue(queue.toString().contains("maxCapacity=-1"));
    assertTrue(queue.toString().contains("getObjects()="));
  }
}