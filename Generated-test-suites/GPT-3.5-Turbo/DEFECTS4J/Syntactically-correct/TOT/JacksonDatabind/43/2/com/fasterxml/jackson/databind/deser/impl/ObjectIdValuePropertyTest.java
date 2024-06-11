import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ReadableObjectId;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, "id", null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeAndSet_WhenIdIsNull() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("null");
        DeserializationContext context = mapper.getDeserializationContext();
        Object instance = new Object();

        assertNull(objectIdValueProperty.deserializeAndSet(parser, context, instance));
    }

    @Test
    public void testDeserializeAndSet_WhenIdNotNull() throws Exception {
        JsonParser parser = mapper.getFactory().createParser("\"123\"");
        DeserializationContext context = mapper.getDeserializationContext();
        Object instance = new Object();

        Object result = objectIdValueProperty.deserializeAndSet(parser, context, instance);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_WhenIdPropertyIsNull() throws Exception {
        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturn_WhenIdPropertyNotNull() throws Exception {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdValueProperty, "newId");
        Object instance = new Object();
        Object value = new Object();

        Object result = src.setAndReturn(instance, value);

        assertNotNull(result);
        assertEquals(instance, result);
    }

    @Test
    public void testWithName() throws Exception {
        ObjectIdValueProperty newName = objectIdValueProperty.withName(new PropertyName("newName"));

        assertEquals("newName", newName.getName().getSimpleName());
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = mapper.getDeserializerProvider().findDeserializer(mapper.getDeserializationContext().getConfig(), mapper.getDeserializationContext().getConfig().constructType(Object.class), null);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);

        assertNotNull(newProperty.getValueDeserializer());
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }
}