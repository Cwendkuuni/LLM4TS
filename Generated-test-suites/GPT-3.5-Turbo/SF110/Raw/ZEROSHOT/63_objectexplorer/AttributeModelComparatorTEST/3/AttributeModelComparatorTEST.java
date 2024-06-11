import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attribute1;
    private AttributeModel attribute2;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attribute1 = new AttributeModel("Name1");
        attribute2 = new AttributeModel("Name2");
    }
    
    @Test
    public void testCompare_NullO1O2_ReturnsNegativeOne() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_NullO1_ReturnsNegativeOne() {
        int result = comparator.compare(null, attribute1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_NullO2_ReturnsNegativeOne() {
        int result = comparator.compare(attribute1, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameLessThanO2Name_ReturnsNegativeOne() {
        attribute1.setName("Name1");
        attribute2.setName("Name2");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameGreaterThanO2Name_ReturnsPositiveOne() {
        attribute1.setName("Name2");
        attribute2.setName("Name1");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_O1NameEqualsO2Name_ReturnsZero() {
        attribute1.setName("Name1");
        attribute2.setName("Name1");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1NameLessThanAAndO2NameGreaterThanOrEqualToA_ReturnsPositiveOne() {
        attribute1.setName("a");
        attribute2.setName("b");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_O1NameGreaterThanAAndO2NameLessThanA_ReturnsNegativeOne() {
        attribute1.setName("b");
        attribute2.setName("a");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameEqualsAAndO2NameGreaterThanOrEqualToA_ReturnsNegativeOne() {
        attribute1.setName("a");
        attribute2.setName("b");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameGreaterThanOrEqualToAAndO2NameEqualsA_ReturnsPositiveOne() {
        attribute1.setName("b");
        attribute2.setName("a");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_O1NameLessThanAAndO2NameLessThanA_ReturnsNegativeOne() {
        attribute1.setName("a");
        attribute2.setName("a");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameGreaterThanAAndO2NameGreaterThanA_ReturnsPositiveOne() {
        attribute1.setName("b");
        attribute2.setName("b");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
}