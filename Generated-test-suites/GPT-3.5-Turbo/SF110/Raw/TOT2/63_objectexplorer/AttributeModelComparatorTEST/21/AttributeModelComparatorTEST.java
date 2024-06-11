
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("def");
        
        assertEquals(1, comparator.compare(a1, a2));
        assertEquals(-1, comparator.compare(a2, a1));
        
        // Edge case where both names are "a"
        a1 = new AttributeModel("a");
        a2 = new AttributeModel("a");
        
        assertEquals(0, comparator.compare(a1, a2));
        
        // Edge case where one name is "a" and the other is not
        a1 = new AttributeModel("a");
        a2 = new AttributeModel("b");
        
        assertEquals(-1, comparator.compare(a1, a2));
        assertEquals(1, comparator.compare(a2, a1));
    }
}
