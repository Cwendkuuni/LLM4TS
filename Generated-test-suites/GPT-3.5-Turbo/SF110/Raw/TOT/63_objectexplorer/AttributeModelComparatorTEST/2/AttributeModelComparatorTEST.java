import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModel attr1 = new AttributeModel("test");
        AttributeModel attr2 = new AttributeModel("test");
        int result = comparator.compare(attr1, attr2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_NullAttributes_ReturnsNegativeOne() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullAttribute1_ReturnsNegativeOne() {
        AttributeModel attr = new AttributeModel("test");
        int result = comparator.compare(null, attr);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullAttribute2_ReturnsNegativeOne() {
        AttributeModel attr = new AttributeModel("test");
        int result = comparator.compare(attr, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Attribute1NullName_ReturnsNegativeOne() {
        AttributeModel attr1 = new AttributeModel(null);
        AttributeModel attr2 = new AttributeModel("test");
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Attribute2NullName_ReturnsNegativeOne() {
        AttributeModel attr1 = new AttributeModel("test");
        AttributeModel attr2 = new AttributeModel(null);
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Attribute1BeforeA_ReturnsNegativeOne() {
        AttributeModel attr1 = new AttributeModel("aardvark");
        AttributeModel attr2 = new AttributeModel("banana");
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Attribute2BeforeA_ReturnsPositiveOne() {
        AttributeModel attr1 = new AttributeModel("banana");
        AttributeModel attr2 = new AttributeModel("apple");
        int result = comparator.compare(attr1, attr2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_Attribute1AfterAndEqualsA_ReturnsNegativeOne() {
        AttributeModel attr1 = new AttributeModel("banana");
        AttributeModel attr2 = new AttributeModel("aardvark");
        int result = comparator.compare(attr1, attr2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Attribute2AfterAndEqualsA_ReturnsPositiveOne() {
        AttributeModel attr1 = new AttributeModel("apple");
        AttributeModel attr2 = new AttributeModel("banana");
        int result = comparator.compare(attr1, attr2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_Attribute1AndAttribute2BeforeA_ReturnsZero() {
        AttributeModel attr1 = new AttributeModel("ant");
        AttributeModel attr2 = new AttributeModel("apple");
        int result = comparator.compare(attr1, attr2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_Attribute1AndAttribute2AfterA_ReturnsZero() {
        AttributeModel attr1 = new AttributeModel("banana");
        AttributeModel attr2 = new AttributeModel("zebra");
        int result = comparator.compare(attr1, attr2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_AscendingOrder_Succeeds() {
        List<AttributeModel> attributes = generateAttributeList();
        Collections.sort(attributes, comparator);

        for (int i = 0; i < attributes.size() - 1; i++) {
            AttributeModel current = attributes.get(i);
            AttributeModel next = attributes.get(i + 1);
            int result = comparator.compare(current, next);
            assertTrue(result <= 0);
        }
    }

    @Test
    public void testCompare_DescendingOrder_Succeeds() {
        List<AttributeModel> attributes = generateAttributeList();
        Collections.sort(attributes, comparator.reversed());

        for (int i = 0; i < attributes.size() - 1; i++) {
            AttributeModel current = attributes.get(i);
            AttributeModel next = attributes.get(i + 1);
            int result = comparator.compare(current, next);
            assertTrue(result >= 0);
        }
    }

    private List<AttributeModel> generateAttributeList() {
        List<AttributeModel> attributes = new ArrayList<>();
        attributes.add(new AttributeModel("ant"));
        attributes.add(new AttributeModel("apple"));
        attributes.add(new AttributeModel("banana"));
        attributes.add(new AttributeModel("zebra"));
        return attributes;
    }
}