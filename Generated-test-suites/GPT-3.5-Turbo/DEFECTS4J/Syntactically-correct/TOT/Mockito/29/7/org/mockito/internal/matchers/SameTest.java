import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

    @Test
    public void testMatches_withSameObject_ReturnsTrue() {
        Object object = new Object();
        Same same = new Same(object);

        boolean result = same.matches(object);

        assertTrue(result);
    }

    @Test
    public void testMatches_withDifferentObject_ReturnsFalse() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        boolean result = same.matches(object2);

        assertFalse(result);
    }

    @Test
    public void testMatches_withNullObject_ReturnsFalse() {
        Object object = new Object();
        Same same = new Same(object);

        boolean result = same.matches(null);

        assertFalse(result);
    }

    @Test
    public void testDescribeTo_withStringObject_AppendQuoting() {
        String string = "test";
        Same same = new Same(string);
        Description description = Mockito.mock(Description.class);

        same.describeTo(description);

        Mockito.verify(description).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_withCharacterObject_AppendQuoting() {
        Character character = 'A';
        Same same = new Same(character);
        Description description = Mockito.mock(Description.class);

        same.describeTo(description);

        Mockito.verify(description).appendText("same('A')");
    }
}