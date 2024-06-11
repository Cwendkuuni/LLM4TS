import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
    }

    @Test
    public void testGetAndSerializeWhenValueIsNull() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        verify(mockAccessor, times(1)).getValue(mockBean);
        verifyNoMoreInteractions(mockGenerator, mockProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWhenValueIsNotMap() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn("not a map");

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndSerializeWhenValueIsMapWithMapSerializer() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(mock(Map.class));
        when(mockSerializer.serializeFields(any(Map.class), eq(mockGenerator), eq(mockProvider))).thenReturn(true);

        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        verify(mockAccessor, times(1)).getValue(mockBean);
        verify(mockSerializer, times(1)).serializeFields(any(Map.class), eq(mockGenerator), eq(mockProvider));
        verifyNoMoreInteractions(mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilterWhenValueIsNull() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        verify(mockAccessor, times(1)).getValue(mockBean);
        verifyNoMoreInteractions(mockGenerator, mockProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWhenValueIsNotMap() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn("not a map");

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testGetAndFilterWhenValueIsMapWithMapSerializer() throws Exception {
        when(mockAccessor.getValue(mockBean)).thenReturn(mock(Map.class));
        when(mockSerializer.serializeFilteredFields(any(Map.class), eq(mockGenerator), eq(mockProvider), eq(mockFilter), isNull())).thenReturn(true);

        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        verify(mockAccessor, times(1)).getValue(mockBean);
        verify(mockSerializer, times(1)).serializeFilteredFields(any(Map.class), eq(mockGenerator), eq(mockProvider), eq(mockFilter), isNull());
        verifyNoMoreInteractions(mockGenerator, mockProvider);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        when(mockProvider.handlePrimaryContextualization(mockSerializer, mockProperty)).thenReturn(mockSerializer);

        anyGetterWriter.resolve(mockProvider);

        verify(mockProvider, times(1)).handlePrimaryContextualization(mockSerializer, mockProperty);
        verifyNoMoreInteractions(mockProvider);
    }
}