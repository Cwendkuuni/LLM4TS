package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Both inputs are null
        assertEquals(0, comparator.compare(null, null));

        // First input is null, second input is not null
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("test");
        assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));

        // First input is not null, second input is null
        attributeModel1 = new AttributeModel("test");
        attributeModel2 = null;
        assertEquals(1, comparator.compare(attributeModel1, attributeModel2));

        // Both inputs are not null, same name
        attributeModel1 = new AttributeModel("test");
        attributeModel2 = new AttributeModel("test");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));

        // Both inputs are not null, o1.name < "a", o2.name >= "a"
        attributeModel1 = new AttributeModel("abc");
        attributeModel2 = new AttributeModel("xyz");
        assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));

        // Both inputs are not null, o2.name < "a", o1.name >= "a"
        attributeModel1 = new AttributeModel("xyz");
        attributeModel2 = new AttributeModel("abc");
        assertEquals(1, comparator.compare(attributeModel1, attributeModel2));

        // Both inputs are not null, o1.name = "a", o2.name > "a"
        attributeModel1 = new AttributeModel("a");
        attributeModel2 = new AttributeModel("xyz");
        assertEquals(-1, comparator.compare(attributeModel1, attributeModel2));

        // Both inputs are not null, o1.name > "a", o2.name = "a"
        attributeModel1 = new AttributeModel("xyz");
        attributeModel2 = new AttributeModel("a");
        assertEquals(1, comparator.compare(attributeModel1, attributeModel2));
    }
}