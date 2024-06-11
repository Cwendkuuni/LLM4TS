package org.mockito.internal.util.reflection;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_NoGenericType_ReturnsObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nonGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_GenericField_ReturnsGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("genericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericType_ReturnsGenericClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nestedGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(List.class, result);
    }

    @Test
    public void testGetGenericType_MultipleNestedGenericType_ReturnsGenericClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("multipleNestedGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(List.class, result);
    }

    @Test
    public void testGetGenericType_NoGeneric_ReturnsObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("noGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_GenericTypeWithGenericClass_ReturnsGenericClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("genericTypeWithGenericClassField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(List.class, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericType_NullField_ThrowsNullPointerException() {
        GenericMaster genericMaster = new GenericMaster();
        genericMaster.getGenericType(null);
    }

    private Field getField(String fieldName) {
        try {
            return GenericMaster.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}