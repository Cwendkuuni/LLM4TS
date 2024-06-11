import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StdSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnyGetterWriterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private AnyGetterWriter writer;
    private BeanProperty property;
    private AnnotatedMember accessor;
    private MapSerializer mapSerializer;
    private SerializerProvider provider;

    @Before
    public void setup() throws Exception {
        property = mock(BeanProperty.class);
        accessor = mock(AnnotatedMember.class);
        when(accessor.getName()).thenReturn("anyGetter");
        when(accessor.getValue(anyObject())).thenReturn(new HashMap<>());

        mapSerializer = new MapSerializer(StdSerializers.emptyMapSerializer(), null, null, null);

        provider = mock(SerializerProvider.class);
        when(provider.handlePrimaryContextualization(any(MapSerializer.class), any()))
                .thenReturn(mapSerializer);

        writer = new AnyGetterWriter(property, accessor, mapSerializer);
    }

    @Test
    public void testGetAndSerialize() throws IOException {
        Object bean = new Object();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        writer.getAndSerialize(bean, gen, provider);
        gen.flush();

        ObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
        Map<String, Object> expected = new HashMap<>();
        writer.writeValueAsString(expected);
        String actual = gen.getValueAsString();
        assertEquals(writer.writeValueAsString(expected), actual);
    }

    @Test
    public void testGetAndFilter() throws IOException {
        Object bean = new Object();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        PropertyFilter filter = mock(PropertyFilter.class);
        writer.getAndFilter(bean, gen, provider, filter);
        gen.flush();

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        ObjectWriter writer = MAPPER.writerWithDefaultPrettyPrinter();
        writer.setSerializationInclusion(SerializationFeature.FILTER_EMPTY_AS_NULL);
        String expected = writer.writeValueAsString(map);

        TypeSerializer typeSerializer = MAPPER.getTypeSerializer();
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setTypeSerializer(Map.class, typeSerializer);
        ObjectWriter filteredWriter = MAPPER.writerWithTypeSerializer(Map.class).with(filter);
        String actual = gen.getValueAsString();
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNullMap() throws Exception {
        Object bean = new Object();
        writer.getAndSerialize(bean, null, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNonMapType() throws Exception {
        Object bean = new Object();
        writer.getAndSerialize(bean, new String[]{"test"}, provider);
    }

    @Test
    public void testResolve() {
        assertNotNull(writer.resolve(provider));
    }
}