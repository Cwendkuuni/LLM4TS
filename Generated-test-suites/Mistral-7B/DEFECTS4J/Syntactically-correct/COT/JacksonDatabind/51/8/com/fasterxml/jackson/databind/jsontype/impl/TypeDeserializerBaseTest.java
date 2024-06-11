package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.getClass().getName());
        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idResolver, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
        assertNull(typeDeserializerBase._property);

        TypeDeserializerBase copiedTypeDeserializerBase = new TypeDeserializerBase(typeDeserializerBase, new BeanProperty("property"));

        assertEquals(typeDeserializerBase._baseType, copiedTypeDeserializerBase._baseType);
        assertEquals(typeDeserializerBase._idResolver, copiedTypeDeserializerBase._idResolver);
        assertEquals(typeDeserializerBase._typePropertyName, copiedTypeDeserializerBase._typePropertyName);
        assertEquals(typeDeserializerBase._typeIdVisible, copiedTypeDeserializerBase._typeIdVisible);
        assertEquals(typeDeserializerBase._property, copiedTypeDeserializerBase._property);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseType() {
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        new TypeDeserializerBase(null, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIdResolver() {
        JavaType baseType = Object.class.getType();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        new TypeDeserializerBase(baseType, null, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTypePropertyName() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        new TypeDeserializerBase(baseType, idResolver, null, typeIdVisible, defaultImpl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDefaultImpl() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;

        new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
    }

    @Test
    public void testForProperty() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        BeanProperty property = new BeanProperty("property", String.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);

        assertNotNull(typeDeserializer);
        assertSame(String.class, typeDeserializer.getType(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyWithNullProperty() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();

        assertNotNull(typeInclusion);
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = Object.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String toStringResult = typeDeserializerBase.toString();

        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver: com.fasterxml.jackson.databind.DefaultTypeIdResolver; ]";

        assertEquals(expected, toStringResult);
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = new StubJsonDeserializer();
        idResolver.putIdMapper(typeId, deserializer);

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(new TestDeserializationContext(), typeId);

        assertNotNull(deser);
        assertEquals(deserializer, deser);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = String.class.getType();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String typeId = "nonExistentTypeId";

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(new TestDeserializationContext(), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Double.TYPE;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        TestDeserializationContext context = new TestDeserializationContext();

        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNull(deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = Object.class.getType();
        TypeIdResolver idResolver = new DefaultTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Double.TYPE;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        TestDeserializationContext context = new TestDeserializationContext();
        context.putContextualValueDeserializer(Double.TYPE, new StubJsonDeserializer());

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId", idResolver, baseType);

        assertNull(javaType);
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "Stub deserialized";
        }
    }

    static class TestDeserializationContext extends DeserializationContext {
        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property)
                throws IOException {
            return null;
        }

        @Override
        public void putContextualValueDeserializer(JavaType type, JsonDeserializer<?> deserializer) {
            // empty
        }
    }
}