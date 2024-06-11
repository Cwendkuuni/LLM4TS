package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = mapper.getTypeFactory();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapper.getTypeFactory().constructType(String.class);
        testee = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty(TypeDeserializerBase.class, "testProperty");
        TypeDeserializer deserializer = testee.forProperty(property);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As typeInclusion = testee.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES, typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("TypeDeserializerBase", testee.baseTypeName());
    }

    @Test
    public void testPropertyName() throws Exception {
        assertEquals("typeProperty", testee.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() throws Exception {
        assertEquals(testee.getTypeIdResolver(), testee._idResolver);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertEquals(String.class, testee.getDefaultImpl().getRawClass());
    }

    @Test
    public void testFindDeserializer_NullTypeId() throws Exception {
        assertNull(testee._findDeserializer(null, null));
    }

    @Test
    public void testFindDeserializer_ValidTypeId() throws Exception {
        String typeId = "com.example.ExampleClass";
        JsonDeserializer<Object> deserializer = mapper.getFactory()
                .createValueDeserializer(mapper.getTypeFactory().constructType(ExampleClass.class), null, null);
        testee._deserializers.put(typeId, deserializer);
        assertEquals(deserializer, testee._findDeserializer(mockContext(), typeId));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        assertNull(testee._findDefaultImplDeserializer(null));
    }

    @Test
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        assertNull(testee._deserializeWithNativeTypeId(mockParser(), mockContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeId_ValidTypeId() throws Exception {
        String typeId = "com.example.ExampleClass";
        JsonDeserializer<Object> deserializer = mapper.getFactory()
                .createValueDeserializer(mapper.getTypeFactory().constructType(ExampleClass.class), null, null);
        testee._deserializers.put(typeId, deserializer);
        ExampleClass result = (ExampleClass) testee._deserializeWithNativeTypeId(mockParser(), mockContext(), typeId);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        testee._deserializeWithNativeTypeId(mockParser(), mockContext(), "com.unknown.UnknownClass");
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        String extraDesc = "type ids are not statically known (for POJO property 'testProperty')";
        JavaType type = mapper.getTypeFactory().constructType(Object.class);
        assertNull(testee._handleUnknownTypeId(mockContext(), "unknownTypeId"));
        assertEquals(type, testee._handleUnknownTypeId(mockContext(), "unknownTypeId").getValueType());
        assertEquals("Unknown type id 'unknownTypeId' (for POJO property 'testProperty')",
                testee._handleUnknownTypeId(mockContext(), "unknownTypeId").getParser().getCurrentName());
        assertEquals(extraDesc, testee._handleUnknownTypeId(mockContext(), "unknownTypeId").getDescription());
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        String extraDesc = "Missing type id (for POJO property 'testProperty')";
        JavaType type = mapper.getTypeFactory().constructType(Object.class);
        assertNull(testee._handleMissingTypeId(mockContext(), extraDesc));
        assertEquals(type, testee._handleMissingTypeId(mockContext(), extraDesc).getValueType());
    }

    private DeserializationContext mockContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    private JsonParser mockParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(new ByteArrayInputStream("{}".getBytes()));
        return jp;
    }
}