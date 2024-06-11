import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonParser;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public IdType getIdType() {
                return IdType.INT;
            }

            @Override
            public ObjectReader idReader(DeserializationContext ctxt) {
                return new SimpleObjectReader();
            }

            @Override
            public ObjectIdResolver idResolver(DeserializationContext ctxt) {
                return new SimpleObjectIdResolver();
            }

            @Override
            public ObjectIdGenerator idGenerator(DeserializationContext ctxt) {
                return new SimpleObjectIdGenerator();
            }

            @Override
            public PropertyMetadata metadata() {
                return new PropertyMetadata();
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        String json = "{\"id\":1}";
        JsonParser parser = mapper.getFactory().createParser(json);
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);

        int id = (int) instance.getClass().getField("id").get(instance);
        assertEquals(1, id);
    }
}