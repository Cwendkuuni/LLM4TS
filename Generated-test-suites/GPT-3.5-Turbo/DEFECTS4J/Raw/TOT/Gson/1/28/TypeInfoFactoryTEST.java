import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TypeInfoFactoryTest {

    private TypeInfoFactory typeInfoFactory;

    @Before
    public void setUp() {
        typeInfoFactory = new TypeInfoFactory();
    }

    @Test
    public void getTypeInfoForArray_typical() {
        Type type = new int[10].getClass();
        TypeInfoArray typeInfoArray = typeInfoFactory.getTypeInfoForArray(type);
        
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void getTypeInfoForArray_edge() {
        Type type = new int[0].getClass();
        TypeInfoArray typeInfoArray = typeInfoFactory.getTypeInfoForArray(type);
        
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void getTypeInfoForArray_error() {
        Type type = null;
        try {
            TypeInfoArray typeInfoArray = typeInfoFactory.getTypeInfoForArray(type);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Type cannot be null", e.getMessage());
        }
    }

    @Test
    public void getTypeInfoForField_typical() {
        Field field = MyClass.class.getField("myField");
        Type typeDefiningF = MyClass.class;

        TypeInfo typeInfo = typeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        
        assertNotNull(typeInfo);
        assertEquals(String.class, typeInfo.getType());
    }

    @Test
    public void getTypeInfoForField_edge() {
        Field field = MyClass.class.getField("myField");
        Type typeDefiningF = null;

        TypeInfo typeInfo = typeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        
        assertNotNull(typeInfo);
        assertEquals(String.class, typeInfo.getType());
    }

    @Test
    public void getTypeInfoForField_error() {
        Field field = null;
        Type typeDefiningF = MyClass.class;

        try {
            TypeInfo typeInfo = typeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Field cannot be null", e.getMessage());
        }
    }

    @Test
    public void getActualType_typical_Class() {
        Type type = String.class;
        Type parentType = MyClass.class;
        Class<?> rawParentClass = MyClass.class;

        Type actualType = typeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(type, actualType);
    }

    @Test
    public void getActualType_typical_ParameterizedType() {
        ParameterizedType type = (ParameterizedType) MyClassGeneric.class.getField("myGenericList").getGenericType();
        Type parentType = MyClassGeneric.class;
        Class<?> rawParentClass = MyClassGeneric.class;

        Type actualType = typeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(List.class, actualType);
    }

    @Test
    public void getActualType_typical_GenericArrayType() {
        Class<?>[] classes = new Class<?>[10];
        Type type = classes.getClass().getComponentType();
        Type parentType = MyClassArray.class;
        Class<?> rawParentClass = MyClassArray.class;

        Type actualType = typeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(Class.class, actualType);
    }

    @Test
    public void getActualType_typical_TypeVariable_ParameterizedType() {
        ParameterizedType type = (ParameterizedType) MyClassGeneric.class.getField("myGenericTypeVariable").getGenericType();
        Type parentType = MyClassGeneric.class;
        Class<?> rawParentClass = MyClassGeneric.class;

        Type actualType = typeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(String.class, actualType);
    }
    
    // Continue with more test cases for each method...

}

class MyClass {
    public String myField;
}

class MyClassGeneric<T> {
    public List<T> myGenericList;
    public T myGenericTypeVariable;
}

class MyClassArray {
    public Class<?>[] myArray;
}