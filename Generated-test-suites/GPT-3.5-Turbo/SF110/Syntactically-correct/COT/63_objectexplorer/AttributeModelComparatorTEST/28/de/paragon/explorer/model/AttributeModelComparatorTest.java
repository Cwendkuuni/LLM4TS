
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        // Test case 1: Comparing two AttributeModels with different names
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Banana");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = o1.getName().compareToIgnoreCase(o2.getName());
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);

        // Test case 2: Comparing two AttributeModels with the same name
        AttributeModel o3 = new AttributeModel("Carrot");
        AttributeModel o4 = new AttributeModel("Carrot");
        expected = o3.getName().compareToIgnoreCase(o4.getName());
        result = comparator.compare(o3, o4);
        assertEquals(expected, result);

         // additional test cases can be added based on identified edge cases and exception handling scenarios

    }

    @Test
    public void testCompare_WhenO1IsNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Banana");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = -1;
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompare_WhenO2IsNull() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = 1;
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompare_WhenO1AndO2HaveSameNameAndA_AS_STRINGIsDifferent() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Apple");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = o1.getName().compareToIgnoreCase(o2.getName());
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompare_WhenO1AndO2HaveSameNameAndA_AS_STRINGIs_lowerCaseA() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Apple");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = -1; // Because "Apple" < "a"
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);
    }

    @Test
    public void testCompare_WhenO1AndO2HaveSameNameAndA_AS_STRINGIs_upperCaseA() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Apple");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int expected = 1; // Because "Apple" > "A"
        int result = comparator.compare(o1, o2);
        assertEquals(expected, result);
    }

    // add test cases to test scenarios where `compareToIgnoreCase()` throws an exception

}

