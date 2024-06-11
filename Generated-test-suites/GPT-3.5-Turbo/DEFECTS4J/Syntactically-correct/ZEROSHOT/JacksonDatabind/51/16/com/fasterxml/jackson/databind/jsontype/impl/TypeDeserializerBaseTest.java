package com.fasterxml.jackson.databind.jsontype.impl;
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
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String result = deserializer.baseTypeName();
        assertEquals("null", result);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "typeProperty", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        String result = deserializer.getPropertyName();
        assertEquals("typeProperty", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolver() {
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
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public Id getMechanism() {
                return null;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        TypeIdResolver result = deserializer.getTypeIdResolver();
        assertEquals(idResolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType() {
            @Override
            public JavaType refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces) {
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
            public JavaType withValueHandler(Object h) {
                return null;
            }

            @Override
            public JavaType withContentValueHandler(Object h) {
                return null;
            }

            @Override
            public JavaType withStaticTyping() {
                return null;
            }

            @Override
            protected JavaType _narrow(Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType narrowBy(Class<?> subclass) {
                return null;
            }

            @Override
            public JavaType narrowContentsBy(Class<?> contentClass) {
                return null;
            }

            @Override
            public JavaType widenBy(Class<?> superClass) {
                return null;
            }

            @Override
            public JavaType widenContentsBy(Class<?> contentClass) {
                return null;
            }

            @Override
            public boolean isTypeOrSubTypeOf(Class<?> type) {
                return false;
            }

            @Override
            public boolean isContainerType() {
                return false;
            }

            @Override
            public boolean hasContentType() {
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
            public boolean hasRawClass(Class<?> raw) {
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
            public boolean isThrowable() {
                return false;
            }

            @Override
            public boolean isArrayType() {
                return false;
            }

            @Override
            public boolean isEnumImplType() {
                return false;
            }

            @Override
            public Class<?> getRawClass() {
                return null;
            }

            @Override
            public Object getTypeHandler() {
                return null;
            }

            @Override
            public Object getContentTypeHandler() {
                return null;
            }

            @Override
            public Object getValueHandler() {
                return null;
            }

            @Override
            public Object getContentValueHandler() {
                return null;
            }

            @Override
            public boolean hasGenericTypes() {
                return false;
            }

            @Override
            public Class<?> getParameterSource() {
                return null;
            }

            @Override
            public boolean isReferenceType() {
                return false;
            }
        };

        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        Object result = deserializer.getDefaultImpl();
        assertEquals(defaultImpl, result);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String result = deserializer.toString();
        String message = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
        message += "; base-type:null";
        message += "; id-resolver: null";
        message += ']';
        assertEquals(message, result);
    }
}