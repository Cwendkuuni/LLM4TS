import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize_NullValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();

        writer.getAndSerialize(null, gen, provider);

        //Assert that no exceptions are thrown and the method completes without any output
        //since the value is null
        assertEquals(null, gen.getCurrentValue());
    }

    @Test
    public void testGetAndSerialize_NonMapValue() {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        Object bean = new Object(); // Any non-map object

        try {
            writer.getAndSerialize(bean, gen, provider);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Object", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testGetAndSerialize_MapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer(null, null, null, true, null, null));
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        writer.getAndSerialize(map, gen, provider);

        //Assert that the map is serialized correctly
        assertEquals("{\"key1\":1,\"key2\":2}", gen.toString());
    }

    @Test
    public void testGetAndFilter_NullValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();

        writer.getAndFilter(null, gen, provider, null);

        //Assert that no exceptions are thrown and the method completes without any output
        //since the value is null
        assertEquals(null, gen.getCurrentValue());
    }

    @Test
    public void testGetAndFilter_NonMapValue() {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        Object bean = new Object(); // Any non-map object

        try {
            writer.getAndFilter(bean, gen, provider, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (null()) not java.util.Map but java.lang.Object", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testGetAndFilter_MapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer(null, null, null, true, null, null));
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        PropertyFilter filter = new PropertyFilter() {
            @Override
            public boolean include(BeanProperty property) {
                return true;
            }
        };
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        writer.getAndFilter(map, gen, provider, filter);

        //Assert that the map is serialized correctly
        assertEquals("{\"key1\":1,\"key2\":2}", gen.toString());
    }

    @Test
    public void testResolve() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, null);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();

        writer.resolve(provider);

        //Assert that the mapSerializer is resolved correctly
        assertNotNull(writer._mapSerializer);
        assertEquals(MapSerializer.class, writer._mapSerializer.getClass());
    }
}