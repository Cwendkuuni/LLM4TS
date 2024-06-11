import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("A");
        AttributeModel model2 = new AttributeModel("B");
        assertEquals(-1, comparator.compare(model1, model2));
        assertEquals(1, comparator.compare(model2, model1));

        model1 = new AttributeModel("C");
        model2 = new AttributeModel("B");
        assertEquals(1, comparator.compare(model1, model2));
        assertEquals(-1, comparator.compare(model2, model1));

        model1 = new AttributeModel("A");
        model2 = new AttributeModel("a");
        assertEquals(-1, comparator.compare(model1, model2));
        assertEquals(1, comparator.compare(model2, model1));

        model1 = new AttributeModel("a");
        model2 = new AttributeModel("a");
        assertEquals(0, comparator.compare(model1, model2));
        assertEquals(0, comparator.compare(model2, model1));

        model1 = new AttributeModel("a");
        model2 = new AttributeModel("B");
        assertEquals(-1, comparator.compare(model1, model2));
        assertEquals(1, comparator.compare(model2, model1));
    }
}