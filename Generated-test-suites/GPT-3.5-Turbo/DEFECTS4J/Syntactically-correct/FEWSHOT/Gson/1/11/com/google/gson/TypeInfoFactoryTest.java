import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {
    
    @Test
    public void testGetTypeInfoForArray() {
        Type type = new GenericArrayTypeImpl(String.class);
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }
    
    @Test
    public void testGetTypeInfoForField_Class() {
        Field field = Foo.class.getDeclaredField("bar");
        Type type = field.getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Foo.class);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField_ParameterizedType() {
        Field field = Foo.class.getDeclaredField("list");
        Type type = field.getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Foo.class);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField_GenericArrayType() {
        Field field = Foo.class.getDeclaredField("array");
        Type type = field.getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Foo.class);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField_TypeVariable() {
        Field field = Foo.class.getDeclaredField("typeVariable");
        Type type = field.getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Foo.class);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField_WildcardType() {
        Field field = Foo.class.getDeclaredField("wildcard");
        Type type = field.getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Foo.class);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }
    
    private static class Foo<T> {
        private String bar;
        private List<String> list;
        private T[] array;
        private T typeVariable;
        private List<? extends Number> wildcard;
    }
}