import com.fasterxml.jackson.annotation.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty mockProperty;
    @Mock
    private AnnotatedMember mockAccessor;
    @Mock
    private MapSerializer mockSerializer;
    @Mock
    private JsonGenerator mockGenerator;
    @Mock
    private SerializerProvider mockProvider;
    @Mock
    private PropertyFilter mockFilter;

    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
    }

    @Test
    public void testGetAndSerialize_withNullValue_shouldDoNothing() throws Exception {
        Object bean = new Object();
        when(mockAccessor.getValue(bean)).thenReturn(null);

        anyGetterWriter.getAndSerialize(bean, mockGenerator, mockProvider);

        // Verify that no serialization happens when the value is null
        verifyZeroInteractions(mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndSerialize_withNonMapValue_shouldThrowException() throws Exception {
        Object bean = new Object();
        when(mockAccessor.getValue(bean)).thenReturn("nonMapValue");

        try {
            anyGetterWriter.getAndSerialize(bean, mockGenerator, mockProvider);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Verify that the expected exception is thrown
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_withMapValue_shouldSerializeFields() throws Exception {
        Object bean = new Object();
        Map<String, Object> mapValue = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(mapValue);

        anyGetterWriter.getAndSerialize(bean, mockGenerator, mockProvider);

        // Verify that the fields are serialized
        verify(mockSerializer).serializeFields(mapValue, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter_withNullValue_shouldDoNothing() throws Exception {
        Object bean = new Object();
        when(mockAccessor.getValue(bean)).thenReturn(null);

        anyGetterWriter.getAndFilter(bean, mockGenerator, mockProvider, mockFilter);

        // Verify that no serialization happens when the value is null
        verifyZeroInteractions(mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter_withNonMapValue_shouldThrowException() throws Exception {
        Object bean = new Object();
        when(mockAccessor.getValue(bean)).thenReturn("nonMapValue");

        try {
            anyGetterWriter.getAndFilter(bean, mockGenerator, mockProvider, mockFilter);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Verify that the expected exception is thrown
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testGetAndFilter_withMapValue_shouldSerializeFilteredFields() throws Exception {
        Object bean = new Object();
        Map<String, Object> mapValue = new HashMap<>();
        when(mockAccessor.getValue(bean)).thenReturn(mapValue);

        anyGetterWriter.getAndFilter(bean, mockGenerator, mockProvider, mockFilter);

        // Verify that the filtered fields are serialized
        verify(mockSerializer).serializeFilteredFields(mapValue, mockGenerator, mockProvider, mockFilter, null);
    }

    @Test
    public void testResolve_shouldHandlePrimaryContextualization() throws JsonMappingException {
        when(mockProvider.handlePrimaryContextualization(mockSerializer, mockProperty)).thenReturn(mockSerializer);

        anyGetterWriter.resolve(mockProvider);

        // Verify that handlePrimaryContextualization is called with the serializer and property
        verify(mockProvider).handlePrimaryContextualization(mockSerializer, mockProperty);
        // Verify that the resolved serializer is set
        assertSame(mockSerializer, anyGetterWriter._mapSerializer);
    }
}