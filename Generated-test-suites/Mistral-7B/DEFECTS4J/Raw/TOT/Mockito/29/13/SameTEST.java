import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = wantedObject;

        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testDifferentObjects() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = new Object();

        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullAndSameObject() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = wantedObject;

        assertNull(sameMatcher.matches(null));
        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullAndDifferentObject() {
        Object wantedObject = new Object();
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = new Object();

        assertFalse(sameMatcher.matches(null));
        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testSameString() {
        String wantedString = "same string";
        Same sameMatcher = new Same(wantedString);
        String actualString = "same string";

        assertTrue(sameMatcher.matches(actualString));
    }

    @Test
    public void testDifferentStrings() {
        String wantedString = "same string";
        Same sameMatcher = new Same(wantedString);
        String actualString = "different string";

        assertFalse(sameMatcher.matches(actualString));
    }

    @Test
    public void testNullAndSameString() {
        String wantedString = "same string";
        Same sameMatcher = new Same(wantedString);
        String actualString = wantedString;

        assertNull(sameMatcher.matches(null));
        assertTrue(sameMatcher.matches(actualString));
    }

    @Test
    public void testNullAndDifferentString() {
        String wantedString = "same string";
        Same sameMatcher = new Same(wantedString);
        String actualString = "different string";

        assertFalse(sameMatcher.matches(null));
        assertFalse(sameMatcher.matches(actualString));
    }

    @Test
    public void testSameCharacter() {
        Character wantedCharacter = 'a';
        Same sameMatcher = new Same(wantedCharacter);
        Character actualCharacter = 'a';

        assertTrue(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testDifferentCharacters() {
        Character wantedCharacter = 'a';
        Same sameMatcher = new Same(wantedCharacter);
        Character actualCharacter = 'b';

        assertFalse(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testNullAndSameCharacter() {
        Character wantedCharacter = 'a';
        Same sameMatcher = new Same(wantedCharacter);
        Character actualCharacter = wantedCharacter;

        assertNull(sameMatcher.matches(null));
        assertTrue(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testNullAndDifferentCharacter() {
        Character wantedCharacter = 'a';
        Same sameMatcher = new Same(wantedCharacter);
        Character actualCharacter = 'b';

        assertFalse(sameMatcher.matches(null));
        assertFalse(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testMockedObject() {
        Object wantedObject = mock(Object.class);
        Same sameMatcher = new Same(wantedObject);
        Object actualObject = wantedObject;

        // In real usage, this assertion will be replaced with the verification
        // of a method call or a property access on the mocked object.
        assertTrue(sameMatcher.matches(actualObject));
    }
}