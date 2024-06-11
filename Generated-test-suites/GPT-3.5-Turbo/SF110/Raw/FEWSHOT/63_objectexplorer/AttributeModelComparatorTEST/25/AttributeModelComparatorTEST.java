package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
        AttributeModel model3 = new AttributeModel("ghi");
        AttributeModel model4 = new AttributeModel("jkl");

        List<AttributeModel> models = Arrays.asList(model1, model2, model3, model4);
        List<AttributeModel> expected = Arrays.asList(model1, model4, model3, model2);

        Collections.sort(models, comparator);
        
        assertEquals(expected, models);
    }

    @Test
    public void testCompareWithNullModels() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = null;

        List<AttributeModel> models = Arrays.asList(model1, model2);

        Collections.sort(models, comparator);

        assertEquals(Arrays.asList(null, model1), models);
    }

    @Test
    public void testCompareWithSameName() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("abc");

        List<AttributeModel> models = Arrays.asList(model1, model2);

        Collections.sort(models, comparator);

        assertTrue(models.indexOf(model1) < models.indexOf(model2));
    }

    @Test
    public void testCompareWithBothNullModels() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;

        List<AttributeModel> models = Arrays.asList(model1, model2);

        Collections.sort(models, comparator);

        assertEquals(Arrays.asList(null, null), models);
    }
}