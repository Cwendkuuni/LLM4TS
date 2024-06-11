import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import static org.junit.Assert.assertThat;

public class EqualityTest {

    @Test
    public void testAreEqualNulls() {
        // Test case for areEqual method with null inputs
        assertThat(Equality.areEqual(null, null), is(true));
    }

    @Test
    public void testAreEqualObjects() {
        String str1 = "mockito";
        String str2 = "mockito";

        // Test case for areEqual method with equal objects
        assertThat(Equality.areEqual(str1, str2), is(true));
    }

    @Test
    public void testAreEqualDifferentObjects() {
        String str1 = "mockito";
        String str2 = "junit";

        // Test case for areEqual method with different objects
        assertThat(Equality.areEqual(str1, str2), is(false));
    }

    @Test
    public void testAreEqualArraysNulls() {
        Object[] arr1 = null;
        Object[] arr2 = null;

        // Test case for areArraysEqual method with null inputs
        assertThat(Equality.areArraysEqual(arr1, arr2), is(true));
    }

    @Test
    public void testAreEqualArraysSameLengthAndElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};

        // Test case for areArraysEqual method with arrays of equal length and elements
        assertThat(Equality.areArraysEqual(arr1, arr2), is(true));
    }

    @Test
    public void testAreEqualArraysDifferentLengths() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3, 4};

        // Test case for areArraysEqual method with arrays of different lengths
        assertThat(Equality.areArraysEqual(arr1, arr2), is(false));
    }

    @Test
    public void testAreEqualArraysSameLengthDifferentElements() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 4, 3};

        // Test case for areArraysEqual method with arrays of the same length but different elements
        assertThat(Equality.areArraysEqual(arr1, arr2), is(false));
    }

    @Test
    public void testIsArray() {
        Object[] arr = new Object[3];
        Integer intObj = new Integer(1);

        // Test case for isArray method
        assertThat(Equality.isArray(arr), is(true));
        assertThat(Equality.isArray(intObj), is(false));
    }
}