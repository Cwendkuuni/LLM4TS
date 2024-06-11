import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AnyGetterWriterTest {
    
    private AnyGetterWriter anyGetterWriter;
    private AnnotatedMember accessor;
    private MapSerializer mapSerializer;
    
    @Before
    public void setup() {
        BeanProperty property = new BeanProperty();
        accessor = new AnnotatedMember();
        mapSerializer = new MapSerializer();
        
        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();

        // Test when value is null
        Object bean = new Object();
        anyGetterWriter.getAndSerialize(bean, gen, provider);

        // Test when value is not a Map
        bean = new Object();
        accessor.setValue(bean, "test");
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndSerialize(bean, gen, provider);
        });

        // Test when value is a Map
        bean = new Object();
        Map<String, String> value = new HashMap<>();
        value.put("key", "value");
        accessor.setValue(bean, value);
        anyGetterWriter.getAndSerialize(bean, gen, provider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        // Test when value is null
        Object bean = new Object();
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);

        // Test when value is not a Map
        bean = new Object();
        accessor.setValue(bean, "test");
        assertThrows(JsonMappingException.class, () -> {
            anyGetterWriter.getAndFilter(bean, gen, provider, filter);
        });

        // Test when value is a Map
        bean = new Object();
        Map<String, String> value = new HashMap<>();
        value.put("key", "value");
        accessor.setValue(bean, value);
        anyGetterWriter.getAndFilter(bean, gen, provider, filter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();

        // Test when mapSerializer is null
        anyGetterWriter.resolve(provider);
        assertEquals(mapSerializer, anyGetterWriter._mapSerializer);

        // Test when mapSerializer is not null
        MapSerializer newMapSerializer = new MapSerializer();
        anyGetterWriter._mapSerializer = null;
        provider.handlePrimaryContextualization(newMapSerializer, anyGetterWriter._property);
        anyGetterWriter.resolve(provider);
        assertEquals(newMapSerializer, anyGetterWriter._mapSerializer);
    }
}