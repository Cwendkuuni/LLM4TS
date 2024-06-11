import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attr1;
    private AttributeModel attr2;
    private AttributeModel attr3;
    private AttributeModel attr4;
    private AttributeModel attr5;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attr1 = new AttributeModel("apple");
        attr2 = new AttributeModel("banana");
        attr3 = new AttributeModel("cherry");
        attr4 = new AttributeModel("A");
        attr5 = new AttributeModel("a");
    }
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        int result = comparator.compare(attr1, attr1);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1Null_ReturnsNegative() {
        int result = comparator.compare(null, attr1);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O2Null_ReturnsPositive() {
        int result = comparator.compare(attr1, null);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_BothNull_ReturnsZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1NameBeforeO2_ReturnsNegative() {
        int result = comparator.compare(attr1, attr2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O1NameAfterO2_ReturnsPositive() {
        int result = comparator.compare(attr2, attr1);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_O1BeforeAAndO2AfterAReturnsNegative() {
        int result = comparator.compare(attr4, attr2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O1AfterAAndO2BeforeAReturnsPositive() {
        int result = comparator.compare(attr2, attr4);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_O1EqualToAAndO2AfterAReturnsPositive() {
        int result = comparator.compare(attr5, attr2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_O1AfterAAndO2EqualToAReturnsNegative() {
        int result = comparator.compare(attr2, attr5);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O1EqualToAAndO2BeforeAReturnsNegative() {
        int result = comparator.compare(attr5, attr4);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O1BeforeAAndO2EqualToAReturnsPositive() {
        int result = comparator.compare(attr4, attr5);
        assertTrue(result > 0);
    }
    
}