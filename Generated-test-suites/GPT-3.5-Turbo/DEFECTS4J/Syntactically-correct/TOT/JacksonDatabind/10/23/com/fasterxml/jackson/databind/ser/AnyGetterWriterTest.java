
package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty beanProperty;
    private AnnotatedMember annotatedMember;
    private MapSerializer mapSerializer;

    @Before
    public void setup() {
        beanProperty = new BeanProperty();
        annotatedMember = new AnnotatedMember();
        mapSerializer = new MapSerializer();
        anyGetterWriter = new AnyGetterWriter(beanProperty, annotatedMember, mapSerializer);
    }

    @Test
    public void testGetAndSerialize_withNullValue_shouldNotCallMapSerializer() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();

        // Act
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        // Ensure that mapSerializer.serializeFields() was not called
        Assert.assertNull(mapSerializer.getResult());
    }

    @Test
    public void testGetAndSerialize_withNonMapValue_shouldThrowJsonMappingException() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();

        // Act & Assert
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Ensure that the correct exception is thrown
            Assert.assertEquals("Value returned by 'any-getter' (_accessor.getName()()) not java.util.Map but " +
                    "java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_withMapValue_shouldCallMapSerializer() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");

        // Act
        annotatedMember.setValue(bean, map);
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Assert
        // Ensure that mapSerializer.serializeFields() was called
        Assert.assertEquals(map, mapSerializer.getResult());
    }

    @Test
    public void testGetAndFilter_withNullValue_shouldNotCallMapSerializer() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        // Act
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        // Ensure that mapSerializer.serializeFilteredFields() was not called
        Assert.assertNull(mapSerializer.getFilteredResult());
    }

    @Test
    public void testGetAndFilter_withNonMapValue_shouldThrowJsonMappingException() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        // Act & Assert
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Ensure that the correct exception is thrown
            Assert.assertEquals("Value returned by 'any-getter' (_accessor.getName()()) not java.util.Map but " +
                    "java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testGetAndFilter_withMapValue_shouldCallMapSerializer() throws Exception {
        // Arrange
        Object bean = new Object();
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "value");

        // Act
        annotatedMember.setValue(bean, map);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Assert
        // Ensure that mapSerializer.serializeFilteredFields() was called
        Assert.assertEquals(map, mapSerializer.getFilteredResult());
    }

    @Test
    public void testResolve_withNonNullMapSerializer_shouldInvokeProviderHandlePrimaryContextualization() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = new SerializerProvider();
        mapSerializer = new MapSerializer();

        // Act
        anyGetterWriter.resolve(provider);

        // Assert
        // Ensure that provider.handlePrimaryContextualization() was called with the correct serializer and property
        Assert.assertEquals(mapSerializer, provider.getPrimaryContextualValue());
        Assert.assertEquals(beanProperty, provider.getPrimaryContextualProperty());
    }
}
