import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

public class IndexedStringTest {

    private IndexedString indexedString;

   @Before
   public void setUp() {
      indexedString = new IndexedString("test");
   }

   @Test
   public void testCount() {
       int count = indexedString.count('t');
       assertEquals(2, count);
   }

   @Test(expected = ArrayIndexOutOfBoundsException.class)
   public void testEmptyInputStringConstructor() {
       new IndexedString("");
   }

   @Test
   public void testEmptyTargetArray() {
       int index = indexedString.indexOf(new char[]{});
       assertEquals(-1, index);
   }

}