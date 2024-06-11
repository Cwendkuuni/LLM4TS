package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        Same sameObj1 = new Same(obj1);
        Same sameObj2 = new Same(obj2);
        Same sameObj3 = new Same(obj3);

        assertTrue(sameObj1.matches(obj1));
        assertTrue(sameObj1.matches(obj2));
        assertFalse(sameObj1.matches(obj3));

        assertFalse(sameObj2.matches(obj1));
        assertTrue(sameObj2.matches(obj2));
        assertFalse(sameObj2.matches(obj3));

        assertFalse(sameObj3.matches(obj1));
        assertFalse(sameObj3.matches(obj2));
        assertTrue(sameObj3.matches(obj3));
    }

    @Test
    public void testDescribeTo_string() {
        Description description = new DescriptionStub();

        Same sameString = new Same("hello");

        sameString.describeTo(description);

        assertEquals("same(\"hello\")", description.toString());
    }

    @Test
    public void testDescribeTo_character() {
        Description description = new DescriptionStub();

        Same sameCharacter = new Same('a');

        sameCharacter.describeTo(description);

        assertEquals("same('a')", description.toString());
    }

    class DescriptionStub extends Description {
        private String description = "";

        public Description appendText(String text) {
            description += text;
            return this;
        }

        public String toString() {
            return description;
        }

        public void appendQuoting(Description description) {

        }
    }
}