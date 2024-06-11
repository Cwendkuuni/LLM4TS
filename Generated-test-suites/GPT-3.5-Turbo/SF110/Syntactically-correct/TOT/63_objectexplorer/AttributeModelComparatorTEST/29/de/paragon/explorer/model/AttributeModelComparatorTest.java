import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }
    
    @Test
    public void testCompare_sameAttributeModel() {
        AttributeModel attribute1 = new AttributeModel("name");
        AttributeModel attribute2 = new AttributeModel("name");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1Null() {
        AttributeModel attribute2 = new AttributeModel("name");
        int result = comparator.compare(null, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o2Null() {
        AttributeModel attribute1 = new AttributeModel("name");
        int result = comparator.compare(attribute1, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_bothNull() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o1NameLessThanO2Name() {
        AttributeModel attribute1 = new AttributeModel("aaa");
        AttributeModel attribute2 = new AttributeModel("bbb");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o1NameGreaterThanO2Name() {
        AttributeModel attribute1 = new AttributeModel("bbb");
        AttributeModel attribute2 = new AttributeModel("aaa");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_o1NameEqualToO2Name() {
        AttributeModel attribute1 = new AttributeModel("aaa");
        AttributeModel attribute2 = new AttributeModel("aaa");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1NameLessThanAAndO2NameGreaterThanOrEqualsA() {
        AttributeModel attribute1 = new AttributeModel("aaa");
        AttributeModel attribute2 = new AttributeModel("baa");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_o1NameGreaterThanAAndO2NameLessThanOrEqualsA() {
        AttributeModel attribute1 = new AttributeModel("baa");
        AttributeModel attribute2 = new AttributeModel("aaa");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o1NameEqualToAAndO2NameEqualToA() {
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("a");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1NameEqualToAAndO2NameGreaterThanA() {
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("b");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_o1NameEqualToAAndO2NameLessThanA() {
        AttributeModel attribute1 = new AttributeModel("a");
        AttributeModel attribute2 = new AttributeModel("b");
        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }
    
}