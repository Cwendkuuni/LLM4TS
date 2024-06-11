
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothAttributesNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(null, null));
    }

    @Test
    public void testCompare_FirstAttributeNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr = new AttributeModel("b");
        assertEquals(-1, comparator.compare(null, attr));
    }

    @Test
    public void testCompare_SecondAttributeNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr = new AttributeModel("a");
        assertEquals(1, comparator.compare(attr, null));
    }

    @Test
    public void testCompare_SameAttributeName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("sameName");
        AttributeModel attr2 = new AttributeModel("sameName");
        assertEquals(0, comparator.compare(attr1, attr2));
    }

    @Test
    public void testCompare_DifferentAttributeName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("b");
        assertEquals(-1, comparator.compare(attr1, attr2));
    }

    @Test
    public void testCompare_EdgeCase1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("A");
        AttributeModel attr2 = new AttributeModel("B");
        assertEquals(-1, comparator.compare(attr1, attr2));
    }

    @Test
    public void testCompare_EdgeCase2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("C");
        AttributeModel attr2 = new AttributeModel("A");
        assertEquals(1, comparator.compare(attr1, attr2));
    }

    @Test
    public void testCompare_EmptyAttributeNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attr1 = new AttributeModel("");
        AttributeModel attr2 = new AttributeModel("");
        assertEquals(0, comparator.compare(attr1, attr2));
    }
}
