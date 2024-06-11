import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoCore;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.answers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoCore.class)
public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNoMismatches_thenReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
     Object[] arguments = new Object[]{new String("test"), 1};

     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertThat(indexes, notNullValue());
     assertThat(indexes.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreOfDifferentSize_thenReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(isA(String.class));
     Object[] arguments = new Object[]{new String("test"), 1, new Object()};

     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertThat(indexes, notNullValue());
     assertThat(indexes.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreOfDifferentTypesButToStringRepresentationIsSame_thenReturnsIndexes() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Object.class));
     Object[] arguments = new Object[]{new String("test"), new Object()};

     GivenStringMatcher stringMatcher = new GivenStringMatcher("expectedValue");
     GivenObjectMatcher objectMatcher = new GivenObjectMatcher();

     matchers.set(0, stringMatcher);
     matchers.set(1, objectMatcher);

     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertThat(indexes, notNullValue());
     assertThat(indexes.length, is(1));
     assertThat(indexes[0].intValue(), is(1));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreOfDifferentTypesButToStringRepresentationIsSame_thenMatchesToString() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Object.class));
     Object[] arguments = new Object[]{new String("test"), new Object()};

     GivenStringMatcher stringMatcher = new GivenStringMatcher("test");
     GivenObjectMatcher objectMatcher = new GivenObjectMatcher();

     matchers.set(0, stringMatcher);
     matchers.set(1, objectMatcher);

     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertThat(indexes, notNullValue());
     assertThat(indexes.length, is(1));
     assertThat(indexes[0].intValue(), is(1));
     assertThat(stringMatcher, isA(ContainsExtraTypeInformation.class));
     assertThat(objectMatcher, isA(ContainsExtraTypeInformation.class));
     assertThat(toStringEquals(stringMatcher, arguments[0]), is(true));
     assertThat(toStringEquals(objectMatcher, arguments[1]), is(true));
     assertThat(toStringEquals(stringMatcher, arguments[1]), is(false));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreOfDifferentTypesButToStringRepresentationIsDifferent_thenReturnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Object.class));
     Object[] arguments = new Object[]{new String("test"), new Object()};

     GivenStringMatcher stringMatcher = new GivenStringMatcher("expectedValue");
     GivenObjectMatcher objectMatcher = new GivenObjectMatcher();

     matchers.set(0, stringMatcher);
     matchers.set(1, objectMatcher);

     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertThat(indexes, notNullValue());
     assertThat(indexes.length, is(0));
 }

 private boolean toStringEquals(Matcher matcher, Object argument) {
     StringDescription stringDescription = new StringDescription();
     matcher.describeTo(stringDescription);
     return stringDescription.toString().equals(argument.toString());
 }

 @SuppressWarnings("unchecked")
 private static class GivenStringMatcher extends ContainsExtraTypeInformation<String> {
     private final String expectedValue;

     public GivenStringMatcher(String expectedValue) {
         this.expectedValue = expectedValue;
     }

     @Override
     public String describeTo(StringDescription description) {
         description.appendText("a String equal to ").appendValue(expectedValue);
         return description.toString();
     }

     @Override
     public boolean matches(Object argument) {
         return argument instanceof String && ((String) argument).equals(expectedValue);
     }
 }

 @SuppressWarnings("unchecked")
 private static class GivenObjectMatcher extends ContainsExtraTypeInformation<Object> {
     @Override
     public String describeTo(StringDescription description) {
         description.appendText("an instance of Object");
         return description.toString();
     }

     @Override
     public boolean matches(Object argument) {
         return true;
     }
 }

 @Test
 public void testSafelyMatches_whenMatcherMatchesArgument_thenReturnsTrue() {
     Matcher<String> matcher = isA(String.class);
     String argument = "test";

     boolean result = ArgumentMatchingTool.safelyMatches(matcher, argument);
     assertTrue(result);
 }

 @Test
 public void testSafelyMatches_whenMatcherThrowsException_thenReturnsFalse() {
     Matcher<String> matcher = new Matcher<String>() {
         @Override
         public boolean matches(String argument) {
             throw new RuntimeException("Test Exception");
         }
     };
     String argument = "test";

     boolean result = ArgumentMatchingTool.safelyMatches(matcher, argument);
     assertFalse(result);
 }

 @Test
 public void testToStringEquals_whenToStringRepresentationIsEqual_thenReturnsTrue() {
     Matcher<String> matcher = isA(String.class);
     String argument = "test";

     boolean result = ArgumentMatchingTool.toStringEquals(matcher, argument);
     assertTrue(result);
 }

 @Test
 public void testToStringEquals_whenToStringRepresentationIsNotEqual_thenReturnsFalse() {
     Matcher<String> matcher = isA(String.class);
     Object argument = new Object();

     boolean result = ArgumentMatchingTool.toStringEquals(matcher, argument);
     assertFalse(result);
 }

 @Test
 public void testToStringEquals_whenMatcherIsStringDescription_whenToStringRepresentationIsEqual_thenReturnsTrue() {
     Matcher<String> matcher = isA(String.class);
     String argument = "test";
     StringDescription stringDescription = new StringDescription();
     matcher.describeTo(stringDescription);

     boolean result = ArgumentMatchingTool.toStringEquals(matcher, argument);
     assertTrue(result);
 }

 @Test
 public void testToStringEquals_whenMatcherIsStringDescription_whenToStringRepresentationIsNotEqual_thenReturnsFalse() {
     Matcher<String> matcher = isA(String.class);
     String argument = "test";
     StringDescription stringDescription = new StringDescription();
     matcher.describeTo(stringDescription);
     stringDescription.appendText(" (extra info)");

     boolean result = ArgumentMatchingTool.toStringEquals(matcher, argument);
     assertFalse(result);
 }

 private static class TestAnswer implements Answer<Void> {
     @Override
     public Void answer(InvocationOnMock invocation) throws Throwable {
         return null;
     }
 }
}