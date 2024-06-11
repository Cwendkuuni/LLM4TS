import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothNull_ShouldReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(null, null);
        
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNull_ShouldReturnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model2 = new AttributeModel("abc");
        
        int result = comparator.compare(null, model2);
        
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondNull_ShouldReturnPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        
        int result = comparator.compare(model1, null);
        
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothNamesNull_ShouldReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel(null);
        AttributeModel model2 = new AttributeModel(null);
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNameNull_ShouldReturnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel(null);
        AttributeModel model2 = new AttributeModel("abc");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondNameNull_ShouldReturnPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel(null);
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothNamesEqual_ShouldReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("abc");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNameBeforeA_And_SecondNameAfterA_ShouldReturnOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstNameAfterA_And_SecondNameBeforeA_ShouldReturnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("def");
        AttributeModel model2 = new AttributeModel("abc");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstNameBeforeA_And_SecondNameBeforeA_ShouldReturnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("abd");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstNameAfterA_And_SecondNameAfterA_ShouldReturnPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("def");
        AttributeModel model2 = new AttributeModel("deg");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(1, result);
    }
}