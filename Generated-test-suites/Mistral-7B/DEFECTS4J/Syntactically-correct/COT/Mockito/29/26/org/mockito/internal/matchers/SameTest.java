import org.junit.Test;
import org.junit. Assert;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.mockito.internal.matchers.Same.appendQuoting;

public class SameTest {

 private Object wanted;

 @Test
 public void testMatchesSameObjects() {
 wanted = new Object();
 Same sameMatcher = new Same(wanted);
 Object actual = new Object();

 Assert.assertTrue("Should match same objects", sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesNullAndNull() {
 Same sameMatcher = new Same(null);

 Assert.assertTrue("Should match null and null", sameMatcher.matches(null));
 }

 @Test(expected = IllegalStateException.class)
 public void testMatchesDifferentTypes() {
 Object stringWanted = "test";
 Object integerWanted = 123;
 Same sameMatcher = new Same(stringWanted);

 try {
 sameMatcher.matches(integerWanted);
 } catch (AssertionError e) {
 throw new IllegalStateException("Unexpected AssertionError", e);
 }
 }

 @Test
 public void testMatchesStringWithQuotes() {
 wanted = "test";
 Same sameMatcher = new Same(wanted);
 Object actual = "test";

 Assert.assertTrue("Should match strings with quotes", sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesCharacterWithQuotes() {
 wanted = 'c';
 Same sameMatcher = new Same(wanted);
 Object actual = 'c';

 Assert.assertTrue("Should match characters with quotes", sameMatcher.matches(actual));
 }

 @Test
 public void testMatchesNullAndAnotherNull() {
 Same sameMatcher = new Same(null);

 Assert.assertTrue("Should match null and another null", sameMatcher.matches(null));
 }

 @Test
 public void testMatchesSerializableObjects() {
 wanted = new SerializableObject();
 Same sameMatcher = new Same(wanted);
 Object actual = new SerializableObject();

 Assert.assertTrue("Should match serializable objects", sameMatcher.matches(actual));
 }

 @Test
 public void testDescribeTo() {
 Object wanted = new Object();
 Description description = new Description();
 Same sameMatcher = new Same(wanted);
 sameMatcher.describeTo(description);

 Matcher expectedMatcher = CoreMatchers.is(wanted);
 Matcher actualMatcher = new SameMatcher(wanted);
 actualMatcher.describeTo(description);

 Assert.assertEquals("Description of SameMatcher should match", expectedMatcher.toString(), actualMatcher.toString());
 }

 @Test
 public void testAppendQuotingForString() {
 Object stringWanted = "test";
 Description description = new Description();
 Same sameMatcher = new Same(stringWanted);
 sameMatcher.appendQuoting(description);

 Assert.assertThat("Quoted string should be enclosed in double quotes", description.toString(), CoreMatchers.containsString("\"test\""));
 }

 @Test
 public void testAppendQuotingForCharacter() {
 Object charWanted = 'c';
 Description description = new Description();
 Same sameMatcher = new Same(charWanted);
 sameMatcher.appendQuoting(description);

 Assert.assertThat("Quoted character should be enclosed in single quotes", description.toString(), CoreMatchers.containsString("'c'"));
 }
}

 class SerializableObject implements Serializable {
 }