import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {
    
    private AttributeModelComparator comparator;
    private List<AttributeModel> attributeList;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attributeList = new ArrayList<>();
        attributeList.add(new AttributeModel("A"));
        attributeList.add(new AttributeModel("b"));
        attributeList.add(new AttributeModel("C"));
    }
    
    @Test
    public void testCompare_LessThan() {
        int result = comparator.compare(attributeList.get(0), attributeList.get(1));
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_GreaterThan() {
        int result = comparator.compare(attributeList.get(2), attributeList.get(1));
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_Same() {
        int result = comparator.compare(attributeList.get(0), attributeList.get(0));
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NullInput() {
        int result = comparator.compare(null, attributeList.get(0));
        assertEquals(-1, result);
        
        result = comparator.compare(attributeList.get(0), null);
        assertEquals(1, result);
        
        result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_CompareIgnoreCase() {
        attributeList.add(new AttributeModel("ba"));
        attributeList.add(new AttributeModel("A"));
        attributeList.add(new AttributeModel("cB"));
        
        Collections.sort(attributeList, comparator);
        
        assertEquals("A", attributeList.get(0).getName());
        assertEquals("a", attributeList.get(1).getName());
        assertEquals("b", attributeList.get(2).getName());
        assertEquals("ba", attributeList.get(3).getName());
        assertEquals("C", attributeList.get(4).getName());
        assertEquals("cB", attributeList.get(5).getName());
    }
}