import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        // Create sample AttributeModel objects for testing
        AttributeModel model1 = new AttributeModel("apple");
        AttributeModel model2 = new AttributeModel("banana");

        // Create an instance of the AttributeModelComparator class
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when both objects have the same name
        int result1 = comparator.compare(model1, model1);
        assertEquals(0, result1);

        // Test when the first object's name is lexicographically smaller than the second object's name
        int result2 = comparator.compare(model1, model2);
        assertTrue(result2 < 0);

        // Test when the first object's name is lexicographically larger than the second object's name
        int result3 = comparator.compare(model2, model1);
        assertTrue(result3 > 0);

        // Test when either object is null
        int result4 = comparator.compare(null, model2);
        assertTrue(result4 < 0);

        int result5 = comparator.compare(model1, null);
        assertTrue(result5 > 0);

        int result6 = comparator.compare(null, null);
        assertEquals(0, result6);
    }

    @Test
    public void testCompareWithSpecialCases() {
        // Create sample AttributeModel objects for testing
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("xyz");

        // Create an instance of the AttributeModelComparator class
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test for special case where both names start with "a"
        int result1 = comparator.compare(model1, model2);
        assertTrue(result1 > 0);

        int result2 = comparator.compare(model2, model1);
        assertTrue(result2 < 0);
    }

    // Add more test cases for edge cases and exception handling scenarios here

}