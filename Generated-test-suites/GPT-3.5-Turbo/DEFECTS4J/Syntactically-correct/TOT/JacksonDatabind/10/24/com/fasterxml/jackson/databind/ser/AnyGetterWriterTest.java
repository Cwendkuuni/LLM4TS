import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setUp() {
        BeanProperty property = new BeanProperty();
        AnnotatedMember accessor = new AnnotatedMember();
        MapSerializer serializer = new MapSerializer();
        anyGetterWriter = new AnyGetterWriter(property, accessor, serializer);
    }

    @Test
    public void testGetAndSerialize_ValueIsNull_ExpectNoSerialization() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        anyGetterWriter.getAndSerialize(null, gen, provider);

        // Assert that nothing was serialized
        assertEquals(gen.getOutput(), "");
    }

    @Test
    public void testGetAndSerialize_ValueIsNotMap_ExpectJsonMappingException() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();

        try {
            anyGetterWriter.getAndSerialize("not a map", gen, provider);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Assert that the correct exception is thrown
            assertEquals("Value returned by 'any-getter' (_accessor.getName()()) not java.util.Map but " +
                    "java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testGetAndSerialize_MapSerializerIsNull_ExpectNoSerialization() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Object value = new HashMap<>();
        value.put("key", "value");

        anyGetterWriter.getAndSerialize(value, gen, provider);

        // Assert that nothing was serialized
        assertEquals(gen.getOutput(), "");
    }

    @Test
    public void testGetAndFilter_ValueIsNull_ExpectNoSerialization() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        anyGetterWriter.getAndFilter(null, gen, provider, filter);

        // Assert that nothing was serialized
        assertEquals(gen.getOutput(), "");
    }

    @Test
    public void testGetAndFilter_ValueIsNotMap_ExpectJsonMappingException() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();

        try {
            anyGetterWriter.getAndFilter("not a map", gen, provider, filter);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Assert that the correct exception is thrown
            assertEquals("Value returned by 'any-getter' (_accessor.getName()()) not java.util.Map but " +
                    "java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testGetAndFilter_MapSerializerIsNull_ExpectNoSerialization() throws Exception {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        PropertyFilter filter = new PropertyFilter();
        Object value = new HashMap<>();
        value.put("key", "value");

        anyGetterWriter.getAndFilter(value, gen, provider, filter);

        // Assert that nothing was serialized
        assertEquals(gen.getOutput(), "");
    }

    @Test
    public void testResolve_ProviderHandlesPrimaryContextualization_ExpectMapSerializerResolved() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();

        anyGetterWriter.resolve(provider);

        // Assert that MapSerializer is resolved by provider
        assertNotNull(anyGetterWriter._mapSerializer);
    }

}