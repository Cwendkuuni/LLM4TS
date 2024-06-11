package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class GenericMasterTest {
    
    private GenericMaster genericMaster;
    
    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_GenericField_ReturnsType() {
        Field field = TestClass.class.getDeclaredField("genericField");
        Class actual = genericMaster.getGenericType(field);
        assertEquals(String.class, actual);
    }
    
    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectType() {
        Field field = TestClass.class.getDeclaredField("nonGenericField");
        Class actual = genericMaster.getGenericType(field);
        assertEquals(Object.class, actual);
    }
    
    @Test
    public void testGetGenericType_NullField_ReturnsObjectType() {
        Class actual = genericMaster.getGenericType(null);
        assertEquals(Object.class, actual);
    }
    
    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_InvalidField_ThrowsNoSuchFieldException() throws NoSuchFieldException {
        genericMaster.getGenericType(TestClass.class.getDeclaredField("invalidField"));
    }
    
    // Test class for reflection
    private static class TestClass {
        private List<String> genericField;
        private String nonGenericField;
    }
}