import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_equalAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("a");
        int result = comparator.compare(attr1, attr2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_differentAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("b");
        int result = comparator.compare(attr1, attr2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_attr1Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = null;
        AttributeModel attr2 = new AttributeModel("a");
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_attr2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = null;
        int result = comparator.compare(attr1, attr2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_attr1AndAttr2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = null;
        AttributeModel attr2 = null;
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_attr1NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("b");
        AttributeModel attr2 = new AttributeModel("a");
        int result = comparator.compare(attr1, attr2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_attr2NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("b");
        int result = comparator.compare(attr1, attr2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_attr1AndAttr2NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("b");
        AttributeModel attr2 = new AttributeModel("c");
        int result = comparator.compare(attr1, attr2);
        assertEquals(0, result);
    }
}