import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

   @Test
   public void testArrayByteList() {
       ArrayByteList list = new ArrayByteList();
       assertEquals(0, list.size());
   }

   @Test
   public void testArrayByteListWithInitialCapacity() {
       ArrayByteList list = new ArrayByteList(10);
       assertEquals(0, list.size());
   }

   @Test
   public void testAddElement() {
       ArrayByteList list = new ArrayByteList();
       list.add(0, (byte) 5);
       assertEquals(1, list.size());
       assertEquals(5, list.get(0));
   }

   @Test
   public void testRemoveElement() {
       ArrayByteList list = new ArrayByteList();
       list.add(0, (byte) 5);
       assertEquals(1, list.size());
       byte removed = list.removeElementAt(0);
       assertEquals(0, list.size());
       assertEquals(5, removed);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testAddWithInvalidIndex() {
       ArrayByteList list = new ArrayByteList();
       list.add(1, (byte) 5);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testRemoveWithInvalidIndex() {
       ArrayByteList list = new ArrayByteList();
       list.removeElementAt(0);
   }
}