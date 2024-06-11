package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {
    private AttributeModelComparator comparator;
    private List<AttributeModel> attributeModels;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
        attributeModels = new ArrayList<>();
    }

    @Test
    public void testCompare_nullAttributes() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_nullAttribute() {
        AttributeModel attributeModel1 = new AttributeModel("Name1");
        AttributeModel attributeModel2 = null;

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_sameName() {
        AttributeModel attributeModel1 = new AttributeModel("Name");
        AttributeModel attributeModel2 = new AttributeModel("Name");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_sameNameIgnoreCase() {
        AttributeModel attributeModel1 = new AttributeModel("Name");
        AttributeModel attributeModel2 = new AttributeModel("name");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_attribute1BeforeAttribute2() {
        AttributeModel attributeModel1 = new AttributeModel("A");
        AttributeModel attributeModel2 = new AttributeModel("B");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_attribute2BeforeAttribute1() {
        AttributeModel attributeModel1 = new AttributeModel("B");
        AttributeModel attributeModel2 = new AttributeModel("A");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_attribute1BeforeAAndAttribute2AfterA() {
        AttributeModel attributeModel1 = new AttributeModel("A");
        AttributeModel attributeModel2 = new AttributeModel("B");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_attribute2BeforeAAndAttribute1AfterA() {
        AttributeModel attributeModel1 = new AttributeModel("B");
        AttributeModel attributeModel2 = new AttributeModel("A");

        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_emptyList() {
        Collections.sort(attributeModels, comparator);

        assertEquals(0, attributeModels.size());
    }

    @Test
    public void testCompare_singleAttribute() {
        AttributeModel attributeModel1 = new AttributeModel("Name");

        attributeModels.add(attributeModel1);

        Collections.sort(attributeModels, comparator);

        assertEquals(1, attributeModels.size());
        assertEquals("Name", attributeModels.get(0).getName());
    }

    @Test
    public void testCompare_multipleAttributes() {
        AttributeModel attributeModel1 = new AttributeModel("Z");
        AttributeModel attributeModel2 = new AttributeModel("A");
        AttributeModel attributeModel3 = new AttributeModel("B");

        attributeModels.add(attributeModel1);
        attributeModels.add(attributeModel2);
        attributeModels.add(attributeModel3);

        Collections.sort(attributeModels, comparator);

        assertEquals(3, attributeModels.size());
        assertEquals("A", attributeModels.get(0).getName());
        assertEquals("B", attributeModels.get(1).getName());
        assertEquals("Z", attributeModels.get(2).getName());
    }
}