import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerializeNull() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndSerialize(null, mockGenerator, mockProvider);
        
        verifyZeroInteractions(mockAccessor);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndSerializeNonMapObject() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndSerialize(new Object(), mockGenerator, mockProvider);
        
        verify(mockAccessor).getValue(anyObject());
        verify(mockGenerator, never()).writeObjectField(anyString(), anyObject());
        verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testGetAndSerializeMapObject() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Map<Object,Object> mockMap = mock(Map.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndSerialize(mockMap, mockGenerator, mockProvider);
        
        verify(mockAccessor).getValue(anyObject());
        verify(mockGenerator).writeObjectField(anyString(), anyObject());
        verifyZeroInteractions(mockProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeMapObjectWithWrongType() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndSerialize("string", mockGenerator, mockProvider);
    }

    @Test
    public void testGetAndFilterNull() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndFilter(null, mockGenerator, mockProvider, mockFilter);
        
        verifyZeroInteractions(mockAccessor);
        verifyZeroInteractions(mockGenerator);
        verifyZeroInteractions(mockProvider);
        verifyZeroInteractions(mockFilter);
    }

    @Test
    public void testGetAndFilterNonMapObject() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndFilter(new Object(), mockGenerator, mockProvider, mockFilter);
        
        verify(mockAccessor).getValue(anyObject());
        verify(mockGenerator, never()).writeObjectField(anyString(), anyObject());
        verifyZeroInteractions(mockProvider);
        verifyZeroInteractions(mockFilter);
    }

    @Test
    public void testGetAndFilterMapObject() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);
        Map<Object,Object> mockMap = mock(Map.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndFilter(mockMap, mockGenerator, mockProvider, mockFilter);
        
        verify(mockAccessor).getValue(anyObject());
        verify(mockGenerator).writeObjectField(anyString(), anyObject());
        verifyZeroInteractions(mockProvider);
        verifyZeroInteractions(mockFilter);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterMapObjectWithWrongType() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        PropertyFilter mockFilter = mock(PropertyFilter.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.getAndFilter("string", mockGenerator, mockProvider, mockFilter);
    }

    @Test
    public void testResolve() throws Exception {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        AnnotatedMember mockAccessor = mock(AnnotatedMember.class);
        MapSerializer mockSerializer = mock(MapSerializer.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAccessor, mockSerializer);
        anyGetterWriter.resolve(mockProvider);
        
        verify(mockProvider).handlePrimaryContextualization(any(), any());
    }
}