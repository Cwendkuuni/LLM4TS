import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        
        // Test null value
        anyGetterWriter.getAndSerialize(null, gen, provider);

        // Test non-Map value
        Object bean = new Object();
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        });

        // Test Map value with serializer
        Mockito.when(accessor.getValue(bean)).thenReturn(Mockito.mock(Map.class));
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        Mockito.verify(serializer, Mockito.times(1)).serializeFields(Mockito.any(), Mockito.eq(gen), Mockito.eq(provider));
    }

    @Test
    public void testGetAndFilter() throws Exception {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        PropertyFilter filter = Mockito.mock(PropertyFilter.class);
        
        // Test null value
        anyGetterWriter.getAndFilter(null, gen, provider, filter);

        // Test non-Map value
        Object bean = new Object();
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        });

        // Test Map value with serializer
        Mockito.when(accessor.getValue(bean)).thenReturn(Mockito.mock(Map.class));
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        Mockito.verify(serializer, Mockito.times(1)).serializeFilteredFields(Mockito.any(), Mockito.eq(gen), Mockito.eq(provider), Mockito.eq(filter), Mockito.eq(null));
    }

    @Test
    public void testResolve() throws Exception {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class);
        MapSerializer serializer = Mockito.mock(MapSerializer.class);
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        anyGetterWriter.resolve(provider);

        Mockito.verify(provider, Mockito.times(1)).handlePrimaryContextualization(Mockito.eq(serializer), Mockito.eq(property));
    }
}