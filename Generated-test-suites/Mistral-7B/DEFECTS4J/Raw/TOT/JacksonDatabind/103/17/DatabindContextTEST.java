package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabindContextTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetConfig() {
        DatabindContext context = objectMapper.getDeserializationContext();
        MapperConfig<?> config = context.getConfig();
        assertEquals("Expected deserialization config instance", config.getClass(), MapperConfig.class);
    }

    @Test
    public void testIsEnabled() {
        DatabindContext context = objectMapper.getDeserializationContext();
        assertTrue("Expected enablement of bigDecimalFormat", context.isEnabled(MapperFeature.USE_BIG_DECIMAL_FOR_FLOATS));
        assertFalse("Expected disablement of canonicalNaNS", context.isEnabled(MapperFeature.CANONICAL_PROPERTIES));

        context = objectMapper.getSerializationContext();
        assertFalse("Expected disablement of pretty printing", context.isEnabled(SerializationFeature.PRETTY_PRINT_OUTPUT));
        assertTrue("Expected enablement of writeNullEmptyJson", context.isEnabled(SerializationFeature.WRITE_NULL_EMPTY_JSON_ARRAYS));
    }

    @Test
    public void testGetActiveView() {
        DatabindContext context = objectMapper.getSerializationContext();
        assertNull("Expected null for no active view", context.getActiveView());

        JavaType javaType = context.getTypeFactory().constructType(MyClass.class);
        context = objectMapper.getSerializationContext().withActiveView(javaType);
        assertEquals("Expected MyClass.class for active view", javaType.getRawClass(), context.getActiveView());
    }

    @Test
    public void testGetLocale() {
        DatabindContext context = objectMapper.getSerializationContext();
        assertEquals("Expected default locale", Locale.getDefault(), context.getLocale());

        context = objectMapper.getSerializationContext().withLocale(Locale.US);
        assertEquals("Expected US locale", Locale.US, context.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        DatabindContext context = objectMapper.getSerializationContext();
        assertEquals("Expected default timezone", TimeZone.getDefault(), context.getTimeZone());

        context = objectMapper.getSerializationContext().withTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("Expected UTC timezone", TimeZone.getTimeZone("UTC"), context.getTimeZone());
    }

    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = objectMapper.getSerializationContext();
        assertEquals("Expected null for no default property format", null, context.getDefaultPropertyFormat(null));

        context = objectMapper.getSerializationContext().withPropertyValueSerializer(new PropertyFormatTestSerializer());
        JsonFormat.Value format = context.getDefaultPropertyFormat(String.class);
        assertNotNull("Expected non-null format instance", format);
        assertEquals("Expected serializer class for String format", PropertyFormatTestSerializer.class, format.getSerializer().getClass());
    }

    @Test
    public void testAttributeGetSet() {
        DatabindContext context = objectMapper.getDeserializationContext();
        context.setAttribute("testKey", "testValue");
        assertEquals("Expected 'testValue' for attribute value", "testValue", context.getAttribute("testKey"));
    }

    @Test
    public void testConstructType() {
        DatabindContext context = objectMapper.getSerializationContext();
        JavaType javaType = context.getTypeFactory().constructType(String[].class);
        assertNotNull("Expected non-null JavaType instance", javaType);
        assertEquals("Expected java.lang.Class<java.lang.String[]> for rawClass", String[].class, javaType.getRawClass());
    }

    @Test
    public void testResolveSubType() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getSerializationContext();

        JavaType baseType = context.getTypeFactory().constructType(MyBaseClass.class);
        JavaType subType = context.resolveSubType(baseType, "MySubClass");
        assertNotNull("Expected non-null JavaType instance for subtype", subType);
        assertEquals("Expected MySubClass.class for subtype class", MySubClass.class, subType.getRawClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidTypeIdException() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabindContext context = objectMapper.getSerializationContext();

        JavaType baseType = context.getTypeFactory().constructType(MyBaseClass.class);
        context.invalidTypeIdException(baseType, "MyInvalidSubClass");
    }

    @Test
    public void testTypeFactory() {
        DatabindContext context = objectMapper.getSerializationContext();
        TypeFactory typeFactory = context.getTypeFactory();
        assertNotNull("Expected non-null TypeFactory instance", typeFactory);
        JavaType javaType = typeFactory.constructType(MyClass.class);
        assertEquals("Expected MyClass.class for JavaType instance", MyClass.class, javaType.getRawClass());
    }
}

class MyClass {
    @JsonProperty("name")
    private String name;
}

class MyBaseClass {}

class MySubClass extends MyBaseClass {}

class PropertyFormatTestSerializer extends StdSerializer<String> {
    public PropertyFormatTestSerializer() {
        this(null);
    }

    public PropertyFormatTestSerializer(Class<String> t) {
        this(null);
    }

    public PropertyFormatTestSerializer(Class<String> t, Converter<String,String> cvr) {
        super(t);
    }
}