import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Typical use case
        AttributeModel model1 = new AttributeModel("Apple");
        AttributeModel model2 = new AttributeModel("Banana");
        assertEquals(-1, comparator.compare(model1, model2));

        // Edge case 1: both models are null
        assertEquals(-1, comparator.compare(null, null));

        // Edge case 2: one model is null
        assertEquals(1, comparator.compare(model1, null));
        assertEquals(-1, comparator.compare(null, model2));

        // Edge case 3: comparing with 'a'
        AttributeModel model3 = new AttributeModel("Zebra");
        assertEquals(-1, comparator.compare(model1, model3));
        assertEquals(1, comparator.compare(model3, model1));
    }
}