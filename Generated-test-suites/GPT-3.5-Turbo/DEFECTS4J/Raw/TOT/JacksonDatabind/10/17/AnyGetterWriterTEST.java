import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    private AnyGetterWriter writer;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer serializer;
    
    @Before
    public void setUp() {
        property = new BeanProperty();
        accessor = new AnnotatedMember();
        serializer = new MapSerializer();
        writer = new AnyGetterWriter(property, accessor, serializer);
    }
    
    @Test
    public void testGetAndSerialize_NullValue() {
        Map<String, Integer> bean = new HashMap<>();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        try {
            writer.getAndSerialize(bean, gen, provider);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Check if nothing was serialized
        assertFalse(gen.hasSerialized());
    }
    
    @Test
    public void testGetAndSerialize_NonMapValue() {
        Map<String, Integer> bean = new HashMap<>();
        bean.put("key", 1);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        try {
            writer.getAndSerialize(bean, gen, provider);
            fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (annotatedMember) not java.util.Map but java.util.HashMap", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Check if nothing was serialized
        assertFalse(gen.hasSerialized());
    }
    
    @Test
    public void testGetAndSerialize_MapValue() {
        Map<String, Integer> bean = new HashMap<>();
        bean.put("key", 1);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // Set the map serializer
        writer._mapSerializer = serializer;
        
        try {
            writer.getAndSerialize(bean, gen, provider);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Check if the map was serialized
        assertTrue(gen.hasSerialized());
    }
    
    @Test
    public void testGetAndFilter_NullValue() {
        Map<String, Integer> bean = new HashMap<>();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        try {
            writer.getAndFilter(bean, gen, provider, filter);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Check if nothing was filtered
        assertFalse(filter.isFiltered());
    }
    
    @Test
    public void testGetAndFilter_NonMapValue() {
        Map<String, Integer> bean = new HashMap<>();
        bean.put("key", 1);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        try {
            writer.getAndFilter(bean, gen, provider, filter);
            fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (annotatedMember) not java.util.Map but java.util.HashMap", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Check if nothing was filtered
        assertFalse(filter.isFiltered());
    }
    
    @Test
    public void testGetAndFilter_MapValue() {
        Map<String, Integer> bean = new HashMap<>();
        bean.put("key", 1);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        
        // Set the map serializer
        writer._mapSerializer = serializer;
        
        try {
            writer.getAndFilter(bean, gen, provider, filter);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        
        // Check if the map was filtered
        assertTrue(filter.isFiltered());
    }
    
    @Test
    public void testResolve() {
        SerializerProvider provider = new SerializerProvider();
        
        try {
            writer.resolve(provider);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
        
        // Check if the map serializer was resolved
        assertNotNull(writer._mapSerializer);
    }
}