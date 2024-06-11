import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("myField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
        assertEquals(String.class, typeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithInvalidInput() {
        TypeInfoFactory.getTypeInfoForArray(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldWithInvalidInput() throws NoSuchFieldException {
        Field field = MyClass.class.getField("invalidField");
        TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
    }

    @Test
    public void testGetActualTypeWithClass() {
        Type actualType = TypeInfoFactory.getActualType(String.class, MyClass.class, MyClass.class);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualTypeWithParameterizedType() {
        Type type = new TypeToken<List<String>>() {}.getType();
        Type actualType = TypeInfoFactory.getActualType(type, MyClass.class, MyClass.class);
        assertEquals(List.class, actualType);
    }

    @Test
    public void testGetActualTypeWithGenericArrayType() {
        Type type = new TypeToken<List<String>[]>() {}.getType();
        Type actualType = TypeInfoFactory.getActualType(type, MyClass.class, MyClass.class);
        assertEquals(List[].class, actualType);
    }

    @Test
    public void testGetActualTypeWithTypeVariable() {
        Type type = new TypeToken<MyClass<String>>() {}.getType();
        Type actualType = TypeInfoFactory.getActualType(type, MyClass.class, MyClass.class);
        assertEquals(MyClass.class, actualType);
    }

    @Test
    public void testGetActualTypeWithWildcardType() {
        Type type = new TypeToken<List<? extends Number>>() {}.getType();
        Type actualType = TypeInfoFactory.getActualType(type, MyClass.class, MyClass.class);
        assertEquals(List.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeWithInvalidInput() {
        TypeInfoFactory.getActualType(new Date(), MyClass.class, MyClass.class);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{ String.class, Integer.class };
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, MyClass.class, MyClass.class);
        assertEquals(String.class, realTypes[0]);
        assertEquals(Integer.class, realTypes[1]);
    }

    @Test(expected = NullPointerException.class)
    public void testExtractRealTypesWithNullInput() {
        TypeInfoFactory.extractRealTypes(null, MyClass.class, MyClass.class);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = MyClass.class.getTypeParameters();
        TypeVariable<?> type = types[0];
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexWithInvalidInput() {
        TypeVariable<?>[] types = MyClass.class.getTypeParameters();
        TypeVariable<?> type = new TypeVariableImpl("T", MyClass.class, null, null);
        TypeInfoFactory.getIndex(types, type);
    }
}

class MyClass<T> {
    public T myField;
}