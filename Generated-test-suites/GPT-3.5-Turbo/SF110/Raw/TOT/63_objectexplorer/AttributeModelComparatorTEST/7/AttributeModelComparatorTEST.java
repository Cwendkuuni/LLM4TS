import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("abc");
        assertEquals(0, new AttributeModelComparator().compare(a1, a2));
    }
    
    @Test
    public void testCompare_SortedOrder_ReturnsNegativeValue() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("def");
        assertTrue(new AttributeModelComparator().compare(a1, a2) < 0);
    }
    
    @Test
    public void testCompare_OppositeSortedOrder_ReturnsPositiveValue() {
        AttributeModel a1 = new AttributeModel("def");
        AttributeModel a2 = new AttributeModel("abc");
        assertTrue(new AttributeModelComparator().compare(a1, a2) > 0);
    }
    
    @Test
    public void testCompare_FirstNameIsNull_ReturnsNegativeValue() {
        AttributeModel a1 = new AttributeModel(null);
        AttributeModel a2 = new AttributeModel("abc");
        assertTrue(new AttributeModelComparator().compare(a1, a2) < 0);
    }
    
    @Test
    public void testCompare_SecondNameIsNull_ReturnsPositiveValue() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel(null);
        assertTrue(new AttributeModelComparator().compare(a1, a2) > 0);
    }
    
    @Test
    public void testCompare_BothNamesAreNull_ReturnsZero() {
        AttributeModel a1 = new AttributeModel(null);
        AttributeModel a2 = new AttributeModel(null);
        assertEquals(0, new AttributeModelComparator().compare(a1, a2));
    }
    
    @Test
    public void testCompare_NamesAreCaseInsensitive_ReturnsZero() {
        AttributeModel a1 = new AttributeModel("ABC");
        AttributeModel a2 = new AttributeModel("abc");
        assertEquals(0, new AttributeModelComparator().compare(a1, a2));
    }
    
    @Test
    public void testCompare_FirstNameIsA_ReturnsPositiveValue() {
        AttributeModel a1 = new AttributeModel("a");
        AttributeModel a2 = new AttributeModel("def");
        assertTrue(new AttributeModelComparator().compare(a1, a2) > 0);
    }
    
    @Test
    public void testCompare_FirstNameIsNotA_AndSecondNameIsA_ReturnsNegativeValue() {
        AttributeModel a1 = new AttributeModel("def");
        AttributeModel a2 = new AttributeModel("a");
        assertTrue(new AttributeModelComparator().compare(a1, a2) < 0);
    }
    
    @Test
    public void testCompare_BothNamesAreA_ReturnsNegativeValue() {
        AttributeModel a1 = new AttributeModel("a");
        AttributeModel a2 = new AttributeModel("a");
        assertTrue(new AttributeModelComparator().compare(a1, a2) < 0);
    }
}