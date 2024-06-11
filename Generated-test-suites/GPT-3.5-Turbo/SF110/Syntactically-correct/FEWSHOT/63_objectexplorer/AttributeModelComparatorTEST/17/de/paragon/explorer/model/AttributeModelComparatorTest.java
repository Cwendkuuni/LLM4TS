import static org.junit.Assert.*;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test case 1: Both objects are null, should return 0
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int expectedResult1 = 0;
        int actualResult1 = comparator.compare(o1, o2);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2: Only one object is null, should return -1
        o1 = null;
        o2 = new AttributeModel();
        int expectedResult2 = -1;
        int actualResult2 = comparator.compare(o1, o2);
        assertEquals(expectedResult2, actualResult2);
        
        // Test case 3: Both objects have null names, should return 0
        o1 = new AttributeModel();
        o2 = new AttributeModel();
        int expectedResult3 = 0;
        int actualResult3 = comparator.compare(o1, o2);
        assertEquals(expectedResult3, actualResult3);
        
        // Test case 4: Both objects have same names, should return 0
        o1 = new AttributeModel();
        o1.setName("Name");
        o2 = new AttributeModel();
        o2.setName("Name");
        int expectedResult4 = 0;
        int actualResult4 = comparator.compare(o1, o2);
        assertEquals(expectedResult4, actualResult4);
        
        // Test case 5: o1 comes before o2 alphabetically, should return -1
        o1 = new AttributeModel();
        o1.setName("Aaa");
        o2 = new AttributeModel();
        o2.setName("Bbb");
        int expectedResult5 = -1;
        int actualResult5 = comparator.compare(o1, o2);
        assertEquals(expectedResult5, actualResult5);
        
        // Test case 6: o1 comes after o2 alphabetically, should return 1
        o1 = new AttributeModel();
        o1.setName("Bbb");
        o2 = new AttributeModel();
        o2.setName("Aaa");
        int expectedResult6 = 1;
        int actualResult6 = comparator.compare(o1, o2);
        assertEquals(expectedResult6, actualResult6);
    }
}