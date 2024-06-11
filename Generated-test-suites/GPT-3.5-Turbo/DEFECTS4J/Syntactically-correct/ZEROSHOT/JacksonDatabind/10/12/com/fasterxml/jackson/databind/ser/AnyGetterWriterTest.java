import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        BeanProperty property = null;
        AnnotatedMember accessor = null;
        MapSerializer serializer = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = null;
        SerializerProvider provider = null;

        // Test when value is null
        anyGetterWriter.getAndSerialize(bean, gen, provider);        
        
        // Test when value is not a Map
        Object value = new Object();
        accessor.setValue(bean, value);
        try {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        // Test when value is a Map and _mapSerializer is null
        value = new Map<Object,Object>();
        accessor.setValue(bean, value);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
        
        // Test when value is a Map and _mapSerializer is not null
        value = new Map<Object,Object>();
        accessor.setValue(bean, value);
        serializer.serializeFields((Map<?,?>) value, gen, provider);
        
        
    }

    @Test
    public void testGetAndFilter() throws Exception {
        BeanProperty property = null;
        AnnotatedMember accessor = null;
        MapSerializer serializer = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        Object bean = new Object();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        PropertyFilter filter = null;

        // Test when value is null
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Test when value is not a Map
        Object value = new Object();
        accessor.setValue(bean, value);
        try {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but " + value.getClass().getName(), e.getMessage());
        }
        
        // Test when value is a Map and _mapSerializer is null
        value = new Map<Object,Object>();
        accessor.setValue(bean, value);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        
        // Test when value is a Map and _mapSerializer is not null
        value = new Map<Object,Object>();
        accessor.setValue(bean, value);
        serializer.serializeFilteredFields((Map<?,?>) value, gen, provider, filter, null);
        
    }

    @Test
    public void testResolve() throws JsonMappingException {
        BeanProperty property = null;
        AnnotatedMember accessor = null;
        MapSerializer serializer = null;
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);

        SerializerProvider provider = null;

        anyGetterWriter.resolve(provider);
        assertEquals(serializer, anyGetterWriter._mapSerializer);
    }
}