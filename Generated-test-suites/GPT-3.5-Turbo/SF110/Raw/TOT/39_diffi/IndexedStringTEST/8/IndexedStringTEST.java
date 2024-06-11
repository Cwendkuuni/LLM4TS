import de.beiri22.stringincrementor.helper.IndexedString;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

   @Test
   public void testCount_ValidInput_ReturnsCorrectCount() {
      IndexedString indexedString = new IndexedString("Hello World");
      int count = indexedString.count('o');
      assertEquals(2, count);
   }

   @Test
   public void testCount_ZeroCount_ReturnsZero() {
      IndexedString indexedString = new IndexedString("");
      int count = indexedString.count('o');
      assertEquals(0, count);
   }

   @Test
   public void testCount_AllCharacters_ReturnsTotalLength() {
      IndexedString indexedString = new IndexedString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
      int count = indexedString.count('a');
      assertEquals(2, count);
   }

   @Test
   public void testIndexOf_ValidInput_ReturnsCorrectIndex() {
      IndexedString indexedString = new IndexedString("Hello World");
      int index = indexedString.indexOf(new char[]{'o', 'r', 'l', 'd'});
      assertEquals(7, index);
   }

   @Test
   public void testIndexOf_InvalidInput_ReturnsNegativeOne() {
      IndexedString indexedString = new IndexedString("Hello World");
      int index = indexedString.indexOf(new char[]{'x', 'y', 'z'});
      assertEquals(-1, index);
   }

   @Test
   public void testIndexOf_EmptyInput_ReturnsNegativeOne() {
      IndexedString indexedString = new IndexedString("Hello World");
      int index = indexedString.indexOf(new char[]{});
      assertEquals(-1, index);
   }
}