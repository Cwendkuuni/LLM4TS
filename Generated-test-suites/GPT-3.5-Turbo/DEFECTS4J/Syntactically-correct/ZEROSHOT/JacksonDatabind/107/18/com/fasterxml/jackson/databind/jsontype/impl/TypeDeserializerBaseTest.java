package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl(null, null, null, false);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl(null, null, "type", false);
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = new TypeIdResolverImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl(null, typeIdResolver, null, false);
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl(defaultImpl, null, null, false);
        assertEquals(defaultImpl.getRawClass(), typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl(baseType, null, null, false);
        assertEquals(baseType, typeDeserializerBase.baseType());
    }

    private static class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {
        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TypeDeserializerBaseTestImpl(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }

    private static class TypeIdResolverImpl implements TypeIdResolver {
        @Override
        public void init(JavaType baseType) {}

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public String idFromValue(Object value, Class<?> clazz) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }

    private static class JavaTypeImpl implements JavaType {
        @Override
        public JavaType withTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withContentTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType narrowBy(Class<?> subclass) {
            return null;
        }

        @Override
        public boolean isTypeOrSuperTypeOf(Class<?> type) {
            return false;
        }

        @Override
        public boolean isTypeOrSubTypeOf(Class<?> type) {
            return false;
        }

        @Override
        public boolean isFinal() {
            return false;
        }

        @Override
        public boolean isContainerType() {
            return false;
        }

        @Override
        public boolean isCollectionLikeType() {
            return false;
        }

        @Override
        public boolean isReferenceType() {
            return false;
        }

        @Override
        public boolean hasContentType() {
            return false;
        }

        @Override
        public boolean hasRawClass(Class<?> clz) {
            return false;
        }

        @Override
        public boolean hasGenericTypes() {
            return false;
        }

        @Override
        public boolean hasSameRawClass(JavaType otherType) {
            return false;
        }

        @Override
        public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
            return null;
        }

        @Override
        public JavaType withContentType(JavaType contentType) {
            return null;
        }

        @Override
        public JavaType withTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType withContentTypeHandler(Object h) {
            return null;
        }

        @Override
        public JavaType narrowBy(Class<?> subclass) {
            return null;
        }

        @Override
        public JavaType widenBy(Class<?> superClass) {
            return null;
        }

        @Override
        public TypeBindings getBindings() {
            return null;
        }

        @Override
        public boolean isCollectionLikeType() {
            return false;
        }

        @Override
        public Class<?> getParameterSource() {
            return getClass();
        }

        @Override
        public JavaType getContentType() {
            return this;
        }

        @Override
        public Object getContentValueHandler() {
            return null;
        }

        @Override
        public Object getTypeHandler() {
            return null;
        }

        @Override
        public boolean hasRawClass(Class<?> clz) {
            return clz == getClass();
        }

        @Override
        public boolean hasSameRawClass(JavaType otherType) {
            return false;
        }

        @Override
        public boolean isContainerType() {
            return false;
        }

        @Override
        public boolean isReferenceType() {
            return false;
        }

        @Override
        public boolean isTypeOrSuperTypeOf(Class<?> type) {
            return false;
        }

        @Override
        public boolean isTypeOrSubTypeOf(Class<?> type) {
            return false;
        }

        @Override
        public boolean isFinal() {
            return false;
        }

        @Override
        public boolean isAbstract() {
            return false;
        }

        @Override
        public boolean isConcrete() {
            return false;
        }

        @Override
        public boolean isThrowable() {
            return false;
        }

        @Override
        public boolean isArrayType() {
            return false;
        }

        @Override
        public boolean isEnumType() {
            return false;
        }

        @Override
        public boolean isInterface() {
            return false;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }

        @Override
        public boolean isPrimitiveOrWrapper() {
            return false;
        }

        @Override
        public boolean hasGenericTypes() {
            return false;
        }

        @Override
        public int containedTypeCount() {
            return 0;
        }

        @Override
        public String containedTypeName(int index) {
            return null;
        }

        @Override
        public Class<?> getParameterSource() {
            return getClass();
        }

        @Override
        public JavaType getContentType() {
            return this;
        }

        @Override
        public Object getContentValueHandler() {
            return null;
        }

        @Override
        public Object getTypeHandler() {
            return null;
        }

        @Override
        public ValueHandlerDecorator getValueHandlerDecorator() {
            return null;
        }
    }
}