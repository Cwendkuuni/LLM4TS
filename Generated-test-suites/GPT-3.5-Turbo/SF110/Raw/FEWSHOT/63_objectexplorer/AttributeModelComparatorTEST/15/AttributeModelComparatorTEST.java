package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare() {
        AttributeModel attr1 = new AttributeModel();
        attr1.setName("a");

        AttributeModel attr2 = new AttributeModel();
        attr2.setName("b");

        assertEquals(-1, comparator.compare(attr1, attr2));
        assertEquals(1, comparator.compare(attr2, attr1));

        attr1.setName("A");
        assertEquals(0, comparator.compare(attr1, attr2));
        assertEquals(0, comparator.compare(attr2, attr1));

        attr1.setName("c");
        attr2.setName("z");
        assertTrue(comparator.compare(attr1, attr2) < 0);
        assertTrue(comparator.compare(attr2, attr1) > 0);

        attr1 = null;
        attr2.setName("a");
        assertEquals(-1, comparator.compare(attr1, attr2));

        attr1 = new AttributeModel();
        attr1.setName("b");
        attr2 = null;
        assertEquals(1, comparator.compare(attr1, attr2));

        attr1 = null;
        attr2 = null;
        assertEquals(0, comparator.compare(attr1, attr2));
    }
}