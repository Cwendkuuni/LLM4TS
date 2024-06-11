import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("abc");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_FirstNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("abc");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_SecondNull_ReturnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = null;
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstNameBeforeA_OtherNameAfterA_ReturnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_FirstNameAfterA_OtherNameBeforeA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("def");
        AttributeModel attribute2 = new AttributeModel("abc");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_BothNamesBeforeA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_BothNamesAfterA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("def");
        AttributeModel attribute2 = new AttributeModel("xyz");
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
}