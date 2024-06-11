import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullArgument() {
        Object wanted = null;
        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        // Test with null argument
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testPrimitiveTypes() {
        int wanted = 1;
        int actual = 1;

        ArgumentMatcher<Integer> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testPrimitiveTypesDifferent() {
        int wanted = 1;
        int actual = 2;

        ArgumentMatcher<Integer> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testString() {
        String wanted = "test";
        String actual = "test";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testStringDifferent() {
        String wanted = "test";
        String actual = "different";

        ArgumentMatcher<String> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacter() {
        char wanted = 'a';
        char actual = 'a';

        ArgumentMatcher<Character> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterDifferent() {
        char wanted = 'a';
        char actual = 'b';

        ArgumentMatcher<Character> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCollection() {
        Object wanted = new Object()[1];
        Object actual = new Object()[1];

        ArgumentMatcher<Object[]> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testCollectionDifferent() {
        Object wanted = new Object[]{1};
        Object actual = new Object[]{2};

        ArgumentMatcher<Object[]> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testArrayOfPrimitives() {
        byte[] wanted = new byte[]{1};
        byte[] actual = new byte[]{1};

        ArgumentMatcher<byte[]> sameMatcher = new Same(wanted);

        // Test matched case
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testArrayOfPrimitivesDifferent() {
        byte[] wanted = new byte[]{1};
        byte[] actual = new byte[]{2};

        ArgumentMatcher<byte[]> sameMatcher = new Same(wanted);

        // Test not matched case
        assertFalse(sameMatcher.matches(actual));
    }
}