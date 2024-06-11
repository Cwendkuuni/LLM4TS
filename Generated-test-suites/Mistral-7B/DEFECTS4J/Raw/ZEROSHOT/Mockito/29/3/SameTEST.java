package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameObject() {
        Same sameObjectMatcher = new Same("same object");
        Object actualObject = "same object";
        Matcher<Object> sameMatcher = is(sameObjectMatcher.wanted);

        // Test matches method
        assertThat(sameObjectMatcher.matches(actualObject), is(true));

        // Test describeTo method
        Description description = new Description();
        sameObjectMatcher.describeTo(description);
        String expectedDescription = "same(same object)";
        String actualDescription = description.toString();
        assertThat(actualDescription, is(expectedDescription));
    }

    @Test
    public void testDifferentStringObjects() {
        Same sameObjectMatcher1 = new Same("same object 1");
        Object actualObject1 = "different object";
        Matcher<Object> sameMatcher1 = is(sameObjectMatcher1.wanted);

        Same sameObjectMatcher2 = new Same("same object 2");
        Object actualObject2 = "different object";
        Matcher<Object> sameMatcher2 = is(sameObjectMatcher2.wanted);

        // Test matches method
        assertThat(sameObjectMatcher1.matches(actualObject1), is(false));
        assertThat(sameObjectMatcher2.matches(actualObject1), is(false));
        assertThat(sameObjectMatcher1.matches(sameObjectMatcher2.wanted), is(false));

        // Test describeTo method
        Description description1 = new Description();
        sameObjectMatcher1.describeTo(description1);
        String expectedDescription1 = "same(same object 1)";
        String actualDescription1 = description1.toString();
        assertThat(actualDescription1, is(expectedDescription1));

        Description description2 = new Description();
        sameObjectMatcher2.describeTo(description2);
        String expectedDescription2 = "same(same object 2)";
        String actualDescription2 = description2.toString();
        assertThat(actualDescription2, is(expectedDescription2));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacterMatcher = new Same('a');
        Character actualCharacter = 'a';
        Matcher<Object> sameMatcher = is(sameCharacterMatcher.wanted);

        // Test matches method
        assertThat(sameCharacterMatcher.matches(actualCharacter), is(true));

        // Test describeTo method
        Description description = new Description();
        sameCharacterMatcher.describeTo(description);
        String expectedDescription = "same('a')";
        String actualDescription = description.toString();
        assertThat(actualDescription, is(expectedDescription));
    }

    @Test
    public void testDifferentCharacters() {
        Same sameCharacterMatcher1 = new Same('a');
        Character actualCharacter1 = 'b';
        Matcher<Object> sameMatcher1 = is(sameCharacterMatcher1.wanted);

        Same sameCharacterMatcher2 = new Same('a');
        Character actualCharacter2 = 'b';
        Matcher<Object> sameMatcher2 = is(sameCharacterMatcher2.wanted);

        // Test matches method
        assertThat(sameCharacterMatcher1.matches(actualCharacter1), is(false));
        assertThat(sameCharacterMatcher2.matches(actualCharacter1), is(false));
        assertThat(sameCharacterMatcher1.matches(sameCharacterMatcher2.wanted), is(false));

        // Test describeTo method
        Description description1 = new Description();
        sameCharacterMatcher1.describeTo(description1);
        String expectedDescription1 = "same('a')";
        String actualDescription1 = description1.toString();
        assertThat(actualDescription1, is(expectedDescription1));

        Description description2 = new Description();
        sameCharacterMatcher2.describeTo(description2);
        String expectedDescription2 = "same('a')";
        String actualDescription2 = description2.toString();
        assertThat(actualDescription2, is(expectedDescription2));
    }

    @Test
    public void testSerializable() {
        Same sameObjectMatcher = new Same("same object");
        Object actualObject = "same object";
        Matcher<Object> sameMatcher = is(sameObjectMatcher.wanted);

        // Serialize and deserialize the Same object
        byte[] serializedObject = SerializationUtils.serialize(sameObjectMatcher);
        Same deserializedObjectMatcher = (Same) SerializationUtils.deserialize(serializedObject);

        // Test matches method
        assertThat(deserializedObjectMatcher.matches(actualObject), is(true));

        // Test describeTo method
        Description description = new Description();
        deserializedObjectMatcher.describeTo(description);
        String expectedDescription = "same(same object)";
        String actualDescription = description.toString();
        assertThat(actualDescription, is(expectedDescription));
    }
}