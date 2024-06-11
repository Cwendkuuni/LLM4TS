import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Map;
import java.util.Arrays;

public class JoinerTest {

   @Test
   public void testJoin() {
       String[] parts = {"Hello", "World"};
       Joiner joiner = Joiner.on("-");

       String expected = "Hello-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_singlePart() {
       String[] parts = {"Hello"};
       Joiner joiner = Joiner.on("-");

       String expected = "Hello";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_emptyInput() {
       String[] parts = {};
       Joiner joiner = Joiner.on("-");

       String expected = "";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }
   
   @Test
   public void testJoin_nullPart() {
       String[] parts = {"Hello", null, "World"};
       Joiner joiner = Joiner.on("-");

       String expected = "Hello-null-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_emptyStringSeparator() {
       String[] parts = {"Hello", "World"};
       Joiner joiner = Joiner.on("");

       String expected = "HelloWorld";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_emptyStringPart() {
       String[] parts = {"Hello", "", "World"};
       Joiner joiner = Joiner.on("-");

       String expected = "Hello--World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_nullSeparator() {
       String[] parts = {"Hello", "World"};
       Joiner joiner = Joiner.on(null);

       String expected = "HelloWorld";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_nullPartAndSeparator() {
       String[] parts = {"Hello", null, "World"};
       Joiner joiner = Joiner.on(null);

       String expected = "HelloWorld";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_singleNullPart() {
       String[] parts = {null};
       Joiner joiner = Joiner.on("-");

       String expected = "null";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_nullInput() {
       String[] parts = null;
       Joiner joiner = Joiner.on("-");

       String expected = "";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_WithIterable() {
       Iterable<String> parts = Arrays.asList("Hello", "World");
       Joiner joiner = Joiner.on("-");

       String expected = "Hello-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_WithIterator() {
       Iterator<String> parts = Arrays.asList("Hello", "World").iterator();
       Joiner joiner = Joiner.on("-");

       String expected = "Hello-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_WithEmptyIterable() {
       Iterable<String> parts = Arrays.asList();
       Joiner joiner = Joiner.on("-");

       String expected = "";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   @Test
   public void testJoin_WithNullIterable() {
       Iterable<String> parts = null;
       Joiner joiner = Joiner.on("-");

       String expected = "";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   // Additional test case for Joiner.useForNull()
   @Test
   public void testJoin_useForNull() {
       String[] parts = {"Hello", null, "World"};
       Joiner joiner = Joiner.on("-").useForNull("NULL");

       String expected = "Hello-NULL-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   // Additional test case for Joiner.skipNulls()
   @Test
   public void testJoin_skipNulls() {
       String[] parts = {"Hello", null, "World"};
       Joiner joiner = Joiner.on("-").skipNulls();

       String expected = "Hello-World";
       String result = joiner.join(parts);

       assertEquals(expected, result);
   }

   // Additional test case for Joiner.withKeyValueSeparator()
   @Test
   public void testJoin_withKeyValueSeparator() {
       Map<String, Integer> map = new HashMap<>();
       map.put("One", 1);
       map.put("Two", 2);
       map.put("Three", 3);

       Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");

       String expected = "One=1,Two=2,Three=3";
       String result = joiner.join(map);

       assertEquals(expected, result);
   }

   // Additional test case for Joiner.useForNull() with Joiner.withKeyValueSeparator()
   @Test
   public void testJoin_useForNull_withKeyValueSeparator() {
       Map<String, Integer> map = new HashMap<>();
       map.put("One", 1);
       map.put("Two", null);
       map.put("Three", 3);

       Joiner joiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("NULL");

       String expected = "One=1,Two=NULL,Three=3";
       String result = joiner.join(map);

       assertEquals(expected, result);
   }

   // Additional test case for Joiner.skipNulls() with Joiner.withKeyValueSeparator()
   @Test
   public void testJoin_skipNulls_withKeyValueSeparator() {
       Map<String, Integer> map = new HashMap<>();
       map.put("One", 1);
       map.put("Two", null);
       map.put("Three", 3);

       Joiner joiner = Joiner.on(",").withKeyValueSeparator("=").skipNulls();

       String expected = "One=1,Three=3";
       String result = joiner.join(map);

       assertEquals(expected, result);
   }

   // Additional test case for overflow of appendable
   @Test(expected = IOException.class)
   public void testJoin_appendTo_overflow() throws IOException {
       StringBuilder builder = new StringBuilder();
       Joiner joiner = Joiner.on("-");

       for (int i = 0; i < Integer.MAX_VALUE; i++) {
           builder.append("Hello");
       }

       joiner.appendTo(builder, "World"); // Expect an IOException
   }

   // Additional test case for AssertionError in appendTo for StringBuilder
   @Test(expected = AssertionError.class)
   public void testJoin_appendTo_assertionError() {
       StringBuilder builder = new StringBuilder();
       Joiner joiner = Joiner.on("-");

       joiner.appendTo(builder, Arrays.asList("Hello", "World").iterator());
   }

}