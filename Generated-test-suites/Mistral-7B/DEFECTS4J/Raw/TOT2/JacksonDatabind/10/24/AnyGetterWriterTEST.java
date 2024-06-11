import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AnyGetterWriterTest {
    private static ObjectMapper mapper;
    private static AnyGetterWriter anyGetterWriter;

    @BeforeAll
    public static void setup() {
        mapper = new JsonMapper();
        anyGetterWriter = new AnyGetterWriter(new BeanProperty("testProperty"), new SimpleBeanProperty(_accessor -> "testMap"), mapper.getFactory().createMapSerializer(Map.class, null));
    }

    @Test
    public void testGetAndSerialize() throws Exception {
        Object bean = new TestBean();
        JsonGenerator gen = mapper.getPolymorphicSerializer(TestBean.class).getSerializerProvider().getGenerator();
        anyGetterWriter.getAndSerialize(bean, gen, mapper.getSerializerProvider());

        String result = gen.getOutputContent();
        // Add assertions for the expected JSON output
        Assertions.assertEquals("{\"testProperty\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", result);
    }
}

class TestBean {
    @JsonAnyGetter
    public Map<String, String> getTestProperty() {
        return new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
    }
}