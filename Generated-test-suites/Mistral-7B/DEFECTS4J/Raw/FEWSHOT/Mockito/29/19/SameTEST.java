package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.Same;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class SameTest {

    @Test
    public void testSameMatcher() {
        Same sameInstance = new Same(new Object());
        Object actual = new Object();

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testSameStringMatcher() {
        Same sameInstance = new Same("testString");
        String actual = "testString";

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testSameCharacterMatcher() {
        Same sameInstance = new Same('c');
        Character actual = 'c';

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testDifferentTypesMatcher() {
        Same intSameInstance = new Same(5);
        Object actual = new String("five");

        assertFalse(intSameInstance.matches(actual));
    }

    @Test
    public void testNullValueMatcher() {
        Same sameInstance = new Same(null);
        Object actual = null;

        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Same sameInstance = new Same("expectedValue");

        Description description = new Description();
        sameInstance.describeTo(description);

        String expectedDescription = "same(expectedValue)";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeToWithString() {
        Same sameInstance = new Same("expectedValue");

        Description description = new Description();
        sameInstance.describeTo(description);

        String expectedDescription = "same(\"expectedValue\")";
        String actualDescription = description.toString();

        assertTrue(actualDescription.contains(expectedDescription));
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same sameInstance = new Same('c');

        Description description = new Description();
        sameInstance.describeTo(description);

        String expectedDescription = "same('c')";
        String actualDescription = description.toString();

        assertTrue(actualDescription.contains(expectedDescription));
    }

    @Test
    public void testDescribeToWithNullValue() {
        Same sameInstance = new Same(null);

        Description description = new Description();
        sameInstance.describeTo(description);

        String expectedDescription = "same(null)";
        String actualDescription = description.toString();

        assertTrue(actualDescription.contains(expectedDescription));
    }

    @Test
    public void testAnswerWithSameInstance() {
        Same sameInstance = new Same("expectedValue");
        String actualValue = "expectedValue";

        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object argument = invocation.getArgument(0);
                return sameInstance.matches(argument) ? actualValue : "different value";
            }
        };

        String result = answer.answer(new MockitoTestRunner().getMockitoContext().getMockingContext().createMock(String.class), new Object[]{sameInstance, "actualValue"});

        assertEquals("expectedValue", result);
    }

    @Test
    public void testAnswerWithDifferentInstance() {
        Same sameInstance = new Same("expectedValue");
        String differentValue = "different Value";

        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object argument = invocation.getArgument(0);
                return sameInstance.matches(argument) ? "expectedValue" : differentValue;
            }
        };

        String result = answer.answer(new MockitoTestRunner().getMockitoContext().getMockingContext().createMock(String.class), new Object[]{sameInstance, "differentValue"});

        assertNotEquals("expectedValue", result);
    }
}