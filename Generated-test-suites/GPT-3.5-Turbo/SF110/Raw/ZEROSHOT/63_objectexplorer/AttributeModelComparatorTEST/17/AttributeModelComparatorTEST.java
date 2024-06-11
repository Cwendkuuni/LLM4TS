import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private List<AttributeModel> attributeModels;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
        attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("C"));
        attributeModels.add(new AttributeModel("B"));
        attributeModels.add(new AttributeModel("A"));
    }

    @Test
    public void testCompare_EqualNames_ReturnsZero() {
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("A");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNameBeforeSecond_ReturnsNegativeValue() {
        AttributeModel o1 = new AttributeModel("B");
        AttributeModel o2 = new AttributeModel("C");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FirstNameAfterSecond_ReturnsPositiveValue() {
        AttributeModel o1 = new AttributeModel("C");
        AttributeModel o2 = new AttributeModel("B");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_FirstNameBeforeSecondWithNameStartingWithA_ReturnsNegativeValue() {
        AttributeModel o1 = new AttributeModel("B");
        AttributeModel o2 = new AttributeModel("A");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FirstNameAfterSecondWithNameStartingWithA_ReturnsPositiveValue() {
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("B");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_FirstNameBeforeSecondWithNameA_ReturnsPositiveValue() {
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("C");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FirstNameAfterSecondWithNameA_ReturnsNegativeValue() {
        AttributeModel o1 = new AttributeModel("C");
        AttributeModel o2 = new AttributeModel("A");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_NullFirstArgument_ReturnsNegativeValue() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("A");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_NullSecondArgument_ReturnsPositiveValue() {
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothArgumentsNull_ReturnsZero() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
}