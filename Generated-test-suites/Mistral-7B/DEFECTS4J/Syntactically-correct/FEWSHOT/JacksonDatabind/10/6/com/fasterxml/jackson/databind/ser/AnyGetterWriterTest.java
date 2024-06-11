package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Test class for {@link AnyGetterWriter}
 */
public class AnyGetterWriterTest {

    @SuppressWarnings("unchecked")
    private static MapSerializer createMockMapSerializer() {
        return mock(MapSerializer.class);
    }

    @SuppressWarnings("unchecked")
    private static AnnotatedMember createMockAnnotatedMember() {
        return mock(AnnotatedMember.class);
    }

    @SuppressWarnings("unchecked")
    private static BeanProperty createMockBeanProperty() {
        return mock(BeanProperty.class);
    }

    @SuppressWarnings("unchecked")
    private static ObjectMapper createMockObjectMapper() {
        return mock(ObjectMapper.class);
    }

    @SuppressWarnings("unchecked")
    private static SerializerProvider createMockSerializerProvider() {
        return mock(SerializerProvider.class);
    }

    @SuppressWarnings("unchecked")
    private static JsonGenerator createMockJsonGenerator() {
        return mock(JsonGenerator.class);
    }

    @SuppressWarnings("unchecked")
    private static PropertyFilter createMockPropertyFilter() {
        return mock(PropertyFilter.class);
    }

    @SuppressWarnings("unchecked")
    private static NameTransformer createMockNameTransformer() {
        return mock(NameTransformer.class);
    }

    @Test
    public void testGetAndSerialize() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();

        when(accessor.getValue(bean)).thenReturn(new HashMap<>());
        when(accessor.getName()).thenReturn("anyGetter");
        when(property.getMember()).thenReturn(accessor);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        writer.getAndSerialize(bean, gen, provider);

        writer._mapSerializer.serializeFields((Map<?,?>) anyNonNull(), gen, provider);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("anyGetter");
        verify(gen).writeStartObject();
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testGetAndSerializeNullValue() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();

        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        writer.getAndSerialize(bean, gen, provider);

        verifyNoInteractions(gen);
    }

    @Test
    public void testGetAndSerializeNonMapValue() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();

        when(accessor.getValue(bean)).thenReturn(new Integer(0));

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        assertThrows(JsonMappingException.class, () -> writer.getAndSerialize(bean, gen, provider));
    }

    @Test
    public void testGetAndFilter() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();
        PropertyFilter filter = createMockPropertyFilter();

        Map<String, Object> value = new HashMap<>();
        value.put("key1", "value1");
        value.put("key2", "value2");

        when(accessor.getValue(bean)).thenReturn(value);
        when(accessor.getName()).thenReturn("anyGetter");
        when(property.getMember()).thenReturn(accessor);
        when(filter.includeProperty("key1")).thenReturn(Boolean.TRUE);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        writer.getAndFilter(bean, gen, provider, filter);

        writer._mapSerializer.serializeFilteredFields((Map<?,?>) eq(value), any(JsonGenerator.class), any(SerializerProvider.class), eq(filter), any());
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("anyGetter");
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("key1");
        verify(gen).writeString("value1");
        verifyNoMoreInteractions(gen);
    }

    @Test
    public void testGetAndFilterNullValue() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();
        PropertyFilter filter = createMockPropertyFilter();

        when(accessor.getValue(bean)).thenReturn(null);

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        writer.getAndFilter(bean, gen, provider, filter);

        verifyNoInteractions(gen);
    }

    @Test
    public void testGetAndFilterNonMapValue() throws JsonMappingException, JsonProcessingException, IOException {
        Object bean = new Object();
        MapSerializer mapSerializer = createMockMapSerializer();
        AnnotatedMember accessor = createMockAnnotatedMember();
        BeanProperty property = createMockBeanProperty();
        SerializerProvider provider = createMockSerializerProvider();
        JsonGenerator gen = createMockJsonGenerator();
        PropertyFilter filter = createMockPropertyFilter();

        when(accessor.getValue(bean)).thenReturn(new Integer(0));

        AnyGetterWriter writer = new AnyGetterWriter(property, accessor, mapSerializer);
        assertThrows(JsonMappingException.class, () -> writer.getAndFilter(bean, gen, provider, filter));
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = createMockSerializerProvider();
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);

        writer.resolve(provider);

        verify(provider).handleContextualization(_mapSerializer, any());
    }

    private <T> T anyNonNull() {
        return (T) new Object();
    }

    private <T> T eq(T reference) {
        return (T) eq(reference.getClass(), reference);
    }

    private <T> T eq(Class<T> clazz, T reference) {
        return mock(clazz);
    }

    private static <T> void verifyNoMoreInteractions(T mock) {
        verify(mock, never()).method(any());
    }

    private static <T> void verify(T mock, int times, T method, Object... args) {
        verify(mock, times(times), method, args);
    }

    private static <T> void verify(T mock, int times, Method method, Object... args) {
        verify(mock, times(times), method.getName(), args);
    }

    @SuppressWarnings("unchecked")
    private static <T> void verify(T mock, int times, String methodName, Object... args) {
        verify(mock, times(times), methodName, args);
    }

    private static <T> void verify(T mock, int times, Method method) {
        verify(mock, times(times), method);
    }

    private static <T> void verify(T mock, int times, String methodName) {
        verify(mock, times(times), methodName);
    }

    private static <T> void verify(T mock, int times) {
        verify(mock, times(times));
    }

    private static <T> void verify(T mock) {
        verify(mock);
    }
}