package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_bothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(null, null));
    }

    @Test
    public void testCompare_o2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("test1");
        assertEquals(-1, comparator.compare(o1, null));
    }

    @Test
    public void testCompare_o1Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o2 = new AttributeModel("test2");
        assertEquals(-1, comparator.compare(null, o2));
    }

    @Test
    public void testCompare_sameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("test");
        AttributeModel o2 = new AttributeModel("test");
        assertEquals(0, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_differentNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("test1");
        AttributeModel o2 = new AttributeModel("test2");
        assertEquals(-1, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_edgeCaseDifferentNamesA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("aaa");
        AttributeModel o2 = new AttributeModel("bbb");
        assertEquals(-1, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_edgeCaseSameNamesA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("aaa");
        AttributeModel o2 = new AttributeModel("aaa");
        assertEquals(0, comparator.compare(o1, o2));
    }

    @Test
    public void testCompare_edgeCaseNullNames() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel(null);
        AttributeModel o2 = new AttributeModel(null);
        assertEquals(-1, comparator.compare(o1, o2));
    }

}