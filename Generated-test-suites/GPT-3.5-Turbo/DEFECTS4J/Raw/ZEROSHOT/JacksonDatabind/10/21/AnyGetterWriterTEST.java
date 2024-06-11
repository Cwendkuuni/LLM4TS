import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testGetAndSerialize() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        // Run the test
        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        // Verify the results
        verify(mockAccessor).getValue(mockBean);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_ValueReturnedByAnyGetter_NotJavaUtilMap_ButDifferentClass() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        when(mockAccessor.getValue(mockBean)).thenReturn(new Object());

        // Run the test
        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndSerialize_MapSerializerIsNull() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, null);
        when(mockAccessor.getValue(mockBean)).thenReturn(mock(Object.class));

        // Run the test
        anyGetterWriter.getAndSerialize(mockBean, mockGenerator, mockProvider);

        // Verify the results
        verify(mockAccessor).getValue(mockBean);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        when(mockAccessor.getValue(mockBean)).thenReturn(null);

        // Run the test
        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        // Verify the results
        verify(mockAccessor).getValue(mockBean);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
        verifyZeroInteractions(mockFilter);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_ValueReturnedByAnyGetter_NotJavaUtilMap_ButDifferentClass() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
        when(mockAccessor.getValue(mockBean)).thenReturn(new Object());

        // Run the test
        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testGetAndFilter_MapSerializerIsNull() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, null);
        when(mockAccessor.getValue(mockBean)).thenReturn(mock(Object.class));

        // Run the test
        anyGetterWriter.getAndFilter(mockBean, mockGenerator, mockProvider, mockFilter);

        // Verify the results
        verify(mockAccessor).getValue(mockBean);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
        verifyZeroInteractions(mockFilter);
    }

    @Test
    public void testResolve() throws Exception {
        // Setup
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);

        // Run the test
        anyGetterWriter.resolve(mockProvider);

        // Verify the results
        verify(mockProvider).handlePrimaryContextualization(mockSerializer, mockProperty);
    }
}