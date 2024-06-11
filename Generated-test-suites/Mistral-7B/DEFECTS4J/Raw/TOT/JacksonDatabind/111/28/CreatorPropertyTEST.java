package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.StandardSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest extends TestBase {

    private ObjectMapper mapper;
    private CreatorProperty property;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        PropertyName name = PropertyName.construct("testProperty");
        JavaType type = mapper.getTypeFactory().constructType(String.class);

        property = new CreatorProperty(name, type, null, null, null, null, 0, null, null);
    }

    @Test
    public void testDefaultConstruction() {
        // Test default creation with no injection or creator index
        CreatorProperty defaultProperty = new CreatorProperty(name, type, null, null, null, null, 0, null, null);
        assertEquals("Unexpected default value for CreatorProperty", null, defaultProperty._annotated);
        assertEquals("Unexpected default value for CreatorProperty creatorIndex", -1, defaultProperty._creatorIndex);
    }

    @Test
    public void testConstructionWithInjectableValue() throws Exception {
        // Test creation with injectable value
        ObjectNode injectNode = rootNode.objectNode();
        injectNode.put("_injectableValueId", "testInjectValue");
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForParam(String[].class, 0);

        CreatorProperty injectProperty = new CreatorProperty(name, type, null, typeDeser, null, null, 0, null, null, null, injectNode);
        assertEquals("Unexpected injectable value id", "testInjectValue", injectProperty._injectableValueId);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingInjectableValue() throws Exception {
        // Test missing injectable value id
        PropertyName wrapperName = PropertyName.construct("testWrapperProperty");
        TypeDeserializer typeDeser = mapper.getTypeDeserializerForParam(String[].class, 0);

        CreatorProperty missingInjectProperty = new CreatorProperty(name, type, wrapperName, typeDeser, null, null, 0, null, null, null, null);
    }

    @Test
    public void testSettingFallbackSetter() {
        BeanPropertyWriter writer = mapper.writerFor(String.class).getBeanPropertyWriter("testProperty");
        SettableBeanProperty fallbackSetter = writer.getBackingFieldWriter();

        property.setFallbackSetter(fallbackSetter);
        assertNotNull("Unexpected null fallback setter", property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testMissingFallbackSetter() throws Exception {
        property._reportMissingSetter(null, null);
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("creatorProperty", property);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        ObjectNode result = mapper.readTree(json);

        ObjectNode creatorPropertyNode = result.path("creatorProperty");
        CreatorProperty deserializedProperty = creatorPropertyNode.deepGetValue(CreatorProperty.class);
        assertEquals("Unexpected deserialized CreatorProperty name", property.getName(), deserializedProperty.getName());
        assertEquals("Unexpected deserialized CreatorProperty type", property.getType(), deserializedProperty.getType());
        assertEquals("Unexpected deserialized CreatorProperty injectableValueId", property._injectableValueId, deserializedProperty._injectableValueId);
    }
}