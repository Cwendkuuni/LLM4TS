
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

   @Test
   public void testIsEmpty() {
      Queue queue = new Queue();
      assertTrue(queue.isEmpty());
   }
   
   @Test
   public void testEnqueue() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      assertEquals(queue.getNumberItems(), 2);
   }
   
   @Test
   public void testDequeue() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      Object dequeued = queue.dequeue();
      assertEquals(dequeued, "A");
      assertEquals(queue.getNumberItems(), 1);
   }
   
   @Test
   public void testRemove() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      queue.enqueue("A");
      int removedNumItems = queue.remove("A");
      assertEquals(removedNumItems, 2);
      assertEquals(queue.getNumberItems(), 1);
   }
   
   @Test
   public void testRefreshElement() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      queue.refreshElement("A");
      assertEquals(queue.getNumberItems(), 2);
   }
   
   @Test
   public void testGetNumberItems() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      assertEquals(queue.getNumberItems(), 2);
   }
   
   @Test
   public void testGetPeakNumberItems() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      assertEquals(queue.getPeakNumberItems(), 2);
   }
   
   @Test
   public void testGetObjects() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      Vector objects = queue.getObjects();
      assertEquals(objects.size(), 2);
      assertTrue(objects.contains("A"));
      assertTrue(objects.contains("B"));
   }
   
   @Test
   public void testMaxCapacityExceeded() {
      Queue queue = new Queue(2);
      queue.enqueue("A");
      queue.enqueue("B");
      assertTrue(queue.maxCapacityExceeded());
   }
   
   @Test
   public void testToString() {
      Queue queue = new Queue();
      queue.enqueue("A");
      queue.enqueue("B");
      String expected = "Queue:[numItems=2, maxNumItems=0, maxCapacity=-1, getObjects()=[A, B]\r\n]";
      assertEquals(queue.toString(), expected);
   }
}
