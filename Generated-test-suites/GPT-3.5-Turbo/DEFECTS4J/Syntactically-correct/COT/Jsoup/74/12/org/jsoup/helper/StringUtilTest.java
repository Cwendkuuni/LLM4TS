import org.junit.Test;
import org.junit.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtilTest {

   @Test
   public void testJoinCollection() {
      Collection<String> strings = Arrays.asList("one", "two", "three");
      String sep = ",";
      String result = StringUtil.join(strings, sep);
      String expected = "one,two,three";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testJoinIterator() {
      Iterator<String> strings = Arrays.asList("one", "two", "three").iterator();
      String sep = ",";
      String result = StringUtil.join(strings, sep);
      String expected = "one,two,three";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testJoinArray() {
      String[] strings = {"one", "two", "three"};
      String sep = ",";
      String result = StringUtil.join(strings, sep);
      String expected = "one,two,three";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testPadding() {
      int width = 5;
      String result = StringUtil.padding(width);
      String expected = "     ";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testIsBlank() {
      String string = "";
      boolean result = StringUtil.isBlank(string);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testIsNumeric() {
      String string = "123";
      boolean result = StringUtil.isNumeric(string);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testIsWhitespace() {
      int c = ' ';
      boolean result = StringUtil.isWhitespace(c);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testIsActuallyWhitespace() {
      int c = ' ';
      boolean result = StringUtil.isActuallyWhitespace(c);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testNormaliseWhitespace() {
      String string = "  one  two  three  ";
      String result = StringUtil.normaliseWhitespace(string);
      String expected = "one two three";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testAppendNormalisedWhitespace() {
      StringBuilder accum = new StringBuilder();
      String string = "  one  two  three  ";
      StringUtil.appendNormalisedWhitespace(accum, string, true);
      String result = accum.toString();
      String expected = "one two three";
      Assert.assertEquals(expected, result);
   }
   
   @Test
   public void testIn() {
      String needle = "one";
      String[] haystack = {"one", "two", "three"};
      boolean result = StringUtil.in(needle, haystack);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testInSorted() {
      String needle = "one";
      String[] haystack = {"one", "two", "three"};
      boolean result = StringUtil.inSorted(needle, haystack);
      Assert.assertTrue(result);
   }
   
   @Test
   public void testResolve() {
      try {
         URL base = new URL("http://www.example.com");
         String relUrl = "/path/file";
         URL result = StringUtil.resolve(base, relUrl);
         URL expected = new URL("http://www.example.com/path/file");
         Assert.assertEquals(expected, result);
      } catch (MalformedURLException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void testResolveWithString() {
      String baseUrl = "http://www.example.com";
      String relUrl = "/path/file";
      String result = StringUtil.resolve(baseUrl, relUrl);
      String expected = "http://www.example.com/path/file";
      Assert.assertEquals(expected, result);
   }

}