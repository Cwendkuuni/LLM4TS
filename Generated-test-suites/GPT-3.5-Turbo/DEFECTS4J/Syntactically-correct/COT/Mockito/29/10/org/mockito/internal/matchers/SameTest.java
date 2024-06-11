import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        // Test with same objects
        Object object = new Object();
        Same same = new Same(object);
        assertTrue(same.matches(object));

        // Test with different objects
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        // Test with String
        String string = "test";
        Same sameString = new Same(string);
        Description descriptionString = new Description();
        sameString.describeTo(descriptionString);
        assertEquals("same(\"test\")", descriptionString.toString());

        // Test with Character
        Character character = 'c';
        Same sameCharacter = new Same(character);
        Description descriptionCharacter = new Description();
        sameCharacter.describeTo(descriptionCharacter);
        assertEquals("same('c')", descriptionCharacter.toString());
    }
}