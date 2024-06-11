import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type type = new int[10].getClass().getComponentType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals("Expected TypeInfo for array", TypeInfo.ARRAY, typeInfoArray.type);
        assertEquals("Expected int for array component type", int.class, typeInfoArray.componentType);
    }

    @Test
    public void testTypeInfoForField_ClassType() throws NoSuchFieldException {
        Class<Integer> clazz = Integer.class;
        Field field = clazz.getField("value");
        Type type = field.getType();
        Type parentType = clazz;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("Expected Int for field type", int.class, typeInfo.type);
    }

    @Test
    public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
        class MyClass<T> {
            T field;
        }

        Field field = MyClass.class.getField("field");
        Type type = field.getGenericType();
        Type parentType = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("Expected TypeVariable for field type", MyClass.class.getTypeParameters()[0], typeInfo.type);
    }

    @Test
    public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException {
        class MyClass<T> {
            T field;
        }

        Field field = MyClass.class.getField("field");
        Type type = field.getGenericType();
        Type parentType = MyClass.class;
        TypeVariable<?> typeVariable = (TypeVariable<?>) type;
        int index = 0;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        Type actualType = typeInfo.type;
        assertEquals("Expected TypeVariable for field type", typeVariable, actualType);
        TypeVariable<?> expectedTypeVariable = MyClass.class.getTypeParameters()[0];
        assertEquals("Expected matching type variable index", index, TypeInfoFactory.getIndex(expectedTypeVariable.getGenericDeclaration().getTypeParameters(), typeVariable));
    }

    @Test
    public void testTypeInfoForField_WildcardType() throws NoSuchFieldException {
        class MyClass {
            List<? extends Number> list;
        }

        Field field = MyClass.class.getField("list");
        Type type = field.getGenericType();
        Type parentType = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        Type actualType = typeInfo.type;
        // WildcardType is not supported, assuming Object for testing
        assertEquals("Expected Object for field type", Object.class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTypeInfoForField_UnsupportedType() throws NoSuchFieldException {
        class MyClass {
            MyClass myField;
        }

        Field field = MyClass.class.getField("myField");
        Type type = field.getGenericType();
        Type parentType = MyClass.class;
        TypeInfoFactory.getTypeInfoForField(field, parentType);
    }

    @Test
    public void testGetActualType_ClassType() {
        Class<?> rawParentClass = Integer.class;
        Type parentType = rawParentClass.getGenericSuperclass();
        Type typeToEvaluate = int[].class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals("Expected int[] for typeToEvaluate", int[].class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Class<MyList<?>> rawParentClass = MyList.class;
        Type parentType = rawParentClass.getGenericSuperclass();
        Type typeToEvaluate = new TypeToken<List<Integer>>(){}.getType();
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals("Expected List<Integer> for typeToEvaluate", List.class, actualType.getComponentType());
        assertEquals("Expected List for typeToEvaluate", List.class, actualType.getRawType());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Class<MyList<?>> rawParentClass = MyList.class;
        Type parentType = rawParentClass.getGenericSuperclass();
        Type typeToEvaluate = new TypeToken<List<List<?>>>() {}.getType();
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals("Expected List<List<?>> for typeToEvaluate", List.class, actualType.getComponentType().getComponentType());
        assertEquals("Expected List for typeToEvaluate", List.class, actualType.getComponentType());
        assertEquals("Expected List for typeToEvaluate", List.class, actualType.getRawType());
    }

    @Test
    public void testGetActualType_TypeVariable() {
        Class<MyClass<T>> rawParentClass = MyClass.class;
        Type parentType = rawParentClass.getGenericSuperclass();
        Type typeToEvaluate = new TypeToken<MyClass<String>>(){}.getType();
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        TypeVariable<?> expectedTypeVariable = MyClass.class.getTypeParameters()[0];
        assertEquals("Expected String for typeToEvaluate", expectedTypeVariable.getBounds()[0], actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_UnsupportedType() {
        Class<MyClass<T>> rawParentClass = MyClass.class;
        Type parentType = rawParentClass.getGenericSuperclass();
        Type typeToEvaluate = MyClass.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] types = new Type[]{new TypeToken<List<String>>(){}.getType(), new TypeToken<List<Integer>>(){}.getType()};
        Type parentType = new TypeToken<List<?>>(){}.getType();
        Class<?> rawParentClass = List.class;
        Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
        assertEquals("Expected List<String> for type 0", List.class, actualTypes[0].getComponentType());
        assertEquals("Expected List<Integer> for type 1", List.class, actualTypes[1].getComponentType());
        assertEquals("Expected List for parentType", List.class, parentType.getComponentType());
        assertEquals("Expected List for typeToEvaluate 0", List.class, actualTypes[0].getRawType());
        assertEquals("Expected List for typeToEvaluate 1", List.class, actualTypes[1].getRawType());
        assertEquals("Expected List for extractedTypes length", 2, actualTypes.length);
    }

    private static class MyClass<T> {
        T field;
    }

    private static class MyList<T> extends ArrayList<T> {
    }
}