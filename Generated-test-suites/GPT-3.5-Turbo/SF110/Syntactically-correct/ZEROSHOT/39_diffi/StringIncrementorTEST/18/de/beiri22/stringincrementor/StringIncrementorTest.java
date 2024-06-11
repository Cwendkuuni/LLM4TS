import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

   @Test
   public void testDiff() {
      RelativeString result = StringIncrementor.diff("abc", "abc");
      assertEquals(result.getLength(), 0);
      assertEquals(result.linksCount(), 0);

      result = StringIncrementor.diff("abc", "def");
      assertEquals(result.getLength(), 6);
      assertEquals(result.linksCount(), 0);

      result = StringIncrementor.diff("abc", "abccba");
      assertEquals(result.getLength(), 3);
      assertEquals(result.linksCount(), 1);

      result = StringIncrementor.diff("abc", "abadbcdcefbgb");
      assertEquals(result.getLength(), 8);
      assertEquals(result.linksCount(), 1);
   }

   @Test
   public void testDiffVerbose() {
      RelativeString result = StringIncrementor.diff("abc", "abc", true);
      assertEquals(result.getLength(), 0);
      assertEquals(result.linksCount(), 0);

      result = StringIncrementor.diff("abc", "def", true);
      assertEquals(result.getLength(), 6);
      assertEquals(result.linksCount(), 0);

      result = StringIncrementor.diff("abc", "abccba", true);
      assertEquals(result.getLength(), 3);
      assertEquals(result.linksCount(), 1);

      result = StringIncrementor.diff("abc", "abadbcdcefbgb", true);
      assertEquals(result.getLength(), 8);
      assertEquals(result.linksCount(), 1);
   }

   @Test
   public void testPatch() {
      String result = StringIncrementor.patch("abc", new RelativeString());
      assertEquals(result, "abc");

      RelativeString rs = new RelativeString();
      StringLink sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      result = StringIncrementor.patch("abc", rs);
      assertEquals(result, "abc");

      rs = new RelativeString();
      sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      rs.setAbsolute("defg");
      result = StringIncrementor.patch("abc", rs);
      assertEquals(result, "abcdefg");

      rs = new RelativeString();
      sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      sl = new StringLink(0, 4, 2);
      rs.addLink(sl);
      rs.setAbsolute("defgh");
      result = StringIncrementor.patch("abc", rs);
      assertEquals(result, "abcdefgh");

      rs = new RelativeString();
      rs.setAbsolute("defgh");
      result = StringIncrementor.patch("abc", rs);
      assertEquals(result, "abcdefgh");
   }

   @Test
   public void testPatchVerbose() {
      String result = StringIncrementor.patch("abc", new RelativeString(), true);
      assertEquals(result, "abc");

      RelativeString rs = new RelativeString();
      StringLink sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      result = StringIncrementor.patch("abc", rs, true);
      assertEquals(result, "abc");

      rs = new RelativeString();
      sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      rs.setAbsolute("defg");
      result = StringIncrementor.patch("abc", rs, true);
      assertEquals(result, "abcdefg");

      rs = new RelativeString();
      sl = new StringLink(0, 0, 3);
      rs.addLink(sl);
      sl = new StringLink(0, 4, 2);
      rs.addLink(sl);
      rs.setAbsolute("defgh");
      result = StringIncrementor.patch("abc", rs, true);
      assertEquals(result, "abcdefgh");

      rs = new RelativeString();
      rs.setAbsolute("defgh");
      result = StringIncrementor.patch("abc", rs, true);
      assertEquals(result, "abcdefgh");
   }
}