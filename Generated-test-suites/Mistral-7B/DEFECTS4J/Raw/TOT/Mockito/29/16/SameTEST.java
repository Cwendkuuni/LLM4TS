import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

    @Test
    public void testSameReference() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        ArgumentMatcher<Object> sameMatcher = new Same(obj1);

        assertTrue(sameMatcher.matches(obj2));
        assertTrue(sameMatcher.matches(obj1));
    }

    @Test
    public void testSameValue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        obj2 = obj1;

        ArgumentMatcher<Object> sameMatcher = new Same(obj1);

        assertTrue(sameMatcher.matches(obj2));
        assertTrue(sameMatcher.matches(obj1));
    }

    @Test
    public void testDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(obj1);

        assertFalse(sameMatcher.matches(obj2));
        assertFalse(sameMatcher.matches(obj1));
    }

    @Test
    public void testNullAndObject() {
        ArgumentMatcher<Object> sameMatcher = new Same(null);

        assertFalse(sameMatcher.matches(new Object()));
        assertFalse(sameMatcher.matches(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        new Same(null);
    }

    @Test
    public void testPrimitiveTypes() {
        int intValue = 5;
        char charValue = 'A';
        String stringValue = "test";

        ArgumentMatcher<Object> sameMatcherInt = new Same(intValue);
        ArgumentMatcher<Object> sameMatcherChar = new Same(charValue);
        ArgumentMatcher<Object> sameMatcherString = new Same(stringValue);

        assertEquals(intValue, intValue);
        assertTrue(sameMatcherInt.matches(intValue));

        assertEquals(charValue, charValue);
        assertTrue(sameMatcherChar.matches(charValue));

        assertEquals(stringValue, stringValue);
        assertTrue(sameMatcherString.matches(stringValue));

        intValue++;
        assertFalse(sameMatcherInt.matches(intValue));

        charValue++;
        assertFalse(sameMatcherChar.matches(charValue));

        stringValue += "test2";
        assertFalse(sameMatcherString.matches(stringValue));
    }

    @Test
    public void testSerializable() {
        Object obj = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(obj);

        byte[] serializedObject = Mockito.serialize(obj);
        Object deserializedObject = Mockito.deserialize(serializedObject);

        assertTrue(sameMatcher.matches(deserializedObject));
        assertTrue(sameMatcher.matches(obj));
    }

    @Test
    public void testAppendQuoting() {
        String string1 = "test";
        String string2 = "test";

        ArgumentMatcher<Object> sameMatcher = new Same(string1);

        sameMatcher.describeTo(Mockito.mock(org.hamcrest.Description.class));
        sameMatcher.appendQuoting(Mockito.mock(org.hamcrest.Description.class));

        String actualDescription = Mockito.mock(String.class);

        Mockito.when(actualDescription.appendText(string1)).thenReturn(actualDescription);
        Mockito.when(actualDescription.appendText("\"")).thenReturn(actualDescription);
        Mockito.when(actualDescription.appendText(string1)).thenReturn(actualDescription);
        Mockito.when(actualDescription.appendText("\"")).thenReturn(actualDescription);

        sameMatcher.describeTo(actualDescription);

        assertEquals("test", actualDescription.toString());
    }
}