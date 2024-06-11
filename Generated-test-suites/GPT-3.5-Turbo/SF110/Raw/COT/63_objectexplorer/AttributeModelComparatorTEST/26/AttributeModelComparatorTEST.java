
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {
    private AttributeModelComparator comparator;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_nullObjects_returnZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_nullObjectWithNonNull_returnNegativeOne() {
        int result = comparator.compare(null, new AttributeModel());
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_nonNullWithNullObject_returnOne() {
        int result = comparator.compare(new AttributeModel(), null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_sameName_returnZero() {
        AttributeModel attributeModel1 = new AttributeModel();
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel1.setName("abc");
        attributeModel2.setName("abc");

        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_o1NameBeforeAAndo2NameAfterA_returnNegativeOne() {
        AttributeModel attributeModel1 = new AttributeModel();
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel1.setName("aa");
        attributeModel2.setName("b");

        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_o2NameBeforeAAndo1NameAfterA_returnOne() {
        AttributeModel attributeModel1 = new AttributeModel();
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel1.setName("b");
        attributeModel2.setName("aa");

        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
}
