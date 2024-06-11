package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeBinding;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedGenericType;
import com.fasterxml.jackson.databind.json.JsonTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() throws Exception {
        // Test constructor from TypeBase
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        TypeBase collectionType = new TypeBase(Collection.class, null, null, null, null, null, null, false);
        CollectionLikeType clt = new CollectionLikeType(collectionType, String.class);
        assertEquals(javaType, clt.getRawType());
        assertEquals(String.class, clt.getErasedType());
        assertEquals(String.class, clt.getContentType());
        assertNull(clt.getSuperClass());
        assertNull(clt.getSuperInterfaces());
        assertEquals(String.class, clt.getContentType());

        // Test constructor from JavaType
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType clazz = TypeFactory.defaultInstance().constructType(Collection.class, elemT);
        CollectionLikeType cltFromJavaType = CollectionLikeType.construct(clazz, null);
        assertEquals(clazz, cltFromJavaType.getRawType());
        assertEquals(elemT, cltFromJavaType.getContentType());
        assertEquals(Collection.class, cltFromJavaType.getErasedType());

        // Test upgradeFrom method
        JavaType baseType = TypeFactory.defaultInstance().constructType(ArrayList.class, elemT);
        JavaType upgradedType = CollectionLikeType.upgradeFrom(baseType, elemT);
        CollectionLikeType cltFromUpgrade = (CollectionLikeType) upgradedType;
        assertEquals(Collection.class, cltFromUpgrade.getErasedType());
        assertEquals(elemT, cltFromUpgrade.getContentType());
    }

    @Test
    public void testCollectionLikeTypeMethods() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType clt = (CollectionLikeType) javaType;

        // Test isContainerType()
        assertEquals(true, clt.isContainerType());

        // Test isCollectionLikeType()
        assertEquals(true, clt.isCollectionLikeType());

        // Test getContentType()
        assertEquals(String.class, clt.getContentType());

        // Test getContentValueHandler()
        TypeHandler<?> contentHandler = clt.getContentValueHandler();
        assertNotNull(contentHandler);

        // Test getContentTypeHandler()
        TypeHandler<?> contentTypeHandler = clt.getContentTypeHandler();
        assertNotNull(contentTypeHandler);

        // Test hasHandlers()
        assertTrue(clt.hasHandlers());

        // Test isTrueCollectionType()
        assertTrue(clt.isTrueCollectionType());

        // Test equals()
        JavaType otherJavaType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.class);
        CollectionLikeType otherClt = (CollectionLikeType) otherJavaType;
        assertFalse(clt.equals(otherClt));

        JavaType sameJavaType = javaType;
        CollectionLikeType sameClt = (CollectionLikeType) sameJavaType;
        assertTrue(clt.equals(sameClt));

        // Test toString()
        String cltToString = clt.toString();
        assertTrue(cltToString.contains("collection-like type"));
        assertTrue(cltToString.contains("class Collection"));
        assertTrue(cltToString.contains("contains " + String.class.getName()));

        // Test getErasedSignature()
        StringBuilder erasedSignature = new StringBuilder();
        clt.getErasedSignature(erasedSignature);
        assertEquals("[Ljava.util.Collection;", erasedSignature.toString());

        // Test getGenericSignature()
        StringBuilder genericSignature = new StringBuilder();
        clt.getGenericSignature(genericSignature);
        assertEquals("[Ljava.util.Collection;<T>", genericSignature.toString());

        // Test buildCanonicalName()
        String canonicalName = clt.buildCanonicalName();
        assertEquals("class java.util.Collection<T>", canonicalName);

        // Test getElementType()
        JavaType elemType = clt.getElementType();
        assertEquals(String.class, elemType.getRawClass());

        // Test getTypeSerializer()
        TypeSerializer serializer = clt.getTypeSerializer();
        assertTrue(serializer instanceof JsonTypeSerializer);

        // Test getTypeDeserializer()
        TypeDeserializer deserializer = clt.getTypeDeserializer();
        assertTrue(deserializer instanceof JsonTypeDeserializer);

        // Test getValueHandler() and getValueSerializer()
        Object valueHandler = clt.getValueHandler();
        assertNotNull(valueHandler);
        TypeSerializer valueSerializer = clt.getValueSerializer();
        assertTrue(valueSerializer instanceof JsonTypeSerializer);

        // Test getTypeHandler() and getTypeSerializer()
        TypeHandler<?> typeHandler = clt.getTypeHandler();
        assertNotNull(typeHandler);
        TypeSerializer typeSerializer = clt.getTypeSerializer();
        assertTrue(typeSerializer instanceof JsonTypeSerializer);

        // Test withContentType()
        JavaType newContentType = TypeFactory.defaultInstance().constructType(StringBuilder.class);
        CollectionLikeType newClt = clt.withContentType(newContentType);
        assertEquals(newContentType, newClt.getContentType());

        // Test withTypeHandler()
        TypeHandler<?> newTypeHandler = new SimpleTypeHandler<>();
        CollectionLikeType newCltWithHandler = clt.withTypeHandler(newTypeHandler);
        assertEquals(newTypeHandler, newCltWithHandler.getTypeHandler());

        // Test withContentTypeHandler()
        TypeHandler<?> newContentTypeHandler = new SimpleTypeHandler<>();
        CollectionLikeType newCltWithContentHandler = clt.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newContentTypeHandler, newCltWithContentHandler.getContentTypeHandler());

        // Test withValueHandler()
        Object newValueHandler = new SimpleValueHandler<>();
        CollectionLikeType newCltWithValueHandler = clt.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newCltWithValueHandler.getValueHandler());

        // Test withContentValueHandler()
        Object newContentValueHandler = new SimpleContentValueHandler<>();
        CollectionLikeType newCltWithContentValueHandler = clt.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newCltWithContentValueHandler.getContentValueHandler());

        // Test withStaticTyping()
        boolean isStatic = clt.isStaticTyping();
        CollectionLikeType newCltWithStaticTyping = clt.withStaticTyping();
        assertFalse(isStatic);
        assertTrue(newCltWithStaticTyping.isStaticTyping());

        // Test refine()
        JavaType refinedJavaType = TypeFactory.defaultInstance().constructType(LinkedList.class, Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType refinedClt = clt.refine(Collection.class, bindings, refinedJavaType, new TypeBinding[0]);
        assertEquals(LinkedList.class, refinedClt.getErasedType());
        assertEquals(Integer.class, refinedClt.getContentType());
    }

    private static class SimpleTypeHandler<T> implements TypeHandler<T> {
        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }

        @Override
        public void serialize(T value, JsonGenerator g, SerializerProvider serializers) throws IOException {
        }
    }

    private static class SimpleValueHandler<T> implements ValueHandler<T> {
        @Override
        public void handleValue(SerializerProvider provider, JavaType type, T value) throws IOException {
        }
    }

    private static class SimpleContentValueHandler<T> implements ContentValueHandler<T> {
        @Override
        public void handleContentValue(SerializerProvider provider, JavaType type, T value) throws IOException {
        }
    }
}

class TypeFactory {
    public static JavaType constructType(Class<?> rawType, JavaType... typeArguments) throws InstantiationException, IllegalAccessException {
        return ClassUtil.constructType(rawType, TypeFactory.bindings, typeArguments);
    }

    public static JavaType constructType(Class<?> rawType) throws InstantiationException, IllegalAccessException {
        return ClassUtil.constructType(rawType, TypeFactory.bindings, new Class[0]);
    }

    public static JavaType constructType(Class<?> rawType, TypeVariable<?>... typeArguments) throws InstantiationException, IllegalAccessException {
        TypeBindings bindings = TypeBindings.create(rawType, Arrays.stream(typeArguments).map(TypeFactory::createTypeVariable).toArray(TypeVariable[].class));
        return ClassUtil.constructType(rawType, bindings, new Class[0]);
    }

    public static JavaType bindArguments(JavaType rawType, JavaType[] arguments) {
        return ClassUtil.bindArguments(rawType, arguments);
    }

    public static JavaType bindArguments(JavaType rawType, JavaType[] arguments, TypeBindings typeBindings) {
        return ClassUtil.bindArguments(rawType, arguments, typeBindings);
    }

    static JavaType createTypeVariable(TypeVariable<?> typeVariable) {
        AnnotatedClass ac = AnnotatedClass.construct(typeVariable.getRawType(), null, null);
        AnnotatedGenericType agt = AnnotatedGenericType.construct(ac, null, new AnnotatedType[0]);
        return new SimpleType(agt);
    }

    static TypeBindings bindings = TypeBindings.emptyBindings();
}