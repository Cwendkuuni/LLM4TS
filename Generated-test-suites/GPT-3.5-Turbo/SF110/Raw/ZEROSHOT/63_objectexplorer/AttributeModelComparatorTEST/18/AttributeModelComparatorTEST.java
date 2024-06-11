package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(null, null);
        assertEquals(0, result);

        AttributeModel attribute1 = new AttributeModel("attribute1");
        result = comparator.compare(attribute1, null);
        assertTrue(result > 0);

        AttributeModel attribute2 = new AttributeModel("attribute2");
        result = comparator.compare(null, attribute2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("attribute1");
        AttributeModel attribute2 = new AttributeModel("attribute2");
        int result = comparator.compare(attribute1, attribute2);
        assertTrue(result < 0);

        attribute1 = new AttributeModel("attribute2");
        attribute2 = new AttributeModel("attribute1");
        result = comparator.compare(attribute1, attribute2);
        assertTrue(result > 0);

        attribute1 = new AttributeModel("attribute1");
        attribute2 = new AttributeModel("attribute1");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareAttributesWithA() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("attribute1");
        AttributeModel attribute2 = new AttributeModel("attribute2");
        int result = comparator.compare(attribute1, attribute2);
        assertTrue(result < 0);

        attribute1 = new AttributeModel("a");
        attribute2 = new AttributeModel("attribute1");
        result = comparator.compare(attribute1, attribute2);
        assertTrue(result > 0);

        attribute1 = new AttributeModel("attribute1");
        attribute2 = new AttributeModel("a");
        result = comparator.compare(attribute1, attribute2);
        assertTrue(result < 0);

        attribute1 = new AttributeModel("a");
        attribute2 = new AttributeModel("a");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }
}