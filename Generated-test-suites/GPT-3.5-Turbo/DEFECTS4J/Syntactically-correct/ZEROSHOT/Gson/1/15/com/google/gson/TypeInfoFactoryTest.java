import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);
        assertEquals(type, result.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field f = TypeInfoFactory.class.getDeclaredField("getTypeInfoForField");
        Type typeDefiningF = TypeInfoFactoryTest.class;
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        assertNotNull(result);
        assertEquals(f.getGenericType(), result.getType());
    }

    @Test
    public void testGetActualTypeWithClass() {
        Type type = new Type() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, result);
    }

    @Test
    public void testGetActualTypeWithParameterizedType() {
        ParameterizedType type = new ParameterizedTypeImpl(new Type() {}, new Type[] {new Type() {}}, new Type() {});
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type.getRawType(), result);
    }

    @Test
    public void testGetActualTypeWithGenericArrayType() {
        GenericArrayType type = new GenericArrayTypeImpl(new Type() {});
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualTypeWithTypeVariableNoParameterizedType() {
        TypeVariable<?> type = new TypeVariableImpl(new Type() {});
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    @Test
    public void testGetActualTypeWithTypeVariableInParameterizedType() {
        TypeVariable<?> typeVariable = new TypeVariableImpl(new Type() {});
        TypeVariable<?>[] classTypeVariables = {typeVariable};
        ParameterizedType parentType = new ParameterizedTypeImpl(new Type() {}, new Type[] {}, null);
        Type[] actualTypeArguments = {new Type() {}};
        Type result = TypeInfoFactory.getActualType(typeVariable, parentType, classTypeVariables);
        assertEquals(actualTypeArguments[0], result);
    }

    @Test
    public void testGetActualTypeWithWildcardType() {
        WildcardType type = new WildcardTypeImpl(new Type() {});
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type.getUpperBounds()[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeWithInvalidType() {
        TypeInfoFactory.getActualType(new Type() {}, new Type() {}, TypeInfoFactoryTest.class);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {new Type() {}, new Type() {}};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals(actualTypeArguments.length, result.length);
        for (int i = 0; i < actualTypeArguments.length; i++) {
            assertEquals(actualTypeArguments[i], result[i]);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexWithMissingTypeVariable() {
        TypeVariable<?>[] types = {};
        TypeVariable<?> type = new TypeVariableImpl(new Type() {});
        TypeInfoFactory.getIndex(types, type);
    }
}

class TypeVariableImpl implements TypeVariable<TypeVariable<?>> {
    private Type[] bounds;

    public TypeVariableImpl(Type[] bounds) {
        this.bounds = bounds;
    }

    @Override
    public Type[] getBounds() {
        return bounds;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getSimpleName() {
        return null;
    }

    @Override
    public Type getGenericDeclaration() {
        return null;
    }

    @Override
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
        return null;
    }
}

class WildcardTypeImpl implements WildcardType {
    private Type[] upperBounds;

    public WildcardTypeImpl(Type[] upperBounds) {
        this.upperBounds = upperBounds;
    }

    @Override
    public Type[] getUpperBounds() {
        return upperBounds;
    }

    @Override
    public Type[] getLowerBounds() {
        return null;
    }

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

class ParameterizedTypeImpl implements ParameterizedType {
    private Type rawType;
    private Type[] actualTypeArguments;
    private Type ownerType;

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

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

class GenericArrayTypeImpl implements GenericArrayType {
    private Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}