import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    
    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
    }
    
    // Test for compare() method
    
    // Typical use case: Testing when o1 is smaller than o2 alphabetically
    @Test
    public void testCompare_typicalSmallerO1O2() {
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("def");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    // Typical use case: Testing when o1 is larger than o2 alphabetically
    @Test
    public void testCompare_typicalLargerO1O2() {
        AttributeModel o1 = new AttributeModel("xyz");
        AttributeModel o2 = new AttributeModel("def");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
    
    // Edge case: Testing when o1 is null
    @Test
    public void testCompare_edgeNullO1() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("def");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    // Edge case: Testing when o2 is null
    @Test
    public void testCompare_edgeNullO2() {
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
    
    // Error scenario: Testing when both o1 and o2 are null
    @Test
    public void testCompare_errorBothNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    // Error scenario: Testing when o1 and o2 have the same name
    @Test
    public void testCompare_errorSameName() {
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("abc");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    // Error scenario: Testing when o1 is "a" and o2 is not "a"
    @Test
    public void testCompare_errorO1EqualsA() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("xyz");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    // Error scenario: Testing when o2 is "a" and o1 is not "a"
    @Test
    public void testCompare_errorO2EqualsA() {
        AttributeModel o1 = new AttributeModel("xyz");
        AttributeModel o2 = new AttributeModel("a");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
    
    // Error scenario: Testing when o1 and o2 are "a"
    @Test
    public void testCompare_errorBothA() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("a");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
}