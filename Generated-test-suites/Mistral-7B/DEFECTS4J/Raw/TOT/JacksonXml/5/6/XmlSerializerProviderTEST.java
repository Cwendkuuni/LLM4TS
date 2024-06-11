package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for XmlSerializerProvider.
 */
public class XmlSerializerProviderTest {

    private ObjectMapper mapper = new XmlMapper();

    @Test
    public void testSerializeNullWithEmptyRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();

        // When
        provider.serializeValue(root.getJsonGenerator(), null);

        // Then
        String xml = root.toString();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeNullWithConfiguredRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("element");
        SerializerProvider provider = new XmlSerializerProvider(config);
        ObjectNode root = mapper.createObjectNode();

        // When
        provider.serializeValue(root.getJsonGenerator(), null);

        // Then
        String xml = root.toString();
        assertEquals("<element><null/></element>", xml);
    }

    @Test
    public void testSerializeValue() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();
        String value = "Hello World";

        // When
        provider.serializeValue(root.getJsonGenerator(), value);

        // Then
        String xml = root.toString();
        String expected = "<HelloWorld xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">Hello World</HelloWorld>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSerializeValueWithRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();
        String value = "Hello World";

        // When
        provider.serializeValue(root.getJsonGenerator(), value, null, null);

        // Then
        String xml = root.toString();
        String expected = "<HelloWorld xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">Hello World</HelloWorld>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSerializeValueWithArrayRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();
        int[] values = {1, 2, 3};

        // When
        provider.serializeValue(root.getJsonGenerator(), values);

        // Then
        String xml = root.toString();
        String expected = "<item><item>1</item><item>2</item><item>3</item></item>";
        assertEquals(expected, xml);
    }

    @Test
    public void testSerializeValueWithArrayRootNameAndCustomRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("items");
        SerializerProvider provider = new XmlSerializerProvider(config);
        ObjectNode root = mapper.createObjectNode();
        int[] values = {1, 2, 3};

        // When
        provider.serializeValue(root.getJsonGenerator(), values);

        // Then
        String xml = root.toString();
        String expected = "<items><item><item>1</item></item><item><item>2</item></item><item><item>3</item></item></items>";
        assertEquals(expected, xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNonRootElement() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();
        root.put("name", "John Doe");

        // When & Then
        provider.serializeValue(root.getJsonGenerator(), null);
        assertNull(root.get("name"));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithNonRootElementAndCustomRootName() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator("items");
        SerializerProvider provider = new XmlSerializerProvider(config);
        ObjectNode root = mapper.createObjectNode();
        root.put("name", "John Doe");

        // When & Then
        provider.serializeValue(root.getJsonGenerator(), null);
        assertNull(root.get("name"));
    }

    @Test
    public void testCustomSerializer() throws JsonProcessingException, JsonMappingException, IOException {
        // Given
        SerializerProvider provider = new XmlSerializerProvider(null);
        ObjectNode root = mapper.createObjectNode();
        SimpleValueSerializer serializer = new SimpleValueSerializer();
        SerializationConfig config = new SerializationConfig();
        config.setSerializerFactory(new JsonSerializerFactory() {
            @Override
            public JsonSerializer<?> createValueSerializer(Class<?> serializableType, PropertyName propertyName, JsonSerializer<?> unwrappedSerializer) {
                return serializer;
            }
        });
        provider.setConfig(config);
        Object value = new Object();

        // When
        provider.serializeValue(root.getJsonGenerator(), value, null, null);

        // Then
        String xml = root.toString();
        String expected = "<Object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"/>";
        assertEquals(expected, xml);
    }
}