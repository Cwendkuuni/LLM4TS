import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

   @Test
   public void testIsEmpty_TrueScenario() {
      Queue queue = new Queue();
      assertTrue(queue.isEmpty());
   }
   
   @Test
   public void testIsEmpty_FalseScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertFalse(queue.isEmpty());
   }
   
   @Test
   public void testEnqueue() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.enqueue("Item2");
      assertEquals("Item1", queue.dequeue());
   }
   
   @Test
   public void testDequeue_EmptyScenario() {
      Queue queue = new Queue();
      assertNull(queue.dequeue());
   }
   
   @Test
   public void testDequeue_SingleItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertEquals("Item", queue.dequeue());
   }
   
   @Test
   public void testDequeue_MultipleItemsScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.enqueue("Item2");
      assertEquals("Item1", queue.dequeue());
      assertEquals("Item2", queue.dequeue());
   }
   
   @Test
   public void testRemove_ExistingItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.enqueue("Item2");
      assertEquals(1, queue.remove("Item1"));
   }
   
   @Test
   public void testRemove_NonExistingItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.enqueue("Item2");
      assertEquals(0, queue.remove("Item3"));
   }
   
   @Test
   public void testRemove_AllItemsScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.enqueue("Item2");
      assertEquals(2, queue.remove("Item1"));
      assertEquals(1, queue.remove("Item2"));
   }
   
   @Test
   public void testRefreshElement_ExistingItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.refreshElement("Item1");
      assertEquals("Item1", queue.dequeue());
   }
   
   @Test
   public void testRefreshElement_NonExistingItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item1");
      queue.refreshElement("Item2");
      assertEquals("Item1", queue.dequeue());
   }
   
   @Test
   public void testGetNumberItems_EmptyScenario() {
      Queue queue = new Queue();
      assertEquals(0, queue.getNumberItems());
   }
   
   @Test
   public void testGetNumberItems_SingleItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertEquals(1, queue.getNumberItems());
   }
   
   @Test
   public void testGetPeakNumberItems_EmptyScenario() {
      Queue queue = new Queue();
      assertEquals(0, queue.getPeakNumberItems());
   }
   
   @Test
   public void testGetPeakNumberItems_SingleItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertEquals(1, queue.getPeakNumberItems());
   }
   
   @Test
   public void testGetObjects_EmptyScenario() {
      Queue queue = new Queue();
      assertEquals(0, queue.getObjects().size());
   }
   
   @Test
   public void testGetObjects_SingleItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertEquals(1, queue.getObjects().size());
   }
   
   @Test
   public void testMaxCapacityExceeded_TrueLimitScenario() {
      Queue queue = new Queue(1);
      queue.enqueue("Item");
      assertTrue(queue.maxCapacityExceeded());
   }
   
   @Test
   public void testMaxCapacityExceeded_FalseLimitScenario() {
      Queue queue = new Queue(1);
      assertFalse(queue.maxCapacityExceeded());
   }
   
   @Test
   public void testMaxCapacityExceeded_NegativeLimitScenario() {
      Queue queue = new Queue(-1);
      assertFalse(queue.maxCapacityExceeded());
   }
   
   @Test
   public void testToString_EmptyScenario() {
      Queue queue = new Queue();
      assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n]", queue.toString());
   }
   
   @Test
   public void testToString_SingleItemScenario() {
      Queue queue = new Queue();
      queue.enqueue("Item");
      assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Item]\r\n]", queue.toString());
   }
}