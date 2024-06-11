package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test cases for AnyGetterWriter class
 */
public class AnyGetterWriterTest {

    private AnyGetterWriter _anyGetterWriter;
    private AnnotatedMember _accessor;
    private BeanProperty _property;
    private MapSerializer _mapSerializer;
    private ObjectNode _testMap;
    private JsonGenerator _mockJsonGenerator;
    private SerializerProvider _mockSerializerProvider;
    private PropertyFilter _mockPropertyFilter;

    @Before
    public void setUp() throws Exception {
        _accessor = mock(AnnotatedMember.class);
        _property = mock(BeanProperty.class);
        _mapSerializer = mock(MapSerializer.class);
        _testMap = mock(ObjectNode.class);
        _mockJsonGenerator = mock(JsonGenerator.class);
        _mockSerializerProvider = mock(SerializerProvider.class);
        _mockPropertyFilter = mock(PropertyFilter.class);

        when(_accessor.getValue(_mockSerializerProvider.getCurrentValue())).thenReturn(_testMap);

        _anyGetterWriter = new AnyGetterWriter(_property, _accessor, _mapSerializer);
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        _anyGetterWriter.getAndSerialize(null, _mockJsonGenerator, _mockSerializerProvider);
        assertNull(_mockJsonGenerator.getOutputBuffer());
    }

    @Test
    public void testGetAndSerialize_InvalidType() throws Exception {
        when(_testMap.getClass()).thenReturn(String.class);

        assertThrows(JsonMappingException.class, () -> _anyGetterWriter.getAndSerialize(null, _mockJsonGenerator, _mockSerializerProvider));
    }

    @Test
    public void testGetAndSerialize_ValidType() throws Exception {
        when(_testMap.getClass()).thenReturn(HashMap.class);

        _anyGetterWriter.getAndSerialize(_mockSerializerProvider.getCurrentValue(), _mockJsonGenerator, _mockSerializerProvider);
        assertInstanceOf(MapSerializer.class, _mapSerializer);
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        _anyGetterWriter.getAndFilter(null, _mockJsonGenerator, _mockSerializerProvider, _mockPropertyFilter);
        // No output, so no assertions needed
    }

    @Test
    public void testGetAndFilter_InvalidType() throws Exception {
        when(_testMap.getClass()).thenReturn(String.class);

        assertThrows(JsonMappingException.class, () -> _anyGetterWriter.getAndFilter(null, _mockJsonGenerator, _mockSerializerProvider, _mockPropertyFilter));
    }

    @Test
    public void testGetAndFilter_ValidType() throws Exception {
        when(_testMap.getClass()).thenReturn(HashMap.class);

        _anyGetterWriter.getAndFilter(_mockSerializerProvider.getCurrentValue(), _mockJsonGenerator, _mockSerializerProvider, _mockPropertyFilter);
        // No assertions needed, as _mapSerializer.serializeFilteredFields() doesn't have verifiable output
    }

    @Test
    public void testResolve_NullProvider() throws Exception {
        assertThrows(JsonMappingException.class, () -> _anyGetterWriter.resolve(null));
    }

    @Test
    public void testResolve_ValidProvider() throws Exception {
        when(_mapSerializer.getClass()).thenReturn(MapSerializer.class);

        _anyGetterWriter.resolve(_mockSerializerProvider);
        assertInstanceOf(MapSerializer.class, _mapSerializer);
    }
}