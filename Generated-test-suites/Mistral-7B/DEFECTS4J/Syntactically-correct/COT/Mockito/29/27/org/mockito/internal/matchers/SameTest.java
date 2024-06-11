import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

 private Object wanted;
 private Object actual;
 private Description description;

 @Test
 public void testMatchesSameObject() {
 wanted = new Object();
 Same sameMatcher = new Same(wanted);
 actual = wanted;

 Assert.assertTrue("should match same object", sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesNullAndSameObject() {
 wanted = new Object();
 Same sameMatcher = new Same(wanted);
 actual = null;

 Assert.assertFalse("should not match null and same object", sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesDifferentObjects() {
 wanted = new Object();
 Same sameMatcher = new Same(wanted);
 actual = new Object();

 Assert.assertFalse("should not match different objects", sameMatcher.matches(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArgument() {
 wanted = null;
 new Same(wanted);
 }

 @Test
 public void testDescribeToEmptyString() {
 wanted = "";
 description = new Description();
 new Same(wanted).describeTo(description);

 String expectedDescription = "same(\"\")";
 String actualDescription = description.toString();

 Assert.assertEquals("description should be 'same(\"\")'", expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToStringObject() {
 wanted = new Object();
 description = new Description();
 new Same(wanted).describeTo(description);

 String expectedDescription = "same(<wanted>{(java.lang.Object)})";
 String actualDescription = description.toString();

 Assert.assertEquals("description should be 'same(<wanted>{(java.lang.Object)})'", expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToString() {
 wanted = "test";
 description = new Description();
 new Same(wanted).describeTo(description);

 String expectedDescription = "same(\"test\")";
 String actualDescription = description.toString();

 Assert.assertEquals("description should be 'same(\"test\")'", expectedDescription, actualDescription);
 }

 @Test
 public void testDescribeToCharacter() {
 wanted = 'a';
 description = new Description();
 new Same(wanted).describeTo(description);

 String expectedDescription = "same('a')";
 String actualDescription = description.toString();

 Assert.assertEquals("description should be 'same('a')'", expectedDescription, actualDescription);
 }
}