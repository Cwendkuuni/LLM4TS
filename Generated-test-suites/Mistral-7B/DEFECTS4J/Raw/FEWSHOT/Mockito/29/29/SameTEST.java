package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.serialVersionUID;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatchesSameObjects() {
        Same sameObjectMatcher = new Same(new Object());
        Object object1 = new Object();
        Object object2 = object1;

        assertTrue(sameObjectMatcher.matches(object1));
        assertTrue(sameObjectMatcher.matches(object2));
    }

    @Test
    public void testMatchesDifferentObjects() {
        Same sameObjectMatcher = new Same(new Object());
        Object object1 = new Object();
        Object object2 = new String("Object 2");

        assertFalse(sameObjectMatcher.matches(object2));
    }

    @Test
    public void testMatchesDifferentClasses() {
        Same sameObjectMatcher = new Same(new Object());
        Integer integer = 42;

        assertFalse(sameObjectMatcher.matches(integer));
    }

    @Test
    public void testDescribeTo_String() {
        Same sameObjectMatcher = new Same("Test String");

        Description description = new Description();
        sameObjectMatcher.describeTo(description);

        String expectedDescription = "same(Test String)";
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeTo_Character() {
        Same sameCharacterMatcher = new Same('A');

        Description description = new Description();
        sameCharacterMatcher.describeTo(description);

        String expectedDescription = "same('A')";
        String actualDescription = description.toString();

        assertTrue(expectedDescription.equals(actualDescription));
    }

    @Test
    public void testDescribeTo_SerializableObject() {
        Same sameSerializableObjectMatcher = new Same(new SerializableObject());

        Description description = new Description();
        sameSerializableObjectMatcher.describeTo(description);

        String expectedDescription = "same(org.mockito.internal.matchers.Same@36c2a7aa)";
        String actualDescription = description.toString();

        assertTrue(expectedDescription.startsWith(actualDescription));
    }

    @Test
    public void testMatches_SerializableObject() {
        SerializableObject serializableObject1 = new SerializableObject();
        SerializableObject serializableObject2 = serializableObject1;

        Same sameSerializableObjectMatcher = new Same(serializableObject1);

        assertTrue(sameSerializableObjectMatcher.matches(serializableObject2));
    }
}

class SerializableObject implements Serializable {

}