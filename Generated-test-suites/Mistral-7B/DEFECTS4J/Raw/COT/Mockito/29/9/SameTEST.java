import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.mockito.internal.matchers.Same.appendQuoting;

public class SameTest {

 private Object wanted;
 private Object actual;

 @Test
 public void testSameMethodWithSameObjects() {
 wanted = new Object();
 actual = wanted;

 Assert.assertTrue("should pass when comparing same objects", match(new Same(wanted)).matches(actual));
 }

 @Test
 public void testDescribeToMethod() {
 Description description = new Description();
 wanted = new Object();

 match(new Same(wanted)).describeTo(description);

 String descriptionText = description.toString();
 String expectedDescriptionText = "same(" + appendQuoting(description) + wanted.toString() + appendQuoting(description) + ")";

 Assert.assertEquals("should generate correct description text", expectedDescriptionText, descriptionText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameMethodWithDifferentObjects() {
 wanted = new Object();
 actual = new Object();

 match(new Same(wanted)).matches(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameMethodWithDifferentClasses() {
 wanted = new String("hello");
 actual = new Integer(5);

 match(new Same(wanted)).matches(actual);
 }

 @Test(expected = NullPointerException.class)
 public void testSameMethodWithNullWanted() {
 wanted = null;
 actual = new Object();

 match(new Same(wanted)).matches(actual);
 }

 @Test
 public void testSameMethodWithNullActual() {
 wanted = new Object();
 actual = null;

 Assert.assertFalse("should not pass when comparing wanted to null", match(new Same(wanted)).matches(actual));
 }

 @Test
 public void testSameMethodWithDifferentTypesButSameValue() {
 wanted = 5;
 actual = 5.0;

 Assert.assertTrue("should pass when comparing Objects with same value but different types", match(new Same(wanted)).matches(actual));
 }

 @Test
 public void testSameMethodWithDifferentTypesButDifferentValues() {
 wanted = 5;
 actual = 6;

 Assert.assertFalse("should not pass when comparing Objects with different values", match(new Same(wanted)).matches(actual));
 }

 @Test
 public void testSameMethodWithPrimitiveTypes() {
 byte wantedByte = 5;
 short wantedShort = 10;
 int wantedInt = 15;
 long wantedLong = 20;
 float wantedFloat = 25.5f;
 double wantedDouble = 30.5;
 boolean wantedBoolean = true;

 byte actualByte = 5;
 short actualShort = 10;
 int actualInt = 15;
 long actualLong = 20L;
 float actualFloat = 25.5f;
 double actualDouble = 30.5;
 boolean actualBoolean = true;

 match(new Same(wantedByte)).matches(actualByte);
 match(new Same(wantedShort)).matches(actualShort);
 match(new Same(wantedInt)).matches(actualInt);
 match(new Same(wantedLong)).matches(actualLong);
 match(new Same(wantedFloat)).matches(actualFloat);
 match(new Same(wantedDouble)).matches(actualDouble);
 match(new Same(wantedBoolean)).matches(actualBoolean);
 }

 @Test
 public void testSameMethodWithArrays() {
 int[] wantedArrayInt = {1, 2, 3, 4};
 int[] actualArrayInt = {1, 2, 3, 4};

 Object[] wantedArrayObject = new Object[]{new Object(), new Object(), new Object()};
 Object[] actualArrayObject = new Object[]{new Object(), new Object(), new Object()};

 match(new Same(wantedArrayInt)).matches(actualArrayInt);
 match(new Same(wantedArrayObject)).matches(actualArrayObject);
 }

 @Test
 public void testSameMethodWithCollections() {
 Integer[] wantedListInt = {1, 2, 3, 4};
 List<Integer> actualListInt = Arrays.asList(1, 2, 3, 4);

 LinkedList<Integer> wantedLinkedListInt = new LinkedList<>(Arrays.asList(1, 2, 3, 4));
 LinkedList<Integer> actualLinkedListInt = new LinkedList<>(Arrays.asList(1, 2, 3, 4));

 match(new Same(wantedListInt)).matches(actualListInt);
 match(new Same(wantedLinkedListInt)).matches(actualLinkedListInt);
 }

 @Test
 public void testSameMethodWithMaps() {
 Map<String, Integer> wantedMap = new HashMap<>();
 wantedMap.put("key1", 1);
 wantedMap.put("key2", 2);

 Map<String, Integer> actualMap = new HashMap<>();
 actualMap.put("key1", 1);
 actualMap.put("key2", 2);

 match(new Same(wantedMap)).matches(actualMap);
 }

 @Test
 public void testSameMethodWithCustomClasses() {
 MyCustomClass wanted = new MyCustomClass();
 MyCustomClass actual = new MyCustomClass();

 match(new Same(wanted)).matches(actual);
 }

 private Matcher<Object> match(ArgumentMatcher<Object> matcher) {
 return matcher;
 }
}