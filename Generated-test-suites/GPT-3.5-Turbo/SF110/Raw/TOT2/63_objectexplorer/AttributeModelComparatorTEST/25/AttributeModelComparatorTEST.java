import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("Attribute1");
        AttributeModel model2 = new AttributeModel("Attribute2");
        assertEquals(1, comparator.compare(model1, model2));
        
        model1 = new AttributeModel("a");
        model2 = new AttributeModel("b");
        assertEquals(-1, comparator.compare(model1, model2));
        
        model1 = new AttributeModel("z");
        model2 = new AttributeModel("a");
        assertEquals(-1, comparator.compare(model1, model2));
        
        model1 = new AttributeModel(null);
        model2 = new AttributeModel("Attribute");
        assertEquals(-1, comparator.compare(model1, model2));
    }
    
}