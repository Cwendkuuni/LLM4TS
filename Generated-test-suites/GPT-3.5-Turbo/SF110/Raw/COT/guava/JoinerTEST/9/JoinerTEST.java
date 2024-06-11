import org.junit.*;
import static org.junit.Assert.*;

public class JoinerTest {

   @Test
   public void testOn() {
      Joiner joiner = Joiner.on(",");
      assertEquals(joiner.separator, ",");
   }

   @Test
   public void testJoin() {
      Joiner joiner = Joiner.on(",");
      String result = joiner.join("one", "two", "three");
      assertEquals(result, "one,two,three");
   }

   @Test
   public void testJoinWithIterable() {
      Joiner joiner = Joiner.on(",");
      String result = joiner.join(Arrays.asList("one", "two", "three"));
      assertEquals(result, "one,two,three");
   }

   @Test
   public void testJoinWithArray() {
      Joiner joiner = Joiner.on(",");
      String[] parts = new String[] { "one", "two", "three" };
      String result = joiner.join(parts);
      assertEquals(result, "one,two,three");
   }
}