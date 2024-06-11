import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when both objects are null
        int nullResult = comparator.compare(null, null);
        assertEquals(0, nullResult);

        // Test when one object is null and the other is not null
        AttributeModel model1 = new AttributeModel("Name1");
        int nullModelResult1 = comparator.compare(null, model1);
        assertEquals(-1, nullModelResult1);

        AttributeModel model2 = new AttributeModel("Name2");
        int nullModelResult2 = comparator.compare(model2, null);
        assertEquals(1, nullModelResult2);

        // Test when neither object name is "a"
        AttributeModel model3 = new AttributeModel("Name3");
        AttributeModel model4 = new AttributeModel("Name4");
        int nonANameResult = comparator.compare(model3, model4);
        assertEquals(-1, nonANameResult);

        // Test when o1 name is "a" and o2 name is not "a"
        AttributeModel model5 = new AttributeModel("a");
        AttributeModel model6 = new AttributeModel("Name6");
        int onlyO1ANameResult = comparator.compare(model5, model6);
        assertEquals(1, onlyO1ANameResult);

        // Test when o1 name is not "a" and o2 name is "a"
        AttributeModel model7 = new AttributeModel("Name7");
        AttributeModel model8 = new AttributeModel("a");
        int onlyO2ANameResult = comparator.compare(model7, model8);
        assertEquals(-1, onlyO2ANameResult);

        // Test when both object names are "a"
        AttributeModel model9 = new AttributeModel("a");
        AttributeModel model10 = new AttributeModel("a");
        int bothANameResult = comparator.compare(model9, model10);
        assertEquals(0, bothANameResult);
    }
}