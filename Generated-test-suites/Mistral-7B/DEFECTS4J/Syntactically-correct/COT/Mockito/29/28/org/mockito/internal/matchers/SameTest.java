import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

 private Object wanted;
 private Object actual;
 private Description description;

 @Test
 public void testMatchesSameObject() {
 wanted = new Object();
 Same same = new Same(wanted);
 actual = wanted;

 assertTrue(same.matches(actual));
 }

 @Test
 public void testMatchesNullWithNull() {
 Same same = new Same(null);
 assertNull(same.matches(null));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesNullWithNonNull() {
 Same same = new Same(new Object());
 wanted = null;
 assertFalse(same.matches(wanted));
 }

 @Test
 public void testDescribeToStringLiteral() {
 wanted = "hello";
 Same same = new Same(wanted);
 description = new Description();
 same.describeTo(description);
 String expectedDescription = "same(\"hello\")";
 String actualDescription = description.toString();
 assertEquals(expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToStringObject() {
 wanted = new Object();
 Same same = new Same(wanted);
 description = new Description();
 same.describeTo(description);
 String expectedDescription = "same(<an object with value: " + wanted.toString() + " >)";
 String actualDescription = description.toString();
 assertEquals(expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToStringArray() {
 Object[] wantedArray = {1, 2, 3};
 Same same = new Same(wantedArray);
 description = new Description();
 same.describeTo(description);
 String expectedDescription = "same([1, 2, 3])";
 String actualDescription = description.toString();
 assertEquals(expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToStringCollection() {
 Object[] wantedArray = {1, 2, 3};
 @SuppressWarnings("unchecked")
 java.util.List<Integer> wantedList = (java.util.List<Integer>) java.util.Arrays.asList(wantedArray);
 Same same = new Same(wantedList);
 description = new Description();
 same.describeTo(description);
 String expectedDescription = "same([1, 2, 3])";
 String actualDescription = description.toString();
 assertEquals(expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToStringCharacter() {
 Character wantedCharacter = 'a';
 Same same = new Same(wantedCharacter);
 description = new Description();
 same.describeTo(description);
 String expectedDescription = "same('a')";
 String actualDescription = description.toString();
 assertEquals(expectedDescription, actualDescription);
 }
}