import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

  @Test
  public void testIsEmpty() {
    // Test when queue is empty
    Queue queue = new Queue();
    assertTrue(queue.isEmpty());

    // Test when queue is not empty
    queue.enqueue("item");
    assertFalse(queue.isEmpty());
  }

  @Test
  public void testEnqueue() {
    Queue queue = new Queue();

    // Test enqueue 1 item
    queue.enqueue("item");
    assertFalse(queue.isEmpty());

    // Test enqueue multiple items
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item3");
    assertFalse(queue.isEmpty());
  }

  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item3");

    // Test dequeue an item
    Object dequeuedItem = queue.dequeue();
    assertEquals("item1", dequeuedItem);

    // Test dequeue multiple items
    dequeuedItem = queue.dequeue();
    assertEquals("item2", dequeuedItem);
    dequeuedItem = queue.dequeue();
    assertEquals("item3", dequeuedItem);

    // Test dequeue from an empty queue
    dequeuedItem = queue.dequeue();
    assertNull(dequeuedItem);
  }

  @Test
  public void testRemove() {
    Queue queue = new Queue();
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item3");

    // Test remove an item that exists in the queue
    int removedCount = queue.remove("item2");
    assertEquals(1, removedCount);
    assertFalse(queue.getObjects().contains("item2"));

    // Test remove an item that doesn't exist in the queue
    removedCount = queue.remove("item4");
    assertEquals(0, removedCount);
  }

  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item3");

    // Test refresh an existing item
    queue.refreshElement("item2");
    assertTrue(queue.getObjects().contains("item2"));

    // Test refresh a non-existing item
    queue.refreshElement("item4");
    assertTrue(queue.getObjects().contains("item4"));
  }

  @Test
  public void testGetNumberItems() {
    Queue queue = new Queue();
    assertEquals(0, queue.getNumberItems());

    queue.enqueue("item1");
    assertEquals(1, queue.getNumberItems());

    queue.enqueue("item2");
    queue.enqueue("item3");
    assertEquals(3, queue.getNumberItems());

    queue.dequeue();
    assertEquals(2, queue.getNumberItems());
  }

  @Test
  public void testGetPeakNumberItems() {
    Queue queue = new Queue();
    assertEquals(0, queue.getPeakNumberItems());

    queue.enqueue("item1");
    assertEquals(1, queue.getPeakNumberItems());

    queue.enqueue("item2");
    queue.enqueue("item3");
    assertEquals(3, queue.getPeakNumberItems());

    queue.dequeue();
    assertEquals(3, queue.getPeakNumberItems());

    queue.dequeue();
    assertEquals(3, queue.getPeakNumberItems());
  }

  @Test
  public void testGetObjects() {
    Queue queue = new Queue();
    queue.enqueue("item1");
    queue.enqueue("item2");
    queue.enqueue("item3");

    // Test getObjects method
    Vector vector = queue.getObjects();
    assertTrue(vector.contains("item1"));
    assertTrue(vector.contains("item2"));
    assertTrue(vector.contains("item3"));
  }

  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(2);
    queue.enqueue("item1");
    queue.enqueue("item2");

    assertFalse(queue.maxCapacityExceeded());

    queue.enqueue("item3");
    assertTrue(queue.maxCapacityExceeded());

    Queue queueNoMaxCapacity = new Queue();
    assertFalse(queueNoMaxCapacity.maxCapacityExceeded());
  }

  @Test
  public void testToString() {
    Queue queue = new Queue();
    assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

    queue.enqueue("item1");
    assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[item1]\r\n]", queue.toString());

    queue.enqueue("item2");
    queue.enqueue("item3");
    assertEquals("bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[item1, item2, item3]\r\n]", queue.toString());
  }
}