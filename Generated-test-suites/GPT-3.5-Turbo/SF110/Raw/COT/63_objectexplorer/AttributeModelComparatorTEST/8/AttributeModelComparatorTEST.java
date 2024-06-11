import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel("attribute1");
        AttributeModel attributeModel2 = new AttributeModel("attribute2");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }
}