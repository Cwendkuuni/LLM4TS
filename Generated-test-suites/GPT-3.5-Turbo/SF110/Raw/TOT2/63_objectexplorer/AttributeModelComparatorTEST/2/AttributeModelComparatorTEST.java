import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        // Typical use case
        AttributeModel model1 = new AttributeModel("Apple");
        AttributeModel model2 = new AttributeModel("Banana");
        AttributeModelComparator comparator = new AttributeModelComparator();

        assertEquals(1, comparator.compare(model1, model2));
        
        // Edge case: Empty AttributeModel objects
        AttributeModel emptyModel1 = new AttributeModel("");
        AttributeModel emptyModel2 = new AttributeModel("");
        
        assertEquals(0, comparator.compare(emptyModel1, emptyModel2));
        
        // Edge case: AttributeModel with same name
        AttributeModel sameModel1 = new AttributeModel("Apple");
        AttributeModel sameModel2 = new AttributeModel("Apple");
        
        assertEquals(0, comparator.compare(sameModel1, sameModel2));
        
        // Error scenario: Null AttributeModel objects
        assertEquals(-1, comparator.compare(null, null));
    }
}