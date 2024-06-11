import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {

   @Test
   public void testDiff() {
      RelativeString result = StringIncrementor.diff("abc", "def");
      assertEquals(result.getLinksCount(), 0);
      assertEquals(result.getAbsolute(), "def");

      result = StringIncrementor.diff("abcdefg", "def");
      assertEquals(result.getLinksCount(), 1);
      assertEquals(result.getLink(0).getPosOrig(), 0);
      assertEquals(result.getLink(0).getPosNew(), 0);
      assertEquals(result.getLink(0).getLen(), 3);
      assertEquals(result.getAbsolute(), "g");

      result = StringIncrementor.diff("aaa", "aaa");
      assertEquals(result.getLinksCount(), 0);
      assertEquals(result.getAbsolute(), "");

   }

   @Test
   public void testDiffVerbose() {
      RelativeString result = StringIncrementor.diff("abc", "def", true);
      assertEquals(result.getLinksCount(), 0);
      assertEquals(result.getAbsolute(), "def");

      result = StringIncrementor.diff("abcdefg", "def", true);
      assertEquals(result.getLinksCount(), 1);
      assertEquals(result.getLink(0).getPosOrig(), 0);
      assertEquals(result.getLink(0).getPosNew(), 0);
      assertEquals(result.getLink(0).getLen(), 3);
      assertEquals(result.getAbsolute(), "g");

      result = StringIncrementor.diff("aaa", "aaa", true);
      assertEquals(result.getLinksCount(), 0);
      assertEquals(result.getAbsolute(), "");

   }

   @Test
   public void testPatch() {
      String result = StringIncrementor.patch("abc", new RelativeString());
      assertEquals(result, "abc");

      StringIncrementor.patch("abcdefg", new RelativeString());
      assertEquals(result, "abcdefg");

      StringIncrementor.patch("abcdefg", new RelativeString("def", 0, 0, 3));
      assertEquals(result, "def");

      StringIncrementor.patch("abc", new RelativeString(), true);
      assertEquals(result, "abc");

   }

   @Test
   public void testPatchVerbose() {
      String result = StringIncrementor.patch("abc", new RelativeString());
      assertEquals(result, "abc");

      StringIncrementor.patch("abcdefg", new RelativeString());
      assertEquals(result, "abcdefg");

      StringIncrementor.patch("abcdefg", new RelativeString("def", 0, 0, 3));
      assertEquals(result, "def");

      StringIncrementor.patch("abc", new RelativeString(), true);
      assertEquals(result, "abc");

   }
}