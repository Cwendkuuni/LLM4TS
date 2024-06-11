package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstructor() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertNotNull(typeDeserializerBase);
        assertEquals(String.class, typeDeserializerBase.baseType());
        assertEquals(idRes, typeDeserializerBase.getTypeIdResolver());
        assertNull(typeDeserializerBase.getPropertyName());
        assertEquals(null, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testTypeDeserializerBaseCopyConstructor() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        BeanProperty property = new BeanProperty("testProperty", String.class, null);
        TypeDeserializerBase copy = new TypeDeserializerBase(typeDeserializerBase, property);
        assertEquals(typeDeserializerBase.baseType(), copy.baseType());
        assertEquals(typeDeserializerBase.getTypeIdResolver(), copy.getTypeIdResolver());
        assertEquals(property, copy.getProperty());
        assertNull(copy.getDefaultImpl());
    }

    @Test
    public void testForProperty() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("testProperty", String.class, null);
        TypeDeserializer<Object> forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(String.class, forProperty.baseType());
        assertEquals(idRes, forProperty.getTypeIdResolver());
        assertEquals(typePropertyName, forProperty.getPropertyName());
        assertEquals(defaultImpl, forProperty.getDefaultImpl());
        assertEquals(TypeDeserializer.None.class, forProperty.getTypeInclusion());
    }

    @Test
    public void testGetTypeInclusion() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(TypeDeserializer.None.class, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals("class java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testPropertyName() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        assertEquals(idRes, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testDefaultImpl() {
        JavaType baseType = JSONFactory.instance.constructType(String.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(Object.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = new StdDeserializer();
        mapper.registerDeserializer(Integer.class, deserializer);
        String typeId = "intType";
        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
        assertEquals(deserializer, deser);

        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        deserializer = new StdDeserializerWithTypeId();
        mapper.registerDeserializer(Map.class, deserializer);
        String typeIdWithClass = "class java.util.Map";
        deser = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeIdWithClass);
        assertTrue(deserializer instanceof MapDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(Object.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = new StdDeserializer();
        mapper.registerDeserializer(Integer.class, deserializer);

        Object nullValue = null;
        JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
        if (defaultImplDeserializer == null) {
            assertNull(defaultImplDeserializer);
            assertEquals(nullValue, mapper.readValue(new JsonFactory(), nullValue.getClass()));
        } else {
            assertNotNull(defaultImplDeserializer);
            JsonNode node = mapper.readTree("{}");
            Object result = defaultImplDeserializer.deserialize(mapper.getDeserializationContext().findValueDeserializer(node), mapper.getDeserializationContext());
            assertEquals(nullValue, result);
        }

        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, null);
        JsonDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
        mapper.registerDeserializer(JsonTypeInfo.Id.CLASS, nullifyingDeserializer);
        Object nullableValue = null;
        deserializer = typeDeserializerBase2._findDefaultImplDeserializer(mapper.getDeserializationContext());
        if (deserializer == null) {
            assertNull(deserializer);
            assertEquals(nullValue, mapper.readValue(new JsonFactory(), nullValue.getClass()));
        } else {
            assertNotNull(deserializer);
            JsonNode node = mapper.readTree("{}");
            Object result = deserializer.deserialize(mapper.getDeserializationContext().findValueDeserializer(node), mapper.getDeserializationContext());
            assertEquals(nullValue, result);
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(Object.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> deserializer = new StdDeserializer();
        mapper.registerDeserializer(Integer.class, deserializer);
        String typeId = "intType";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mapper.getFactory().createParser("{\"typeId\":\"" + typeId + "\"}"), mapper.getDeserializationContext());
        assertEquals(Integer.class, result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(Object.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper mapper = new ObjectMapper();
        String typeId = "unknownTypeId";
        typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeId() throws IOException {
        JavaType baseType = JSONFactory.instance.constructType(Object.class);
        TypeIdResolver idRes = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        JavaType defaultImpl = JSONFactory.instance.constructType(Integer.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        ObjectMapper mapper = new ObjectMapper();
        String extraDesc = "type ids are not statically known (for POJO property 'typeProperty')";
        typeDeserializerBase._handleMissingTypeId(mapper.getDeserializationContext(), extraDesc);
    }
}