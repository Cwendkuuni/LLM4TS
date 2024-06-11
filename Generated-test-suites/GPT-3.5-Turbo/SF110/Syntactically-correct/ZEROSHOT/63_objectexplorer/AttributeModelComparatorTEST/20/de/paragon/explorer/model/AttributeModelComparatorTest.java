import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;
    private AttributeModel attributeModel3;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
        attributeModel1 = new AttributeModel("abc");
        attributeModel2 = new AttributeModel("def");
        attributeModel3 = new AttributeModel("ghi");
    }

    @Test
    public void testCompare_FirstAttributeNull() {
        int result = comparator.compare(null, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondAttributeNull() {
        int result = comparator.compare(attributeModel1, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAttributesNull() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_O1BeforeO2() {
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_O2BeforeO1() {
        int result = comparator.compare(attributeModel2, attributeModel1);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_O1EqualToO2() {
        int result = comparator.compare(attributeModel1, attributeModel1);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_O1BeforeAAndO2AfterA() {
        attributeModel1.setName("abc");
        attributeModel2.setName("bde");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_O2BeforeAAndO1AfterA() {
        attributeModel1.setName("bde");
        attributeModel2.setName("abc");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_O1BeforeAAndO2BeforeA() {
        attributeModel1.setName("abc");
        attributeModel2.setName("abd");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_O1AfterAAndO2AfterA() {
        attributeModel1.setName("zzz");
        attributeModel2.setName("zzz");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

}