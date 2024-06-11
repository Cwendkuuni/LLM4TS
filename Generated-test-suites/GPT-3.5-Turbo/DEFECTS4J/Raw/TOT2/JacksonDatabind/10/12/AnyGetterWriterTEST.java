import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Test
    public void testGetAndSerialize() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer());
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        writer.getAndSerialize(map, gen, provider);
        Mockito.verify(gen).writeObjectField("key", 1);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer());
        writer.getAndSerialize("not_map", null, null);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer());
        Map<String, String> map = new HashMap<>();
        map.put("filter_key", "filter_value");
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        writer.getAndFilter(map, gen, provider, null);
        Mockito.verify(gen).writeObjectField("filter_key", "filter_value");
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer());
        writer.getAndFilter("not_map", null, null, null);
    }

    @Test
    public void testResolve() throws Exception {
        AnyGetterWriter writer = new AnyGetterWriter(null, null, new MapSerializer());
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        writer.resolve(provider);
        assertNotNull(writer._mapSerializer);
    }
}