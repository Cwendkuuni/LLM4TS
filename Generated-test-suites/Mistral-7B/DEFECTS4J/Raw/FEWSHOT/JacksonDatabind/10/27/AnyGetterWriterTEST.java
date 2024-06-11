package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.TestBase;

public class AnyGetterWriterTest extends TestBase {

    @Test
    public void testAnyGetterWriter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(new PropertyName("anyMapProperty"));

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        MyClass obj = new MyClass();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        obj.anyMapProperty = map;

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();

        writer.getAndSerialize(obj, gen, provider);

        assertMapEquals(map, mapper.readValue(gen.getOutputStream(), Map.class));
    }

    @Test
    public void testNullAnyGetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = null;

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        MyClass obj = new MyClass();

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();

        writer.getAndSerialize(obj, gen, provider);

        assertNull(gen.getOutputStream());
    }

    @Test
    public void testInvalidAnyGetterType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(new PropertyName("anyMapProperty"));
        when(accessor.getValue(anyObject())).thenReturn("not a map");

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        MyClass obj = new MyClass();

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();

        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(obj, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(new PropertyName("anyMapProperty"));
        when(accessor.getValue(anyObject())).thenReturn(new HashMap<>());

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        MyClass obj = new MyClass();
        PropertyFilter filter = mock(PropertyFilter.class);

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();

        writer.getAndFilter(obj, gen, provider, filter);

        assertEmptyMap(mapper.readValue(gen.getOutputStream(), Map.class));
    }

    @Test
    public void testNullFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(new PropertyName("anyMapProperty"));
        when(accessor.getValue(anyObject())).thenReturn(new HashMap<>());

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        MyClass obj = new MyClass();

        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();

        writer.getAndFilter(obj, gen, provider, null);

        assertMapEquals(new HashMap<>(), mapper.readValue(gen.getOutputStream(), Map.class));
    }

    @Test
    public void testResolve() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mapper.findProperty("anyMapProperty", Map.class, MyClass.class);
        AnnotatedMember accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn(new PropertyName("anyMapProperty"));

        AnnotatedClass annotatedClass = mapper.findAnnotationInfo(MyClass.class);
        AnnotatedMember anyGetter = annotatedClass.findAnnotatedMember("anyMapProperty", JsonAnyGetter.class);
        if (anyGetter != null) {
            accessor = anyGetter;
        }

        MapSerializer mapSerializer = mapper.getDefaultSerializerProvider().findValueSerializer(Map.class, null);
        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);

        SerializerProvider provider = createSerializerProvider();
        provider.setActiveContext(new SerializerProvider.BaseContext());

        writer.resolve(provider);

        MapSerializer resolvedSerializer = writer._mapSerializer;
        assertNotNull(resolvedSerializer);
    }

    private void assertMapEquals(Map<?, ?> expected, Map<?, ?> actual) {
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<?, ?> entry : expected.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            Object actualValue = actual.get(key);
            assertEquals(value, actualValue);
        }
    }

    private void assertEmptyMap(Map<?, ?> map) {
        assertTrue(map.isEmpty());
    }
}

class MyClass {
    @JsonAnyGetter
    public Map<String, Object> anyMapProperty() {
        return new HashMap<>();
    }
}