import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;

import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<Object> deserializer;
    private NullValueProvider nullValueProvider;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader();
        metadata = new PropertyMetadata();
        deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonParseException {
                return null;
            }
        };
        nullValueProvider = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullValue() throws IOException {
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("null");

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, null, null);

        Assert.assertNull(result);
    }

    @Test
    public void testWithNonNullValue() throws IOException {
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("123");

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, null, null);

        Assert.assertEquals(123, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.setAndReturn(instance, value);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty idProperty = new SettableBeanProperty() {
            @Override
            public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                // Do nothing
            }

            @Override
            public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
                // Do nothing
                return null;
            }

            @Override
            public void set(Object instance, Object value) throws IOException {
                // Do nothing
            }

            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                return instance;
            }
        };

        objectIdReader.idProperty = idProperty;

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        Assert.assertEquals(instance, result);
    }
}