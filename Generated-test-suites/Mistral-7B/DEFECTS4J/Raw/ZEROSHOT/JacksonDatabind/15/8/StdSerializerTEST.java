package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StdSerializerTest {

    private StdSerializer<String> stringSerializer;
    private ObjectNode node;
    private JsonSerializerProvider provider;

    @Before
    public void setUp() throws Exception {
        stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(value);
            }
        };
        node = JsonNodeFactory.instance.objectNode();
        provider = Mockito.mock(SerializerProvider.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(String.class, stringSerializer.handledType());
    }

    @Test
    public void testSerialize() throws Exception {
        String value = "test";
        ObjectWriter writer = Json.getInstance(provider).writerWithRootValue(node);
        stringSerializer.serialize(value, writer.getRootValue().getJsonGenerator(), provider);
        assertEquals("test", writer.ValueAsString());
    }

    @Test
    public void testGetSchema() throws Exception {
        assertEquals(node, stringSerializer.getSchema(provider, String.class));
    }

    @Test
    public void testGetSchemaWithOptional() throws Exception {
        ObjectNode schema = stringSerializer.getSchema(provider, String.class, false);
        assertTrue(schema.hasNonNull("required"));
        assertFalse(schema.getBoolean("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        stringSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).expectAnyFormat(typeHint);
    }

    @Test
    public void testWrapAndThrow() throws Exception {
        Throwable t = new RuntimeException("Test Exception");
        stringSerializer.wrapAndThrow(provider, t, null, "");
        Mockito.verify(provider).isEnabled(SerializationFeature.WRAP_EXCEPTIONS);
        JsonMappingException e = Mockito.capture(JsonMappingException.class).getValue();
        assertEquals("Test Exception", e.getCause().getMessage());
    }

    @Test
    public void testIsDefaultSerializer() throws Exception {
        assertFalse(stringSerializer.isDefaultSerializer(new StdSerializer<String>(String[].class)));
        assertTrue(stringSerializer.isDefaultSerializer(stringSerializer));
    }

    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        Converter<Object, Object> converter = Mockito.mock(Converter.class);
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        String propertyName = "testProperty";
        AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);

        when(provider.getAnnotationIntrospector().findSerializationContentConverter(annotatedMember)).thenReturn(JsonSerialize.class);
        when(provider.converterInstance(annotatedMember, JsonSerialize.class)).thenReturn(converter);

        assertEquals(delegateSerializer, stringSerializer.findConvertingContentSerializer(provider, annotatedClass, propertyName, delegateSerializer));

        verify(converter).getOutputType(provider.getTypeFactory());
        verify(provider).getSerializerProvider();
        verify(provider).findValueSerializer(Mockito.any());
    }

    @Test
    public void testFindPropertyFilter() throws Exception {
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        String filterId = "testFilterId";

        when(provider.getFilterProvider()).thenReturn(new SimpleFilterProvider());
        when(provider.getFilterProvider().findPropertyFilter(filterId, null)).thenReturn(filter);

        assertEquals(filter, stringSerializer.findPropertyFilter(provider, filterId, null));
    }
}