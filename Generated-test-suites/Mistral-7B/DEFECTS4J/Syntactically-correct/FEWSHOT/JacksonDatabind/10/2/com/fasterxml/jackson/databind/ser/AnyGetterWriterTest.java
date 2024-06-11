package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Class for testing {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static final AnyGetterWriter ANY_GETTER_WRITER = new AnyGetterWriter(
            new BeanProperty(new AnnotatedClass(Object.class), "anyProperty", "anyProperty"),
            new AnnotatedMethod(new AnnotatedClass(Object.class), "anyMethod", "get"),
            (MapSerializer) mock(MapSerializer.class)) {
    };

    @Test
    public void testGetAndSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = new HashMap<String, Object>() {{
            put("anyProperty", new HashMap<String, String>() {{
                put("value1", "value1");
                put("value2", "value2");
            }});
        }};
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ANY_GETTER_WRITER.getAndSerialize(bean, gen, provider);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("anyProperty");
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("value1");
        verify(gen).writeString("value1");
        verify(gen).writeFieldName("value2");
        verify(gen).writeString("value2");
        verify(gen).writeEndObject();
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetAndFilter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = new HashMap<String, Object>() {{
            put("anyProperty", new HashMap<String, String>() {{
                put("value1", "value1");
                put("value2", "value2");
            }});
        }};
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        PropertyFilter filter = mock(PropertyFilter.class);
        when(filter.include("value1")).thenReturn(true);
        when(filter.include("value2")).thenReturn(false);

        ANY_GETTER_WRITER.getAndFilter(bean, gen, provider, filter);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("anyProperty");
        verify(gen).writeFieldName("value1");
        verify(gen).writeString("value1");
        verify(gen, never()).writeFieldName("value2");
        verify(gen).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = new Object() {{
            // This class should not be a Map
        }};
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ANY_GETTER_WRITER.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testResolve() {
        SerializerProvider provider = mock(SerializerProvider.class);
        MapSerializer mapSerializer = mock(MapSerializer.class);
        when(provider.handlePrimaryContextualization(mapSerializer, new BeanProperty(null, null, null))).thenReturn(mapSerializer);

        ANY_GETTER_WRITER.resolve(provider);
        assertNotNull(ANY_GETTER_WRITER._mapSerializer);
        assertEquals(mapSerializer, ANY_GETTER_WRITER._mapSerializer);
    }
}