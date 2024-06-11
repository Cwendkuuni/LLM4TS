import org.junit.Assert;
import org.junit.Test;
import java.util.Comparator;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        AttributeModel o1 = new AttributeModel("apple");
        AttributeModel o2 = new AttributeModel("banana");
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result < 0);
    }
}