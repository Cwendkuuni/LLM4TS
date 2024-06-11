import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private List<AttributeModel> attributes;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attributes = new ArrayList<>();
    }

    @Test
    public void testCompareEqualAttributeModels() {
        AttributeModel attribute1 = new AttributeModel("attribute");
        AttributeModel attribute2 = new AttributeModel("attribute");

        assertEquals(0, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompareFirstNullAttributeModel() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("attribute");

        assertTrue(comparator.compare(attribute1, attribute2) < 0);
    }

    @Test
    public void testCompareSecondNullAttributeModel() {
        AttributeModel attribute1 = new AttributeModel("attribute");
        AttributeModel attribute2 = null;

        assertTrue(comparator.compare(attribute1, attribute2) > 0);
    }

    @Test
    public void testCompareBothNullAttributeModels() {
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;

        assertEquals(0, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompareFirstAttributeModelNullName() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel("attribute");

        assertTrue(comparator.compare(attribute1, attribute2) < 0);
    }

    @Test
    public void testCompareSecondAttributeModelNullName() {
        AttributeModel attribute1 = new AttributeModel("attribute");
        AttributeModel attribute2 = new AttributeModel(null);

        assertTrue(comparator.compare(attribute1, attribute2) > 0);
    }

    @Test
    public void testCompareBothAttributeModelsNullName() {
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel(null);

        assertEquals(0, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompareFirstAttributeModelBeforeSecondAttributeModel() {
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");

        assertTrue(comparator.compare(attribute1, attribute2) < 0);
    }

    @Test
    public void testCompareSecondAttributeModelBeforeFirstAttributeModel() {
        AttributeModel attribute1 = new AttributeModel("def");
        AttributeModel attribute2 = new AttributeModel("abc");

        assertTrue(comparator.compare(attribute1, attribute2) > 0);
    }

    @Test
    public void testCompareBothAttributeModelsBeforeA() {
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");

        attribute1.setName("g");
        attribute2.setName("h");

        assertTrue(comparator.compare(attribute1, attribute2) < 0);
    }

    @Test
    public void testCompareFirstAttributeModelBeforeAAndSecondAttributeModelAfterA() {
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");

        attribute1.setName("z");
        attribute2.setName("a");

        assertTrue(comparator.compare(attribute1, attribute2) > 0);
    }

    @Test
    public void testCompareFirstAttributeModelAfterAAndSecondAttributeModelBeforeA() {
        AttributeModel attribute1 = new AttributeModel("def");
        AttributeModel attribute2 = new AttributeModel("abc");

        attribute1.setName("a");
        attribute2.setName("z");

        assertTrue(comparator.compare(attribute1, attribute2) < 0);
    }

    @Test
    public void testCompareBothAttributeModelsSameA() {
        AttributeModel attribute1 = new AttributeModel("ab");
        AttributeModel attribute2 = new AttributeModel("ac");

        attribute1.setName("a");
        attribute2.setName("a");

        assertEquals(0, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompareNullAttributes() {
        attributes.add(null);
        attributes.add(null);

        Collections.sort(attributes, comparator);

        assertNull(attributes.get(0));
        assertNull(attributes.get(1));
    }

    @Test
    public void testCompareEmptyAttributes() {
        Collections.sort(attributes, comparator);

        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testCompareSingleAttribute() {
        AttributeModel attribute = new AttributeModel("attribute");

        attributes.add(attribute);

        Collections.sort(attributes, comparator);

        assertNotNull(attributes.get(0));
        assertEquals(attribute, attributes.get(0));
    }

    @Test
    public void testCompareMultipleAttributes() {
        AttributeModel attribute1 = new AttributeModel("abc");
        AttributeModel attribute2 = new AttributeModel("def");
        AttributeModel attribute3 = new AttributeModel("ghi");

        attributes.add(attribute2);
        attributes.add(attribute3);
        attributes.add(attribute1);

        Collections.sort(attributes, comparator);

        assertNotNull(attributes.get(0));
        assertEquals(attribute1, attributes.get(0));
        assertNotNull(attributes.get(1));
        assertEquals(attribute2, attributes.get(1));
        assertNotNull(attributes.get(2));
        assertEquals(attribute3, attributes.get(2));
    }
}