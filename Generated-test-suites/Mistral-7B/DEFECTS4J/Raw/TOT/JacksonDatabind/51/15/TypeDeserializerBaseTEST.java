package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireTypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private ObjectMapper mapper;
    private SimpleModule module;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        module = new SimpleModule();
        module.setSerializerFactory(SimpleBeanProperty.Serializers.BASE_NAME,
                new SerializableStringSerializer());
        mapper.registerModule(module);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up the deserializer cache
        mapper.clearDeserializers();
    }

    /**
     * Test for the default constructor.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        // Create an instance of TypeDeserializerBase with no arguments
        TypeDeserializerBase deserializer = mapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);

        // Check that the instance has a TypeIdResolver and a base type
        TypeIdResolver idResolver = deserializer.getTypeIdResolver();
        JavaType baseType = deserializer.getType();

        // Check that the TypeIdResolver is initialized with a default implementation
        assertEquals(LaissezFaireTypeIdResolver.instance, idResolver);

        // Check that the base type is Object.class
        assertEquals(Object.class, baseType.getRawClass());

        // Check that the instance has no property or default impl
        assertNull(deserializer._property);
        assertNull(deserializer._defaultImpl);

        // Check that the instance has an empty deserializer map
        assertEquals(0, deserializer._deserializers.size());

        // Check that the instance has a default implementation deserializer
        JsonDeserializer<Object> defaultImplDeserializer = deserializer._defaultImplDeserializer;
        assertNotNull(defaultImplDeserializer);
        assertEquals(NullifyingDeserializer.instance, defaultImplDeserializer);
    }

    /**
     * Test for the constructor with arguments.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testConstructorWithArguments() throws IOException {
        // Create a custom TypeIdResolver
        TypeIdResolver customIdResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                    throws JsonMappingException, IOException {
                if ("CustomType".equals(typeId)) {
                    return mapper.getTypeFactory()
                            .constructType(CustomType.class);
                }
                return null;
            }
        };

        // Create a custom TypeDeserializerBase instance
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, customIdResolver,
                "typeProperty", true, mapper.getTypeFactory().constructType(CustomTypeImpl.class));

        // Check that the instance has the expected values
        assertEquals(baseType, deserializer._baseType);
        assertEquals(customIdResolver, deserializer._idResolver);
        assertEquals("typeProperty", deserializer._typePropertyName);
        assertTrue(deserializer._typeIdVisible);
        assertNotNull(deserializer._property);
        assertEquals(CustomTypeImpl.class, deserializer._defaultImpl.getRawClass());

        // Check that the instance has an empty deserializer map
        assertEquals(0, deserializer._deserializers.size());
    }

    /**
     * Test for the forProperty method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testForProperty() throws IOException {
        // Create a custom TypeDeserializerBase instance
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, false, null);

        // Call forProperty with a null property
        assertNull(deserializer.forProperty(null));

        // Call forProperty with a non-BeanProperty argument
        assertNull(deserializer.forProperty(new Object()));

        // Call forProperty with a valid BeanProperty argument
        BeanProperty property = new BeanProperty("typeProperty", CustomTypeImpl.class);
        TypeDeserializerBase copy = deserializer.forProperty(property);

        // Check that the copy has the same base type, idResolver, and property
        assertEquals(baseType, copy._baseType);
        assertEquals(deserializer._idResolver, copy._idResolver);
        assertEquals(property, copy._property);
    }

    /**
     * Test for the toString method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testToString() throws IOException {
        // Create a custom TypeDeserializerBase instance
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, false, null);

        // Check that the toString method returns a String representation of the instance
        String toString = deserializer.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase"));
        assertTrue(toString.endsWith("]"));
    }

    /**
     * Test for the findDeserializer method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testFindDeserializer() throws IOException {
        // Create a custom TypeDeserializerBase instance
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, false, null);

        // Register a custom deserializer for "CustomType" type id
        JsonDeserializer<CustomType> customDeserializer = new CustomTypeDeserializer();
        mapper.registerDeserializer(CustomType.class, customDeserializer);

        // Test finding the deserializer for a known type id
        String typeId = "CustomType";
        JsonDeserializer<Object> deserializerForKnownTypeId = deserializer._findDeserializer(mapper.getDeserializationContext(), typeId);
        assertEquals(customDeserializer, deserializerForKnownTypeId);

        // Test finding the default implementation deserializer
        JsonDeserializer<Object> defaultImplDeserializer = deserializer._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNotNull(defaultImplDeserializer);
        assertEquals(NullifyingDeserializer.instance, defaultImplDeserializer);

        // Test finding the deserializer for an unknown type id
        String unknownTypeId = "UnknownType";
        JsonDeserializer<Object> deserializerForUnknownTypeId = deserializer._findDeserializer(mapper.getDeserializationContext(), unknownTypeId);
        assertNull(deserializerForUnknownTypeId);
    }

    /**
     * Test for the _deserializeWithNativeTypeId method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Create a custom TypeDeserializerBase instance with a custom TypeIdResolver
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeIdResolver customIdResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                    throws JsonMappingException, IOException {
                if ("CustomType".equals(typeId)) {
                    return mapper.getTypeFactory()
                            .constructType(CustomType.class);
                }
                return null;
            }
        };
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, customIdResolver, "typeProperty", true, null);

        // Create a CustomType instance with a known type id
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("typeId", "CustomType");
        CustomType customType = mapper.readValue(jsonNode, CustomType.class);

        // Test deserializing with a known type id
        assertEquals(customType.getValue(), deserializer._deserializeWithNativeTypeId(mapper.getFactory()
                .createParser(jsonNode.toString()), mapper.getDeserializationContext()));

        // Create a CustomType instance with an unknown type id
        jsonNode.put("typeId", "UnknownType");
        CustomType unknownType = mapper.readValue(jsonNode, CustomType.class);

        // Test deserializing with an unknown type id
        assertNull(deserializer._deserializeWithNativeTypeId(mapper.getFactory()
                .createParser(jsonNode.toString()), mapper.getDeserializationContext()));
    }

    /**
     * Test for the _handleUnknownTypeId method.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Create a custom TypeDeserializerBase instance with a custom TypeIdResolver
        JavaType baseType = mapper.getTypeFactory().constructType(CustomType.class);
        TypeIdResolver customIdResolver = new TypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                    throws JsonMappingException, IOException {
                throw new JsonMappingException("Could not find type id [" + typeId + "]");
            }
        };
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, customIdResolver, "typeProperty", true, null);

        // Test handling an unknown type id
        assertNull(deserializer._handleUnknownTypeId(mapper.getDeserializationContext(), "UnknownTypeId", customIdResolver, baseType));
    }
}