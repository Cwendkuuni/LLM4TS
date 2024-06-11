package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Object wanted = new Object();
        GivenWantedObject w = given(wanted);

        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(true));
    }

    @Test
    public void testMatchesNullAndNull() {
        Same sameMatcher = new Same(null);

        Object actual = null;

        assertThat(sameMatcher.matches(actual), is(true));
    }

    @Test
    public void testDoesNotMatchDifferentObjects() {
        Object wanted = new Object();
        GivenWantedObject w = given(wanted);

        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(false));
    }

    @Test
    public void testMatchesStringSameObjects() {
        String wanted = "testString";
        GivenString wantedGiven = given(wanted);

        String actual = "testString";

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(true));
    }

    @Test
    public void testMatchesCharacterSameObjects() {
        Character wanted = 'c';
        GivenCharacter wantedGiven = given(wanted);

        Character actual = 'c';

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(true));
    }

    @Test
    public void testDoesNotMatchStringAndCharacter() {
        String wanted = "testString";
        GivenString wantedGiven = given(wanted);

        Character actual = 'c';

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(false));
    }

    @Test
    public void testDoesNotMatchDifferentString() {
        String wanted = "testString1";
        GivenString wantedGiven = given(wanted);

        String actual = "testString2";

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher.matches(actual), is(false));
    }

    @Test
    public void testMatchesCustomMatcher() {
        Object wanted = new Object();
        GivenWantedObject w = given(wanted);

        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Matcher<Object> matcher = new Matcher<Object>() {
            @Override
            public boolean matches(Object object) {
                return object == wanted;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("same(");
                description.appendValue(wanted);
                description.appendText(")");
            }
        };

        assertThat(sameMatcher, is(matcher));
    }

    @Test
    public void testMatchesCustomMatcherWithDescription() {
        Object wanted = new Object();
        GivenWantedObject w = given(wanted);

        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Matcher<Object> matcher = new Matcher<Object>() {
            @Override
            public boolean matches(Object object) {
                return object == wanted;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("same(");
                description.appendValue(wanted);
                description.appendText(")");
            }
        };

        Description description = new Description();
        Same sameMatcherWithDescription = new Same(wanted);
        sameMatcherWithDescription.describeTo(description);

        assertThat(sameMatcherWithDescription.matches(actual), is(true));
        assertThat(description.toString(), is("same(testObject)"));
    }

    @Test
    public void testSerializable() {
        Object wanted = new Object();
        GivenWantedObject w = given(wanted);

        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Answer<Object> answer = answer(sameMatcher);

        Object serializedSameMatcher = given(serializationService().serialize(sameMatcher)).thenReturn(wanted).get();
        Object deserializedSameMatcher = given(serializationService().deserialize(serializedSameMatcher)).thenReturn(wanted).get();

        Object result = then(w).when(answer).thenReturn(wanted).thenCallRealMethod().get();

        assertThat(result, is(actual));
        assertThat(deserializedSameMatcher, is(sameMatcher));
    }

    private static org.mockito.serialization.SerializationService serializationService() {
        return mock(org.mockito.serialization.SerializationService.class);
    }

    private static Answer<Object> answer(Object answerObject) {
        return mock(Answer.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return answerObject;
            }
        });
    }

    private static Object given(Object object) {
        return mock(object.getClass());
    }

    private static GivenString GivenString() {
        return mock(GivenString.class);
    }

    private static GivenCharacter GivenCharacter() {
        return mock(GivenCharacter.class);
    }

    private interface GivenString {
    }

    private interface GivenCharacter {
    }
}