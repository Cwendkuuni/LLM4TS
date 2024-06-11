import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameMethodWithDifferentObjectsOfSameClass() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithNullObject() {
        Same sameMatcher = new Same(null);
        // This test case should throw an IllegalArgumentException when the wanted object is null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameMethodWithDifferentClasses() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Integer actual = 1;

        assertFalse(sameMatcher.matches(actual));
        // This test case should throw an IllegalArgumentException when the actual object is not of the same class as the wanted object
    }

    @Test
    public void testDescribeToMethodWithString() {
        Object wanted = "mockito";
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(\"mockito\")";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToMethodWithCharacter() {
        Object wanted = 'a';
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same('a')";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToMethodWithSerializableObject() {
        Object wanted = new SerializableObject();
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(org.mockito.internal.matchers.Same@123456)";
        String actualDescription = description.toString();

        assertTrue(actualDescription.contains("Same@"));
    }
}

class SerializableObject implements Serializable {
    // Empty class
}