package de.paragon.explorer.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {

    private AttributeModelComparator attributeModelComparator;
    private List<AttributeModel> attributeModels;

    @Before
    public void setUp() {
        attributeModelComparator = new AttributeModelComparator();
        attributeModels = new ArrayList<>();
        attributeModels.add(new AttributeModel("B"));
        attributeModels.add(new AttributeModel("C"));
        attributeModels.add(new AttributeModel("a"));
        attributeModels.add(new AttributeModel("d"));
    }

    @Test
    public void testCompare() {
        Collections.sort(attributeModels, attributeModelComparator);
        Assert.assertEquals("a", attributeModels.get(0).getName());
        Assert.assertEquals("B", attributeModels.get(1).getName());
        Assert.assertEquals("C", attributeModels.get(2).getName());
        Assert.assertEquals("d", attributeModels.get(3).getName());
    }
}