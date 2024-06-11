package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

    private BeanProperty beanProperty;
    private AnnotatedMember annotatedMember;
    private MapSerializer mapSerializer;
    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setUp() {
        beanProperty = mock(BeanProperty.class);
        annotatedMember = mock(AnnotatedMember.class);
        mapSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        // Mock the necessary objects
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Mock the behavior
        when(annotatedMember.getValue(bean)).thenReturn(mock(Map.class));

        // Call the method under test
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Verify the behavior
        verify(mapSerializer).serializeFields(any(Map.class), eq(gen), eq(provider));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        // Mock the necessary objects
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Mock the behavior
        when(annotatedMember.getValue(bean)).thenReturn(new HashMap());

        // Call the method under test
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        // Mock the necessary objects
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        // Mock the behavior
        when(annotatedMember.getValue(bean)).thenReturn(mock(Map.class));

        // Call the method under test
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Verify the behavior
        verify(mapSerializer).serializeFilteredFields(any(Map.class), eq(gen), eq(provider), eq(filter), eq(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        // Mock the necessary objects
        Object bean = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        PropertyFilter filter = mock(PropertyFilter.class);

        // Mock the behavior
        when(annotatedMember.getValue(bean)).thenReturn(new HashMap());

        // Call the method under test
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        // Mock the necessary objects
        SerializerProvider provider = mock(SerializerProvider.class);

        // Call the method under test
        anyGetterWriter.resolve(provider);

        // Verify the behavior
        verify(provider).handlePrimaryContextualization(eq(mapSerializer), eq(beanProperty));
    }
}