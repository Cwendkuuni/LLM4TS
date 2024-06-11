import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {
  
  @Test
  public void testIsEmpty() {
    // Test when queue is empty
    Queue queue = new Queue();
    assertTrue(queue.isEmpty());
    
    // Test when queue is not empty
    queue.enqueue("item1");
    assertFalse(queue.isEmpty());
  }
  
  @Test
  public void testEnqueue() {
    Queue queue = new Queue();
    
    // Test enqueue with null object
    queue.enqueue(null);
    assertTrue(queue.isEmpty());
    
    // Test enqueue with non-null object
    queue.enqueue("item1");
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    
    // Test dequeue when queue is empty
    assertNull(queue.dequeue());
    
    // Test dequeue when queue has only one item
    queue.enqueue("item1");
    assertEquals("item1", queue.dequeue());
    assertTrue(queue.isEmpty());
    
    // Test dequeue when queue has multiple items
    queue.enqueue("item1");
    queue.enqueue("item2");
    assertEquals("item1", queue.dequeue());
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testRemove() {
    Queue queue = new Queue();
    
    // Test remove with empty queue
    assertEquals(0, queue.remove("item1"));
    
    // Test remove with queue containing only the object to be removed
    queue.enqueue("item1");
    assertEquals(1, queue.remove("item1"));
    assertTrue(queue.isEmpty());
    
    // Test remove with queue containing multiple objects to be removed
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item1");
    assertEquals(2, queue.remove("item1"));
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    
    // Test refreshElement with empty queue
    queue.refreshElement("item1");
    assertTrue(queue.isEmpty());
    
    // Test refreshElement with queue containing the object to be refreshed
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.refreshElement("item1");
    assertFalse(queue.isEmpty());
    assertEquals(2, queue.getNumberItems());
    assertEquals("item1", queue.dequeue());
  }
  
  @Test
  public void testGetNumberItems() {
    Queue queue = new Queue();
    
    // Test getNumberItems with empty queue
    assertEquals(0, queue.getNumberItems());
    
    // Test getNumberItems with non-empty queue
    queue.enqueue("item1");
    assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testGetPeakNumberItems() {
    Queue queue = new Queue();
    
    // Test getPeakNumberItems with initial queue
    assertEquals(0, queue.getPeakNumberItems());
    
    // Test getPeakNumberItems after enqueueing multiple items
    queue.enqueue("item1");
    assertEquals(1, queue.getPeakNumberItems());
    queue.enqueue("item2");
    assertEquals(2, queue.getPeakNumberItems());
  }
  
  @Test
  public void testGetObjects() {
    Queue queue = new Queue();
    
    // Test getObjects with empty queue
    Vector expected = new Vector();
    assertEquals(expected, queue.getObjects());
    
    // Test getObjects with non-empty queue
    queue.enqueue("item1");
    expected.add("item1");
    assertEquals(expected, queue.getObjects());
  }
  
  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(2);
    
    // Test maxCapacityExceeded with initial queue
    assertFalse(queue.maxCapacityExceeded());
    
    // Test maxCapacityExceeded after enqueueing multiple items within capacity
    queue.enqueue("item1");
    assertFalse(queue.maxCapacityExceeded());
    queue.enqueue("item2");
    assertFalse(queue.maxCapacityExceeded());
    
    // Test maxCapacityExceeded after enqueueing additional item exceeding capacity
    queue.enqueue("item3");
    assertTrue(queue.maxCapacityExceeded());
  }
  
  @Test
  public void testToString() {
    Queue queue = new Queue();
    
    // Test toString with empty queue
    String expected = "Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
    assertEquals(expected, queue.toString());
    
    // Test toString with non-empty queue
    queue.enqueue("item1");
    expected = "Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item1]\r\n]";
    assertEquals(expected, queue.toString());
  }
}