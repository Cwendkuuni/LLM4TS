import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
    
    @Test
    public void testGetTypeInformationForArray() {
        Type type = new int[10].getClass();
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(arrayTypeInfo);
        assertEquals(type, arrayTypeInfo.getType());
    }
    
    @Test
    public void testGetTypeInformationForField() {
        Type typeDefiningF = getClass();
        Field f = getClass().getField("exampleField");
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        assertNotNull(fieldTypeInfo);
        assertTrue(fieldTypeInfo.getType() instanceof TypeVariable<?>);
    }
    
    @Test
    public void testGetActualType_Class() {
        Type type = getClass();
        Type parentType = getClass().getGenericSuperclass();
        Class<?> rawParentClass = getClass();
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertTrue(actualType instanceof Class<?>);
    }
    
    @Test
    public void testGetActualType_ParameterizedType() {
        Type type = new ParameterizedTypeImpl(getClass(), new Type[]{Integer.class}, getClass().getDeclaringClass());
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertTrue(actualType instanceof ParameterizedType);
    }
    
    @Test
    public void testGetActualType_GenericArrayType() {
        Type type = new GenericArrayTypeImpl(Integer.class);
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertTrue(actualType instanceof GenericArrayType);
    }
    
    @Test
    public void testGetActualType_TypeVariable() {
        Type type = new TypeVariableImpl("T", getClass());
        Type parentType = new ParameterizedTypeImpl(getClass(), new Type[]{Integer.class}, getClass().getDeclaringClass());
        Class<?> rawParentClass = getClass();
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertTrue(actualType instanceof TypeVariable<?>);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_UnsupportedType() {
        Type type = new WildcardTypeImpl(new Type[]{Integer.class}, new Type[]{});
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }
    
    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{Integer.class, String.class};
        Type parentType = new ParameterizedTypeImpl(getClass(), actualTypeArguments, getClass().getDeclaringClass());
        Class<?> rawParentClass = getClass();
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(realTypes);
        assertEquals(actualTypeArguments.length, realTypes.length);
    }
    
    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = new TypeVariable[]{new TypeVariableImpl("T", getClass())};
        TypeVariable<?> type = new TypeVariableImpl("T", getClass());
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, index);
    }
    
    // Example field for testGetTypeInformationForField()
    private TypeVariable<?> exampleField;
}

class ParameterizedTypeImpl implements ParameterizedType {
    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    
    ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
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

class GenericArrayTypeImpl implements GenericArrayType {
    private final Type genericComponentType;
    
    GenericArrayTypeImpl(Type genericComponentType) {
        this.genericComponentType = genericComponentType;
    }
    
    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }
}

class TypeVariableImpl implements TypeVariable<?> {
    private final String name;
    private final Type[] bounds;
    
    TypeVariableImpl(String name, Type... bounds) {
        this.name = name;
        this.bounds = bounds;
    }
    
    @Override
    public Type[] getBounds() {
        return bounds;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;
    
    WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
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