import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        Type type = new GenericArrayTypeImpl(Integer.class);
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = SomeClass.class.getDeclaredField("someField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, SomeClass.class);
        assertEquals(field.getGenericType(), typeInfo.getType());
    }

    @Test
    public void testGetActualTypeForClass() {
        Type type = String.class;
        Type actualType = TypeInfoFactory.getActualType(type, null, null);
        assertEquals(type, actualType);
    }

    @Test
    public void testGetActualTypeForParameterizedType() {
        Type rawType = SomeClass.class;
        Type[] actualTypeArgs = { Integer.class, String.class };
        ParameterizedType parameterizedType = new ParameterizedTypeImpl(rawType, actualTypeArgs, null);

        Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);

        assertEquals(parameterizedType, actualType);
    }

    @Test
    public void testGetActualTypeForGenericArrayType() {
        Type componentType = List.class;
        GenericArrayType genericArrayType = new GenericArrayTypeImpl(componentType);

        Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);

        assertEquals(genericArrayType, actualType);
    }

    @Test
    public void testGetActualTypeForTypeVariable() {
        TypeVariable<?> typeVariable = TypeVariable.class;

        Type actualType = TypeInfoFactory.getActualType(typeVariable, null, null);

        assertNull(actualType);
    }

    @Test
    public void testGetActualTypeForWildcardType() {
        WildcardType wildcardType = new WildcardTypeImpl(new Type[] { Integer.class }, new Type[0]);

        Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);

        assertEquals(Integer.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeForInvalidType() {
        Type invalidType = new SomeInvalidType();

        TypeInfoFactory.getActualType(invalidType, null, null);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = { Integer.class, String.class };
        Type parentType = SomeClass.class;
        Class<?> rawParentClass = SomeClass.class;

        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

        assertEquals(actualTypeArguments.length, realTypes.length);
        assertEquals(Integer.class, realTypes[0]);
        assertEquals(String.class, realTypes[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexForInvalidTypeVariable() {
        TypeVariable<?>[] types = new TypeVariable[] { TypeVariable.class };
        TypeVariable<?> invalidTypeVariable = TypeVariable.class;

        TypeInfoFactory.getIndex(types, invalidTypeVariable);
    }
}

class SomeClass<T> {
    T someField;
}

class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }
}

class ParameterizedTypeImpl implements ParameterizedType {
    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}

class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
        this.upperBounds = upperBounds;
        this.lowerBounds = lowerBounds;
    }

    @Override
    public Type[] getUpperBounds() {
        return upperBounds;
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBounds;
    }
}

class SomeInvalidType {
}