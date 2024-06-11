import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;


public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Integer[10].getClass();
        TypeInfoArray expected = new TypeInfoArray(type);
        TypeInfoArray actual = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = ExampleClass.class.getDeclaredField("exampleField");
        Type type = field.getGenericType();
        Type parentType = ExampleClass.class;
        TypeInfo expected = new TypeInfo(String.class);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInfoForFieldWithTypeVariable() throws NoSuchFieldException {
        Field field = ExampleClass.class.getDeclaredField("exampleFieldWithTypeVariable");
        Type type = field.getGenericType();
        Type parentType = ExampleClass.class;
        TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) type;
        ParameterizedType parentParameterizedType = (ParameterizedType) parentType;
        Type[] actualTypeArguments = parentParameterizedType.getActualTypeArguments();
        TypeInfo expected = new TypeInfo(actualTypeArguments[0]);
        TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForFieldWithUnsupportedOperation() throws NoSuchFieldException {
        Field field = ExampleClass.class.getDeclaredField("exampleFieldWithUnsupportedOperation");
        Type type = field.getGenericType();
        Type parentType = ExampleClass.class;
        TypeInfoFactory.getTypeInfoForField(field, parentType);
    }

    @Test
    public void testGetActualTypeWithClass() {
        Type type = String.class;
        Type parentType = ExampleClass.class;
        Class<?> rawParentClass = ExampleClass.class;
        Type expected = String.class;
        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualTypeWithParameterizedType() {
        Type type = ExampleClassWithParameterizedType.getParameterizedList().getType();
        Type parentType = ExampleClassWithParameterizedType.getParameterizedList().getType();
        Class<?> rawParentClass = ExampleClassWithParameterizedType.class;
        Type expected = ExampleClassWithParameterizedType.getParameterizedList().getType();
        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualTypeWithGenericArrayType() {
        Type type = ExampleClassWithGenericArrayType.getGenericArray().getType();
        Type parentType = ExampleClassWithGenericArrayType.getGenericArray().getType();
        Class<?> rawParentClass = ExampleClassWithGenericArrayType.class;
        Type expected = ExampleClassWithGenericArrayType.getGenericArray().getType();
        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualTypeWithTypeVariable() {
        Type type = ExampleClassWithTypeVariable.getTypeVariableList().getType();
        Type parentType = ExampleClassWithTypeVariable.getTypeVariableList().getType();
        Class<?> rawParentClass = ExampleClassWithTypeVariable.class;
        Type expected = ExampleClassWithTypeVariable.getTypeVariableList().getType();
        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualTypeWithWildcardType() {
        Type type = ExampleClassWithWildcardType.getWildcardList().getType();
        Type parentType = ExampleClassWithWildcardType.getWildcardList().getType();
        Class<?> rawParentClass = ExampleClassWithWildcardType.class;
        Type expected = ExampleClassWithWildcardType.getWildcardList().getType();
        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeWithInvalidType() {
        Type type = Object.class;
        Type parentType = ExampleClass.class;
        Class<?> rawParentClass = ExampleClass.class;
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }
}