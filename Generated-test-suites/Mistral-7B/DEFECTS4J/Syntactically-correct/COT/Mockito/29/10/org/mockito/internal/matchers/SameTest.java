import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObject() {
        Same sameObject = new Same(new Object());
        Object actualObject = new Object();
        assertTrue(sameObject.matches(actualObject));
    }

    @Test
    public void testSameNull() {
        Same sameNull = new Same(null);
        assertNull(sameNull.matches(new Object()));
        assertNull(sameNull.matches(null));
        assertNull(sameNull.matches(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameNonObject() {
        Same sameNonObject = new Same("not an object");
    }

    @Test
    public void testSamePrimitiveTypes() {
        Same sameInt = new Same(5);
        int actualInt = 5;
        assertTrue(sameInt.matches(actualInt));

        Same sameBoolean = new Same(true);
        boolean actualBoolean = true;
        assertTrue(sameBoolean.matches(actualBoolean));
    }

    @Test
    public void testSameString() {
        Same sameString = new Same("Hello World");
        String actualString = "Hello World";
        assertTrue(sameString.matches(actualString));

        String differentString = "Hello There";
        assertFalse(sameString.matches(differentString));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('A');
        char actualCharacter = 'A';
        assertTrue(sameCharacter.matches(actualCharacter));

        char differentCharacter = 'B';
        assertFalse(sameCharacter.matches(differentCharacter));
    }

    @Test
    public void testSameArray() {
        Object[] wantedArray = new Object[]{1, 2, 3};
        Same sameArray = new Same(wantedArray);
        Object[] actualArray = new Object[]{1, 2, 3};
        assertTrue(sameArray.matches(actualArray));

        Object[] differentArray = new Object[]{1, 2, 4};
        assertFalse(sameArray.matches(differentArray));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameArrayWithNull() {
        Object[] wantedArray = new Object[]{1, new Object(), 3};
        Same sameArray = new Same(wantedArray);
    }

    @Test
    public void testSameCollection() {
        Integer[] wantedCollection = {1, 2, 3};
        Same sameCollection = new Same(wantedCollection);
        List<Integer> actualCollection = Arrays.asList(1, 2, 3);
        assertTrue(sameCollection.matches(actualCollection));

        List<Integer> differentCollection = Arrays.asList(1, 2, 4);
        assertFalse(sameCollection.matches(differentCollection));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameCollectionWithNull() {
        List<Integer> wantedCollection = Arrays.asList(1, new Object(), 3);
        Same sameCollection = new Same(wantedCollection);
    }

    @Test
    public void testDescribeTo() {
        Same sameObject = new Same(new Object());
        Description description = new Description();
        sameObject.describeTo(description);
        String expectedDescription = "same(<object>)";
        String actualDescription = description.toString();
        assertThat(actualDescription, CoreMatchers.is(expectedDescription));
    }
}