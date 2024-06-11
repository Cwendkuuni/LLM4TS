
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.Comparator;

public class AttributeModelComparatorTest {

    private final AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompare_BothNull_ReturnsZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNull_ReturnsNegative() {
        AttributeModel model = new AttributeModel("Test");
        int result = comparator.compare(null, model);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SecondNull_ReturnsPositive() {
        AttributeModel model = new AttributeModel("Test");
        int result = comparator.compare(model, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothNotNull_SameName_ReturnsZero() {
        AttributeModel model1 = new AttributeModel("Test");
        AttributeModel model2 = new AttributeModel("Test");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_BothNotNull_DifferentName_ReturnsNegative() {
        AttributeModel model1 = new AttributeModel("Test1");
        AttributeModel model2 = new AttributeModel("Test2");
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_DifferentNameReverse_ReturnsPositive() {
        AttributeModel model1 = new AttributeModel("Test2");
        AttributeModel model2 = new AttributeModel("Test1");
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComesBeforeA_ReturnsNegative() {
        AttributeModel model1 = new AttributeModel("A");
        AttributeModel model2 = new AttributeModel("B");
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNotNull_NameComesAfterA_ReturnsPositive() {
        AttributeModel model1 = new AttributeModel("B");
        AttributeModel model2 = new AttributeModel("A");
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }
}