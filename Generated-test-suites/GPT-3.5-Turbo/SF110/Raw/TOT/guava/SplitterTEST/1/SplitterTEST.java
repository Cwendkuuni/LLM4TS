import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

public class SplitterTest {

   private final Splitter splitter = new Splitter((Splitter.Strategy) null);

   @Test
   public void testOnChar() {
      Splitter charSplitter = Splitter.on(' ');
      String input = "hello world!";
      Iterable<String> result = charSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testOnCharMatcher() {
      Splitter charMatcherSplitter = Splitter.on(CharMatcher.javaUpperCase());
      String input = "HelloWorld!";
      Iterable<String> result = charMatcherSplitter.split(input);
      String[] expectedResult = {"Hello", "World!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testOnString() {
      Splitter stringSplitter = Splitter.on(" ");
      String input = "hello world!";
      Iterable<String> result = stringSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testOnPattern() {
      Pattern pattern = Pattern.compile(",");
      Splitter patternSplitter = Splitter.on(pattern);
      String input = "hello,world!";
      Iterable<String> result = patternSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testOnPatternWithEmptyString() {
      Pattern pattern = Pattern.compile("");
      try {
         Splitter patternSplitter = Splitter.on(pattern);
         fail("IllegalArgumentException should be thrown for empty pattern");
      } catch (IllegalArgumentException e) {
         // exception caught as expected
      }
   }

   @Test
   public void testOnEmptyString() {
      try {
         Splitter stringSplitter = Splitter.on("");
         fail("IllegalArgumentException should be thrown for empty separator");
      } catch (IllegalArgumentException e) {
         // exception caught as expected
      }
   }

   @Test
   public void testFixedLength() {
      Splitter fixedLengthSplitter = Splitter.fixedLength(2);
      String input = "hello world";
      Iterable<String> result = fixedLengthSplitter.split(input);
      String[] expectedResult = {"he", "ll", "o ", "wo", "rl", "d"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++].charAt(0), str.charAt(0));
      }
   }

   @Test
   public void testOmitEmptyStrings() {
      Splitter omitEmptyStringsSplitter = splitter.omitEmptyStrings();
      String input = "hello, ,world!";
      Iterable<String> result = omitEmptyStringsSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testLimit() {
      Splitter limitSplitter = splitter.limit(2);
      String input = "hello,world!,again!";
      Iterable<String> result = limitSplitter.split(input);
      String[] expectedResult = {"hello", "world!,again!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testTrimResults() {
      Splitter trimResultsSplitter = splitter.trimResults();
      String input = " hello , world! ";
      Iterable<String> result = trimResultsSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testTrimResultsWithCharMatcher() {
      Splitter trimResultsCharMatcherSplitter = splitter.trimResults(CharMatcher.is('.'));
      String input = ".hello. .world!. .";
      Iterable<String> result = trimResultsCharMatcherSplitter.split(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testSplitToList() {
      String input = "hello,world!";
      List<String> result = splitter.splitToList(input);
      String[] expectedResult = {"hello", "world!"};
      int i = 0;
      for (String str : result) {
         assertEquals(expectedResult[i++], str);
      }
   }

   @Test
   public void testWithKeyValueSeparator() {
      String input = "key1=value1,key2=value2";
      Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
      Map<String, String> result = mapSplitter.split(input);
      Map<String, String> expectedResult = new LinkedHashMap<>();
      expectedResult.put("key1", "value1");
      expectedResult.put("key2", "value2");
      assertEquals(expectedResult, result);
   }

   @Test(expected=IllegalArgumentException.class)
   public void testWithKeyValueSeparatorInvalidEntry() {
      String input = "key1,value1,key2";
      Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(",");
      Map<String, String> result = mapSplitter.split(input);
   }

}