
import org.junit.*;
import static org.junit.Assert.*;

public class MetaphoneTest {

   @Test
   public void testMetaphone() {
      Metaphone m = new Metaphone();
      assertEquals("MTFN", m.metaphone("Metaphone"));
   }

   @Test
   public void testEncode() {
      Metaphone m = new Metaphone();
      assertEquals("MTFN", m.encode("Metaphone"));
   }

   @Test
   public void testIsMetaphoneEqual() {
      Metaphone m = new Metaphone();
      assertTrue(m.isMetaphoneEqual("Metaphone", "MATPHON"));
      assertFalse(m.isMetaphoneEqual("Metaphone", "Codex"));
   }

   @Test(expected = EncoderException.class)
   public void testEncodeInvalidInput() throws EncoderException {
      Metaphone m = new Metaphone();
      m.encode(new Integer(10));
   }

}
