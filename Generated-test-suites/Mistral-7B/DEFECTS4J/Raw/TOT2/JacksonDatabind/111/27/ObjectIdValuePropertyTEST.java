import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.find("id", null);
            }

            @Override
            public Class<?> getIdType() {
                return String.class;
            }

            @Override
            public ObjectIdReader.IdGenerator idGenerator() {
                return new ObjectIdGenerator() {
                    String id = "testId";

                    @Override
                    public String generateId() {
                        id++;
                        return id;
                    }
                };
            }

            @Override
            public ObjectIdReader.IdResolver idResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId getId(Object idValue, ObjectIdReader reader) {
                        return new ObjectId(idValue.toString());
                    }
                };
            }

            @JsonDeserialize(using = FromStringDeserializer.class)
            public static class IdType {}
        };

        PropertyMetadata metadata = new PropertyMetadata() {
            @Override
            public PropertyName getPropertyName() {
                return PropertyName.find("testProperty", null);
            }

            @Override
            public String getName() {
                return "testProperty";
            }

            @Override
            public Class<?> getType() {
                return String.class;
            }
        };

        JsonDeserializer<ObjectIdValueProperty> valueDeserializer = new None();
        NullValueProvider nullProvider = new NullValueProvider();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(objectIdReader.getClass(), objectIdValueProperty._objectIdReader.getClass());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty.getNullProvider());
    }
}