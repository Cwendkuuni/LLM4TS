package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("A");
        AttributeModel model2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }
}