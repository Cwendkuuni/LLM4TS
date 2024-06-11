import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_WhenO1IsNullAndO2IsNull_ShouldReturn0() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actual = comparator.compare(null, null);
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompare_WhenO1IsNullAndO2IsNotNull_ShouldReturnNegative1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actual = comparator.compare(null, new AttributeModel());
        int expected = -1;
        Assert.assertEquals(expected, actual);
    }

    // Add more test cases for other scenarios

}



// Merge all the individual test cases here

