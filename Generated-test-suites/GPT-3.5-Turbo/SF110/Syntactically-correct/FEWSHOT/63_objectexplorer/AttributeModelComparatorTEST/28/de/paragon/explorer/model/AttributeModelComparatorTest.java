import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel model1 = new AttributeModel("apple");
        AttributeModel model2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(model1, model2));

        model1 = new AttributeModel("banana");
        model2 = new AttributeModel("apple");
        assertEquals(1, comparator.compare(model1, model2));

        model1 = new AttributeModel("banana");
        model2 = new AttributeModel("banana");
        assertEquals(0, comparator.compare(model1, model2));

        model1 = new AttributeModel("");
        model2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(model1, model2));

        model1 = new AttributeModel("banana");
        model2 = new AttributeModel("");
        assertEquals(1, comparator.compare(model1, model2));

        model1 = null;
        model2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(model1, model2));

        model1 = new AttributeModel("banana");
        model2 = null;
        assertEquals(1, comparator.compare(model1, model2));

        model1 = null;
        model2 = null;
        assertEquals(-1, comparator.compare(model1, model2));
    }
}