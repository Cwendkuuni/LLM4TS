import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class AnyGetterWriterTest {
    
    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        
        // Act
        writer.getAndSerialize(bean, dummyGenerator, dummyProvider);
        
        // Assert
        Mockito.verify(dummyAccessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(dummyGenerator, dummyProvider);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerialize_NonMapValue() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        Object nonMapValue = "non-map value";
        Mockito.when(dummyAccessor.getValue(bean)).thenReturn(nonMapValue);
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        
        // Act
        writer.getAndSerialize(bean, dummyGenerator, dummyProvider);
    }
    
    @Test
    public void testGetAndSerialize_MapValue() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        Object mapValue = Mockito.mock(Map.class);
        Mockito.when(dummyAccessor.getValue(bean)).thenReturn(mapValue);
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        
        // Act
        writer.getAndSerialize(bean, dummyGenerator, dummyProvider);
        
        // Assert
        Mockito.verify(dummyAccessor).getValue(bean);
        Mockito.verify(dummySerializer).serializeFields((Map<?,?>) mapValue, dummyGenerator, dummyProvider);
        Mockito.verifyNoMoreInteractions(dummyGenerator, dummyProvider);
    }
    
    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter dummyFilter = Mockito.mock(PropertyFilter.class);
        
        // Act
        writer.getAndFilter(bean, dummyGenerator, dummyProvider, dummyFilter);
        
        // Assert
        Mockito.verify(dummyAccessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(dummyGenerator, dummyProvider, dummyFilter);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndFilter_NonMapValue() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        Object nonMapValue = "non-map value";
        Mockito.when(dummyAccessor.getValue(bean)).thenReturn(nonMapValue);
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter dummyFilter = Mockito.mock(PropertyFilter.class);
        
        // Act
        writer.getAndFilter(bean, dummyGenerator, dummyProvider, dummyFilter);
    }
    
    @Test
    public void testGetAndFilter_MapValue_WithoutSerializer() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = null;
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        Object mapValue = Mockito.mock(Map.class);
        Mockito.when(dummyAccessor.getValue(bean)).thenReturn(mapValue);
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter dummyFilter = Mockito.mock(PropertyFilter.class);
        
        // Act
        writer.getAndFilter(bean, dummyGenerator, dummyProvider, dummyFilter);
        
        // Assert
        Mockito.verify(dummyAccessor).getValue(bean);
        Mockito.verifyNoMoreInteractions(dummyGenerator, dummyProvider, dummyFilter);
    }
    
    @Test
    public void testGetAndFilter_MapValue_WithSerializer() throws Exception {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        Object bean = new Object();
        Object mapValue = Mockito.mock(Map.class);
        Mockito.when(dummyAccessor.getValue(bean)).thenReturn(mapValue);
        JsonGenerator dummyGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter dummyFilter = Mockito.mock(PropertyFilter.class);
        
        // Act
        writer.getAndFilter(bean, dummyGenerator, dummyProvider, dummyFilter);
        
        // Assert
        Mockito.verify(dummyAccessor).getValue(bean);
        Mockito.verify(dummySerializer).serializeFilteredFields((Map<?,?>) mapValue, dummyGenerator, dummyProvider, dummyFilter, null);
        Mockito.verifyNoMoreInteractions(dummyGenerator, dummyProvider, dummyFilter);
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        // Arrange
        BeanProperty dummyProperty = Mockito.mock(BeanProperty.class);
        AnnotatedMember dummyAccessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer dummySerializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter writer = new AnyGetterWriter(dummyProperty, dummyAccessor, dummySerializer);
        SerializerProvider dummyProvider = Mockito.mock(SerializerProvider.class);
        
        // Act
        writer.resolve(dummyProvider);
        
        // Assert
        Mockito.verify(dummyProvider).handlePrimaryContextualization(dummySerializer, dummyProperty);
    }
}