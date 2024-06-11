import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {

    // Typical Use Cases
    
    @Test
    public void testCompare_typicalUseCase1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("def");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_typicalUseCase2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("aaa");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    // Edge Cases
    
    @Test
    public void testCompare_edgeCase1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("abc");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_edgeCase2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("b");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_edgeCase3() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("A");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    // Error Scenarios
    
    @Test
    public void testCompare_errorScenario1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("abc");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_errorScenario2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
}