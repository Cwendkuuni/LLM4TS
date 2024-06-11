
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AnyGetterWriterTest {
    private AnyGetterWriter anyGetterWriter;
    private BeanProperty mockProperty;
    private AnnotatedMember mockAccessor;
    private MapSerializer mockSerializer;
    private Object mockBean;
    private JsonGenerator mockGenerator;
    private SerializerProvider mockProvider;
    private PropertyFilter mockFilter;

    @Before
    public void setup() {
        mockProperty = mock(BeanProperty.class);
        mockAccessor = mock(AnnotatedMember.class);
        mockSerializer = mock(MapSerializer.class);
        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        mockBean = mock(Object.class);
        mockGenerator = mock(JsonGenerator.class);
        mockProvider = mock(SerializerProvider.class);
        mockFilter = mock(PropertyFilter.class);
    }

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndSerialize_NonMapValue() throws Exception {
        Object nonMapValue = "nonMapValue";
        when(mockAccessor.getValue(mockBean)).thenReturn(nonMapValue);

        try {
            anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but " + nonMapValue.getClass().getName();
            assertEquals(expectedMessage, e.getMessage());
        }

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndSerialize_MapValue_NoMapSerializer() throws Exception {
        Map<?, ?> mapValue = mock(Map.class);
        when(mockAccessor.getValue(mockBean)).thenReturn(mapValue);

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndSerialize_MapValue_WithMapSerializer() throws Exception {
        Map<?, ?> mapValue = mock(Map.class);
        when(mockAccessor.getValue(mockBean)).thenReturn(mapValue);
        anyGetterWriter._mapSerializer = mockSerializer;

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        verify(mockSerializer).serializeFields(mapValue, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndFilter_NonMapValue() throws Exception {
        Object nonMapValue = "nonMapValue";
        when(mockAccessor.getValue(mockBean)).thenReturn(nonMapValue);

        try {
            anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Value returned by 'any-getter' (" + mockAccessor.getName() + "()) not java.util.Map but " + nonMapValue.getClass().getName();
            assertEquals(expectedMessage, e.getMessage());
        }

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndFilter_MapValue_NoMapSerializer() throws Exception {
        Map<?, ?> mapValue = mock(Map.class);
        when(mockAccessor.getValue(mockBean)).thenReturn(mapValue);

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndFilter_MapValue_WithMapSerializer() throws Exception {
        Map<?, ?> mapValue = mock(Map.class);
        when(mockAccessor.getValue(mockBean)).thenReturn(mapValue);
        anyGetterWriter._mapSerializer = mockSerializer;

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        verify(mockSerializer).serializeFilteredFields(mapValue, mockGenerator, mockProvider, mockFilter, null);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        anyGetterWriter.resolve(mockProvider);

        verify(mockProvider).handlePrimaryContextualization(mockSerializer, mockProperty);
    }
}
