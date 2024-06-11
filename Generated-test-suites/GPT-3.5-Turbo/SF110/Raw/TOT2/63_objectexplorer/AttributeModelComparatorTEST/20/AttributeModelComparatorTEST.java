import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical use case
        AttributeModel attr1 = new AttributeModel("Apple");
        AttributeModel attr2 = new AttributeModel("Banana");
        assertEquals(-1, comparator.compare(attr1, attr2));
        
        // Edge case: AttributeModel objects are null
        assertEquals(-1, comparator.compare(null, null));
        
        // Edge case: AttributeModel names are equal
        AttributeModel attr3 = new AttributeModel("Apple");
        AttributeModel attr4 = new AttributeModel("apple");
        assertEquals(0, comparator.compare(attr3, attr4));
        
        // Error scenario: AttributeModel names are both "a"
        AttributeModel attr5 = new AttributeModel("a");
        AttributeModel attr6 = new AttributeModel("a");
        assertEquals(0, comparator.compare(attr5, attr6));
        
        // Error scenario: One AttributeModel is null
        AttributeModel attr7 = new AttributeModel("Potato");
        assertEquals(-1, comparator.compare(attr7, null));
    }
}