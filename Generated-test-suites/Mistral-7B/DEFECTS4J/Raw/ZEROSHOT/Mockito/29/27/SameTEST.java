import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

@RunWith(org.mockito.runners.JUnit4.class)
public class SameTest {

 private Object wanted;

 @Test
 public void testMatchesSameObject() {
 wanted = new Object();

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.that(wanted, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesDifferentObjects() {
 wanted = new Object();
 Object differentObject = new Object();

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.assertThat(differentObject, sameMatcher);
 }

 @Test
 public void testDescribeToWithString() {
 wanted = "Hello";

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 Description description = new Description();
 sameMatcher.describeTo(description);

 String expectedDescription = "same(\"Hello\")";
 String actualDescription = description.toString();

 MatcherAssert.assertThat(actualDescription, CoreMatchers.is(expectedDescription));
 }

 @Test
 public void testDescribeToWithCharacter() {
 wanted = 'A';

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 Description description = new Description();
 sameMatcher.describeTo(description);

 String expectedDescription = "same('A')";
 String actualDescription = description.toString();

 MatcherAssert.assertThat(actualDescription, CoreMatchers.is(expectedDescription));
 }

 @Test
 public void testMatchesSameInteger() {
 wanted = 1;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.that(wanted, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesDifferentInteger() {
 wanted = 1;
 int differentInteger = 2;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.assertThat(differentInteger, sameMatcher);
 }

 @Test
 public void testMatchesSameDouble() {
 wanted = 1.0;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.that(wanted, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesDifferentDouble() {
 wanted = 1.0;
 double differentDouble = 2.0;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.assertThat(differentDouble, sameMatcher);
 }

 @Test
 public void testMatchesSameBoolean() {
 wanted = true;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.that(wanted, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesDifferentBoolean() {
 wanted = true;
 boolean differentBoolean = false;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.assertThat(differentBoolean, sameMatcher);
 }

 @Test
 public void testMatchesSameNull() {
 wanted = null;

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.that(wanted, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesDifferentNull() {
 wanted = null;
 Object differentNull = new Object();

 ArgumentMatcher<Object> sameMatcher = new Same(wanted);
 MatcherAssert.assertThat(differentNull, sameMatcher);
 }

 @Test
 public void testMockitoMatchesSameObject() {
 Object wantedMock = Mockito.mock(Object.class);
 ArgumentMatcher<Object> sameMatcher = new Same(wantedMock);

 Object actual = wantedMock;
 Mockito.verify(wantedMock).equals(actual);
 MatcherAssert.assertThat(actual, sameMatcher);
 }

 @Test(expected = AssertionError.class)
 public void testMockitoDoesNotMatchDifferentObjects() {
 Object wantedMock = Mockito.mock(Object.class);
 ArgumentMatcher<Object> sameMatcher = new Same(wantedMock);
 Object differentMock = Mockito.mock(Object.class);

 MatcherAssert.assertThat(differentMock, sameMatcher);
 verify(wantedMock).equals(differentMock);
 }

}