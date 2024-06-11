
package de.paragon.explorer.model;

import java.util.*;
import java.io.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_equalNames() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Name");
        AttributeModel o2 = new AttributeModel("Name");
        int result = amc.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_o1Null() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Name");
        int result = amc.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_o2Null() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Name");
        AttributeModel o2 = null;
        int result = amc.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_o1NameBeforeA() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Name");
        AttributeModel o2 = new AttributeModel("a");
        int result = amc.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_o1NameAfterA() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("name");
        AttributeModel o2 = new AttributeModel("A");
        int result = amc.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_o2NameBeforeA() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("Name");
        int result = amc.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_o2NameAfterA() {
        AttributeModelComparator amc = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("A");
        AttributeModel o2 = new AttributeModel("name");
        int result = amc.compare(o1, o2);
        assertTrue(result < 0);
    }
}
