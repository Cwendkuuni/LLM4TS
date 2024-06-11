import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SameTest {

 private Object wanted;
 private Object actual;
 private Description description;

 @Test
 public void testMatchesSameObjects() {
 wanted = new Object();
 actual = wanted;
 Same sameMatcher = new Same(wanted);
 Matcher<Object> matcher = sameMatcher;

 assertThat(matcher.matches(actual), is(true));
 }

 @Test
 public void testMatchesDifferentObjects() {
 wanted = new Object();
 actual = new Object();
 Same sameMatcher = new Same(wanted);
 Matcher<Object> matcher = sameMatcher;

 assertThat(matcher.matches(actual), is(false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExceptionWhenNullWantedObject() {
 wanted = null;
 Same sameMatcher = new Same(wanted);
 Matcher<Object> matcher = sameMatcher;

 try {
 matcher.matches(new Object());
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExceptionWhenNullActualObject() {
 wanted = new Object();
 Same sameMatcher = new Same(wanted);
 Matcher<Object> matcher = sameMatcher;

 try {
 matcher.matches(null);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testDescribeToFormatsSameObjects() {
 wanted = new Object();
 actual = wanted;
 Same sameMatcher = new Same(wanted);
 Description description = mock(Description.class);
 sameMatcher.describeTo(description);
 String expectedDescription = "same(" + wanted.toString() + ")";
 String actualDescription = description.toString();

 assertThat(actualDescription, is(equalTo(expectedDescription)));
 }

 @Test
 public void testDescribeToFormatsDifferentObjects() {
 wanted = new Object();
 actual = new Object();
 Same sameMatcher = new Same(wanted);
 Description description = mock(Description.class);
 sameMatcher.describeTo(description);
 String expectedDescription = "same(" + wanted.toString() + ")" +
 " != " + "different object";
 String actualDescription = description.toString();

 assertThat(actualDescription, is(notNullValue()));
 }
}