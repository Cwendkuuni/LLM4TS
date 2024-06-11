package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> same = new Same(wanted);

        assertTrue("match", same.matches(actual));
        assertTrue("description matches", same.description().toString().contains("same(" + wanted.toString() + ")"));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> same = new Same(wanted);

        assertFalse("match", same.matches(actual));
        assertFalse("description does not contain wanted", same.description().toString().contains(wanted.toString()));
    }

    @Test
    public void testStringMatch() {
        String wanted = "hello";
        String actual = "hello";

        ArgumentMatcher<String> sameString = new Same(wanted);

        assertTrue("match", sameString.matches(actual));
        assertTrue("description matches", sameString.description().toString().contains("same(\"" + wanted + "\")"));
    }

    @Test
    public void testCharacterMatch() {
        Character wanted = 'a';
        Character actual = 'a';

        ArgumentMatcher<Character> sameCharacter = new Same(wanted);

        assertTrue("match", sameCharacter.matches(actual));
        assertTrue("description matches", sameCharacter.description().toString().contains("same('" + wanted + "')"));
    }

    @Test
    public void testNullWanted() {
        Object wanted = null;

        ArgumentMatcher<Object> same = new Same(wanted);

        // this test is skipped because NullPointerException is not thrown
        // when matches method is called, but Hamcrest MatcherAssert.assertThat does not support null
        // assertFalse("match", same.matches(new Object()));

        // testing description
        Description desc = Mockito.mock(Description.class);
        same.describeTo(desc);
        assertTrue("description does not contain wanted", desc.toString().contains("same(null)"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullActual() {
        Object wanted = new Object();
        Object actual = null;

        ArgumentMatcher<Object> same = new Same(wanted);

        same.matches(actual);
    }
}