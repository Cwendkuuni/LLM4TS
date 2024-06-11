import org.junit.Test;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

 private Object wanted;
 private Object actual;
 private Same sameMatcher;

 @Test
 public void testMatchesSameObject() {
 wanted = new Object();
 sameMatcher = new Same(wanted);
 actual = wanted;

 Assert.assertTrue(sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesNullAndSameObject() {
 wanted = new Object();
 sameMatcher = new Same(wanted);
 actual = null;

 Assert.assertFalse(sameMatcher.matches(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArgument() {
 sameMatcher = new Same(null);
 }

 @Test
 public void testDescribeToMatchesString() {
 Description description = new Description();
 Same stringMatcher = new Same("Hello World");
 stringMatcher.describeTo(description);

 String expected = "same(\"Hello World\")";
 String actual = description.toString();

 Assert.assertThat(actual, is(expected));
 }

 @Test
 public void testDescribeToMatchesCharacter() {
 Description description = new Description();
 Same charMatcher = new Same('A');
 charMatcher.describeTo(description);

 String expected = "same('A')";
 String actual = description.toString();

 Assert.assertThat(actual, is(expected));
 }

 @Test
 public void testDescribeToMatchesObject() {
 Description description = new Description();
 Same objectMatcher = new Same(new Object());
 objectMatcher.describeTo(description);

 String expected = "same(<object>)";
 String actual = description.toString();

 Assert.assertThat(actual, is(expected));
 }
}