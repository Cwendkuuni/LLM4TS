package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {
    
    @Test
    public void testGetTypeForArray() {
        Type arrayType = new int[0].getClass();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        
        assertNotNull(typeInfoArray);
        assertEquals(arrayType, typeInfoArray.getType());
    }
    
    @Test
    public void testGetTypeInfoForField_withTypeVariable() throws NoSuchFieldException {
        Field field = GenericClass.class.getDeclaredField("field");
        Type typeDefiningF = GenericClass.class;
        
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        
        assertNotNull(typeInfo);
        assertEquals(GenericClass.class, typeInfo.getRawType());
        assertEquals(String.class, typeInfo.getRawActualTypeArguments()[0]);
    }
    
    @Test
    public void testGetTypeInfoForField_withWildcardType() throws NoSuchFieldException {
        Field field = WildcardClass.class.getDeclaredField("field");
        Type typeDefiningF = WildcardClass.class;
        
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        
        assertNotNull(typeInfo);
        assertEquals(WildcardClass.class, typeInfo.getRawType());
        assertEquals(String.class, typeInfo.getRawActualTypeArguments()[0]);
    }
    
    @Test
    public void testGetActualType_withClassType() {
        Type type = String.class;
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        
        assertEquals(type, actualType);
    }
    
    @Test
    public void testGetActualType_withParameterizedType() {
        Type type = new ParameterizedTypeImpl(ParameterizedClass.class, new Type[]{String.class}, null);
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        
        assertEquals(type, actualType);
    }
    
    @Test
    public void testGetActualType_withGenericArrayType() {
        Type componentType = String.class;
        Type type = new GenericArrayTypeImpl(componentType);
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        
        assertEquals(type, actualType);
    }
    
    @Test
    public void testGetActualType_withTypeVariable() {
        TypeVariable<?> typeToEvaluate = TypeVariableClass.class.getTypeParameters()[0];
        ParameterizedType parentType = new ParameterizedTypeImpl(ParameterizedClass.class, new Type[]{String.class}, null);
        Class<?> rawParentClass = ParameterizedClass.class;
        
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        
        assertEquals(String.class, actualType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_withUnsupportedTypeVariable() {
        TypeVariable<?> typeToEvaluate = TypeVariableClass.class.getTypeParameters()[0];
        ParameterizedType parentType = null;
        Class<?> rawParentClass = ParameterizedClass.class;
        
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }
    
    @Test
    public void testGetActualType_withWildcardType() {
        WildcardType wildcardType = new WildcardTypeImpl(new Type[]{String.class}, new Type[]{Object.class});
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
        
        assertEquals(String.class, actualType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withInvalidType() {
        Type typeToEvaluate = Integer.class;
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }
    
    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{String.class, Integer.class};
        Type parentType = null;
        Class<?> rawParentClass = null;
        
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        
        assertEquals(actualTypeArguments.length, realTypes.length);
        assertEquals(String.class, realTypes[0]);
        assertEquals(Integer.class, realTypes[1]);
    }
    
    @Test
    public void testGetIndex_found() {
        TypeVariable<?>[] types = TypeVariableClass.class.getTypeParameters();
        TypeVariable<?> type = types[0];
        
        int index = TypeInfoFactory.getIndex(types, type);
        
        assertEquals(0, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetIndex_notFound() {
        TypeVariable<?>[] types = TypeVariableClass.class.getTypeParameters();
        TypeVariable<?> type = new TypeVariableImpl("T", TypeVariableClass.class);
        
        TypeInfoFactory.getIndex(types, type);
    }
    
    private static class GenericClass<X> {
        private X field;
    }
    
    private static class WildcardClass<X extends String> {
        private X field;
    }
    
    private static class TypeVariableClass<T> {
        private T field;
    }
    
    private static class TypeVariableImpl<D extends GenericClass<T>, T> implements TypeVariable<D> {
        private String name;
        private D genericDeclaration;
        
        public TypeVariableImpl(String name, D genericDeclaration) {
            this.name = name;
            this.genericDeclaration = genericDeclaration;
        }
        
        @Override
        public Type[] getBounds() {
            return new Type[]{Object.class};
        }
    
        @Override
        public D getGenericDeclaration() {
            return genericDeclaration;
        }
    
        @Override
        public String getName() {
            return name;
        }
    }
    
    private static class ParameterizedClass<X> {
        private X field;
    }
    
    private static class ParameterizedTypeImpl implements ParameterizedType {
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
        
    }
    
    private static class GenericArrayTypeImpl implements GenericArrayType {
        private Type genericComponentType;
        
        public GenericArrayTypeImpl(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }
        
        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }
        
    }
    
    private static class TypeInfoArray {
        private Type type;
        
        public TypeInfoArray(Type type) {
            this.type = type;
        }
        
        public Type getType() {
            return type;
        }
    }
    
    private static class TypeInfo {
        private Type rawType;
        private Type[] rawActualTypeArguments;
        
        public TypeInfo(Type rawType) {
            this.rawType = rawType;
        }
        
        public Type getRawType() {
            return rawType;
        }
        
        public Type[] getRawActualTypeArguments() {
            return rawActualTypeArguments;
        }
    }
    
    private static class WildcardTypeImpl implements WildcardType {
        private Type[] upperBounds;
        private Type[] lowerBounds;
        
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
}