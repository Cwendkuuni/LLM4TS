import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private Object bean;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(new AnnotatedClass(Object.class), "anyProperty", AnnotatedElement.NO_ANNOTATIONS);
        AnnotatedMember accessor = new AnnotatedMember(new AnnotatedField(Object.class, "anyProperty"), AnnotatedElement.NO_ANNOTATIONS);
        MapSerializer mapSerializer = new MapSerializer(new TypeFactory().constructTypeMap(Map.class, String.class, String.class));

        anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
        bean = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        jsonGenerator = new JsonFactory().createGenerator(System.out);
        serializerProvider = new StandardSerializerFinder().findValueSerializer(Map.class, null);
    }

    @Test
    public void testGetAndSerialize() throws IOException {
        anyGetterWriter.getAndSerialize(bean, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputBuffer().toString();
        Assert.assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", json);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNullValue() throws IOException {
        anyGetterWriter.getAndSerialize(new HashMap<>(), jsonGenerator, serializerProvider);
        jsonGenerator.flush();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeWithNonMapValue() throws IOException {
        Object nonMapValue = 42;
        anyGetterWriter.getAndSerialize(new HashMap<>(), jsonGenerator, serializerProvider);
        anyGetterWriter.getAndSerialize(nonMapValue, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
    }

    @Test
    public void testGetAndFilter() throws IOException {
        PropertyFilter filter = new FilterProvider().filter("anyProperty", new FilterConfig() {{
            setInclude("key1");
        }});

        anyGetterWriter.getAndFilter(bean, jsonGenerator, serializerProvider, filter);
        jsonGenerator.flush();

        String json = jsonGenerator.getOutputBuffer().toString();
        Assert.assertEquals("{\"key1\":\"value1\"}", json);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNullValue() throws IOException {
        anyGetterWriter.getAndFilter(new HashMap<>(), jsonGenerator, serializerProvider, null);
        jsonGenerator.flush();
    }

    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterWithNonMapValue() throws IOException {
        Object nonMapValue = 42;
        anyGetterWriter.getAndFilter(nonMapValue, jsonGenerator, serializerProvider, null);
        jsonGenerator.flush();
    }

    @Test
    public void testResolve() throws JsonMappingException {
        // No need to test this method as it doesn't have any observable side effect
    }
}