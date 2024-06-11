import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectIdReader = mock(ObjectIdReader.class);
    }

    @Test
    public void testBasicDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonDeserializer<String> stringDeserializer = new FromStringDeserializer(String.class);
        property = property.withValueDeserializer(stringDeserializer);

        String json = "{\"id\":\"123\"}";
        MyPojo pojo = new MyPojo();

        ObjectIdReader generator = mock(ObjectIdReader.class);
        ObjectIdReader resolver = mock(ObjectIdReader.class);
        when(generator.getIdType()).thenReturn(String.class);
        when(resolver.resolverFor(eq(String.class))).thenReturn(generator);

        property.deserializeAndSet(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext(), pojo);

        assertEquals("123", pojo.getId());
        verify(generator).bindItem(pojo);
    }

    @Test
    public void testNullIdDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.deserializeAndSet(objectMapper.getFactory().createParser("{}"), objectMapper.getDeserializationContext(), new MyPojo());

        assertNull(objectMapper.findValueToDeserialize(objectMapper.getFactory().createParser("{}"), MyPojo.class, "_class"));
        assertNull(objectMapper.getDeserializationContext().findObjectId(null, objectIdReader.generator, objectIdReader.resolver));
    }

    @Test
    public void testEmptyIdDeserialization() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.deserializeAndSet(objectMapper.getFactory().createParser("{}"), objectMapper.getDeserializationContext(), new MyPojo());

        assertNull(objectMapper.findValueToDeserialize(objectMapper.getFactory().createParser("{}"), MyPojo.class, "_class"));
        assertNull(objectMapper.getDeserializationContext().findObjectId(null, objectIdReader.generator, objectIdReader.resolver));
    }

    @Test
    public void testSetterMethod() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        MyPojo pojo = new MyPojo();

        String id = "123";
        property.set(pojo, id);

        assertEquals(id, pojo.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterMethodWithNoIdProperty() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        MyPojo pojo = new MyPojo();

        property.set(pojo, "id");
    }

    @JsonDeserialize(using = ToStringSerializer.class)
    static class MyPojo {
        private String id;

        public String getId() {
            return id;
        }

        @JsonDeserialize(using = FromStringDeserializer.class)
        public void setId(String id) {
            this.id = id;
        }
    }
}