import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Same sameObjectMatcher = new Same(new Object());
        Object givenObject = new Object();
        assertTrue(sameObjectMatcher.matches(givenObject));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullWanted() {
        Same sameObjectMatcher = new Same(null);
        Object givenObject = new Object();
        // The test should throw a NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testMatchesNullActual() {
        Same sameObjectMatcher = new Same(new Object());
        Object givenObject = null;
        // The test should throw a NullPointerException
    }

    @Test
    public void testMatchesDifferentClasses() {
        Same sameObjectMatcher = new Same(new Object());
        String givenString = new String("Test String");
        assertFalse(sameObjectMatcher.matches(givenString));
    }

    // ... add more test cases for different edge cases

}