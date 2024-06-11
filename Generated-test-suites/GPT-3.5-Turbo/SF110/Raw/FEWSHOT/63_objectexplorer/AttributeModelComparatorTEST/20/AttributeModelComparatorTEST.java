package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {
    private AttributeModelComparator attributeModelComparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        List<AttributeModel> attributeModels = new ArrayList<AttributeModel>();
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("c");
        AttributeModel attributeModel3 = new AttributeModel("a a");
        AttributeModel attributeModel4 = new AttributeModel("d");
        attributeModels.add(attributeModel1);
        attributeModels.add(attributeModel2);
        attributeModels.add(attributeModel3);
        attributeModels.add(attributeModel4);

        Collections.sort(attributeModels, attributeModelComparator);

        assertEquals("a a", attributeModels.get(0).getName());
        assertEquals("b", attributeModels.get(1).getName());
        assertEquals("c", attributeModels.get(2).getName());
        assertEquals("d", attributeModels.get(3).getName());
    }
}