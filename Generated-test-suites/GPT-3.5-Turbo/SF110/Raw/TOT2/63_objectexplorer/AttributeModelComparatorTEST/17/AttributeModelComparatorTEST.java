import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_OneNull() {
        AttributeModel o1 = new AttributeModel("Name1");
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_EqualNames() {
        AttributeModel o1 = new AttributeModel("Name");
        AttributeModel o2 = new AttributeModel("Name");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(0, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_FirstBeforeSecond() {
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_SecondBeforeFirst() {
        AttributeModel o1 = new AttributeModel("C");
        AttributeModel o2 = new AttributeModel("A");
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(1, comparator.compare(o1, o2));
    }

}