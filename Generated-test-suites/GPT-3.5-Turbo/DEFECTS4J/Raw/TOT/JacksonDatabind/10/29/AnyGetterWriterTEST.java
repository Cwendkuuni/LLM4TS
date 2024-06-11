import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

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
    private Object mockBean;

    @Mock
    private JsonGenerator mockGenerator;

    @Mock
    private SerializerProvider mockProvider;

    @Mock
    private PropertyFilter mockFilter;

    @Captor
    private ArgumentCaptor<Map<?, ?>> mapCaptor;

    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
    }

    @Test
    public void testGetAndSerialize_MapValueNull_NoExceptionThrown() {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        try {
            anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
        } catch (Exception e) {
            fail("Exception should not be thrown when value is null");
        }

        verifyZeroInteractions(mockGenerator);
    }

    @Test
    public void testGetAndSerialize_MapValueNotMap_ExceptionThrown() {
        when(mockAccessor.getValue(mockBean)).thenReturn("not a map");

        try {
            anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
            fail("Exception should be thrown when value is not a map");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but java.lang.String", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        verifyZeroInteractions(mockGenerator);
    }

    @Test
    public void testGetAndSerialize_MapSerializerNotNull_CorrectSerialization() {
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("key1", 1);
        testMap.put("key2", 2);
        when(mockAccessor.getValue(mockBean)).thenReturn(testMap);
        when(mockSerializer.serializeFields(any(Map.class), eq(mockGenerator), eq(mockProvider))).thenReturn(true);

        try {
            anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        verify(mockSerializer).serializeFields(mapCaptor.capture(), eq(mockGenerator), eq(mockProvider));
        Map<?, ?> capturedMap = mapCaptor.getValue();
        assertEquals(testMap, capturedMap);
    }

    @Test
    public void testGetAndFilter_MapValueNull_NoExceptionThrown() {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        try {
            anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
        } catch (Exception e) {
            fail("Exception should not be thrown when value is null");
        }

        verifyZeroInteractions(mockGenerator);
    }

    @Test
    public void testGetAndFilter_MapValueNotMap_ExceptionThrown() {
        when(mockAccessor.getValue(mockBean)).thenReturn("not a map");

        try {
            anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
            fail("Exception should be thrown when value is not a map");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but java.lang.String", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        verifyZeroInteractions(mockGenerator);
    }

    @Test
    public void testGetAndFilter_MapSerializerNotNull_CorrectSerialization() {
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("key1", 1);
        testMap.put("key2", 2);
        when(mockAccessor.getValue(mockBean)).thenReturn(testMap);
        when(mockSerializer.serializeFilteredFields(any(Map.class), eq(mockGenerator), eq(mockProvider), eq(mockFilter), eq(null))).thenReturn(true);

        try {
            anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        verify(mockSerializer).serializeFilteredFields(mapCaptor.capture(), eq(mockGenerator), eq(mockProvider), eq(mockFilter), eq(null));
        Map<?, ?> capturedMap = mapCaptor.getValue();
        assertEquals(testMap, capturedMap);
    }

    @Test
    public void testResolve_ProviderReturnsCorrectSerializer_ResolveSuccessfully() throws JsonMappingException {
        when(mockProvider.handlePrimaryContextualization(eq(mockSerializer), eq(mockProperty))).thenReturn(mockSerializer);

        try {
            anyGetterWriter.resolve(mockProvider);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }

        verify(mockProvider).handlePrimaryContextualization(eq(mockSerializer), eq(mockProperty));
    }

    @Test(expected = JsonMappingException.class)
    public void testResolve_ProviderReturnsNull_ThrowException() throws JsonMappingException {
        when(mockProvider.handlePrimaryContextualization(eq(mockSerializer), eq(mockProperty))).thenReturn(null);

        anyGetterWriter.resolve(mockProvider);
    }

}