package de.paragon.explorer.modelUnitTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
        attributeModel1 = new AttributeModel("name1");
        attributeModel2 = new AttributeModel("name2");
    }
    
    @Test
    public void testCompare_WhenBothAttributesAreNull_ThenReturnNegativeOne() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeOneIsNullAndAttributeTwoIsNotNull_ThenReturnNegativeOne() {
        int result = comparator.compare(null, attributeModel1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeOneIsNotNullAndAttributeTwoIsNull_ThenReturnPositiveOne() {
        int result = comparator.compare(attributeModel1, null);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeNamesAreEqual_ThenReturnZero() {
        attributeModel1.setName("name");
        attributeModel2.setName("name");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_WhenAttributeOnePrecedesAttributeTwoAlphabetically_ThenReturnNegativeOne() {
        attributeModel1.setName("aaa");
        attributeModel2.setName("bbb");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeOneFollowsAttributeTwoAlphabetically_ThenReturnPositiveOne() {
        attributeModel1.setName("bbb");
        attributeModel2.setName("aaa");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeOneMatchesAsStringAndAttributeTwoDoesNot_ThenReturnPositiveOne() {
        attributeModel1.setName(AttributeModelComparator.A_AS_STRING);
        attributeModel2.setName("bbb");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_WhenAttributeTwoMatchesAsStringAndAttributeOneDoesNot_ThenReturnNegativeOne() {
        attributeModel1.setName("bbb");
        attributeModel2.setName(AttributeModelComparator.A_AS_STRING);
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_WhenBothAttributesMatchAsStrings_ThenReturnZero() {
        attributeModel1.setName(AttributeModelComparator.A_AS_STRING);
        attributeModel2.setName(AttributeModelComparator.A_AS_STRING);
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }
}