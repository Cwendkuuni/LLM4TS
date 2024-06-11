package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = mock(AttributeModel.class);
        AttributeModel attribute2 = mock(AttributeModel.class);

        when(attribute1.getName()).thenReturn("a");
        when(attribute2.getName()).thenReturn("b");
        assertEquals(-1, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("b");
        when(attribute2.getName()).thenReturn("a");
        assertEquals(1, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("a");
        when(attribute2.getName()).thenReturn("a");
        assertEquals(0, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("c");
        when(attribute2.getName()).thenReturn("c");
        assertEquals(0, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("a");
        when(attribute2.getName()).thenReturn("c");
        assertEquals(-1, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("c");
        when(attribute2.getName()).thenReturn("a");
        assertEquals(1, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn(null);
        when(attribute2.getName()).thenReturn(null);
        assertEquals(0, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn(null);
        when(attribute2.getName()).thenReturn("a");
        assertEquals(-1, comparator.compare(attribute1, attribute2));

        when(attribute1.getName()).thenReturn("a");
        when(attribute2.getName()).thenReturn(null);
        assertEquals(1, comparator.compare(attribute1, attribute2));
    }
}