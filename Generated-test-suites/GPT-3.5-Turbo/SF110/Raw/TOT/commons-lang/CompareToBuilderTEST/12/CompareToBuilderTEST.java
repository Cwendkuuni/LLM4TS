import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {

    // Test cases for reflectionCompare method
    
    @Test
    public void testReflectionCompare_sameObjects_zeroComparison() {
        Object obj = new Object();
        assertEquals(0, CompareToBuilder.reflectionCompare(obj, obj));
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompare_nullObject_throwNullPointerException() {
        CompareToBuilder.reflectionCompare(null, new Object());
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_typeMismatch_throwClassCastException() {
        CompareToBuilder.reflectionCompare(1, "1");
    }

    // Test cases for append method
    
    @Test
    public void testAppend_objectWithComparable_compareToResult() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append("abc", "abc");
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_objectWithComparator_compareResult() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10, 5, Comparator.reverseOrder());
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testAppend_arrayOfObjectsWithComparator_compareEachElement() {
        CompareToBuilder builder = new CompareToBuilder();
        String[] arr1 = {"abc", "def"};
        String[] arr2 = {"abc", "def"};
        builder.append(arr1, arr2, Comparator.naturalOrder());
        assertEquals(0, builder.toComparison());
    }

    @Test
    public void testAppend_arrayWithDifferentLengths_compareLength() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        builder.append(arr1, arr2);
        assertEquals(1, builder.toComparison());
    }

    // Other test cases
    
    @Test
    public void testAppendSuper_compareToResult() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        assertEquals(1, builder.toComparison());
    }

    @Test
    public void testToComparison_returnsComparisonValueOfBuilder() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append("abc", "def");
        assertEquals(-1, builder.toComparison());
    }

}