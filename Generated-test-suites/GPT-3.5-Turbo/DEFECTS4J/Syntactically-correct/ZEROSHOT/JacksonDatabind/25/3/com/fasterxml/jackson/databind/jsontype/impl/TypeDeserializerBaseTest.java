package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idRes, typeDeserializerBase._idResolver);
        assertEquals("typePropertyName", typeDeserializerBase._typePropertyName);
        assertTrue(typeDeserializerBase._typeIdVisible);
        assertEquals(String.class, typeDeserializerBase._defaultImpl.getRawClass());
        assertNull(typeDeserializerBase._property);
    }

    @Test
    public void testConstructorWithProperty() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        BeanProperty property = new BeanPropertyImpl();
        TypeDeserializerBase src = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(src, property);
        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idRes, typeDeserializerBase._idResolver);
        assertEquals("typePropertyName", typeDeserializerBase._typePropertyName);
        assertTrue(typeDeserializerBase._typeIdVisible);
        assertEquals(String.class, typeDeserializerBase._defaultImpl.getRawClass());
        assertEquals(property, typeDeserializerBase._property);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals(idRes, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals(String.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeIdResolver idRes = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idRes, "typePropertyName", true, String.class);
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl; base-type:java.lang.Object; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl]", typeDeserializerBase.toString());
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        protected TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        protected TypeDeserializerBaseImpl(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }
    }

    private static class TypeIdResolverImpl implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {
        }

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
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return JsonTypeInfo.Id.CUSTOM;
        }
    }

    private static class JavaTypeImpl extends JavaType {

        protected JavaTypeImpl() {
            super(Object.class);
        }

        @Override
        public Class<?> getRawClass() {
            return Object.class;
        }

        @Override
        public boolean hasRawClass(Class<?> clz) {
            return false;
        }
    }

    private static class BeanPropertyImpl implements BeanProperty {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public PropertyName getWrapperName() {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
            return null;
        }
    }
}