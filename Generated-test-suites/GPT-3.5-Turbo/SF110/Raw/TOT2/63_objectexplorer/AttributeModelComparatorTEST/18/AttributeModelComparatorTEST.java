import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
        
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
        
        // Test for equal names
        model1 = new AttributeModel("abc");
        model2 = new AttributeModel("abc");
        result = comparator.compare(model1, model2);
        assertEquals(0, result);
        
        // Test when one of the models is null
        model1 = new AttributeModel("abc");
        model2 = null;
        result = comparator.compare(model1, model2);
        assertEquals(-1, result);
        
        // Test for edge case where name is "a"
        model1 = new AttributeModel("a");
        model2 = new AttributeModel("b");
        result = comparator.compare(model1, model2);
        assertEquals(1, result);
        
        // Test for edge case where name is "a"
        model1 = new AttributeModel("b");
        model2 = new AttributeModel("a");
        result = comparator.compare(model1, model2);
        assertEquals(-1, result);
    }
}