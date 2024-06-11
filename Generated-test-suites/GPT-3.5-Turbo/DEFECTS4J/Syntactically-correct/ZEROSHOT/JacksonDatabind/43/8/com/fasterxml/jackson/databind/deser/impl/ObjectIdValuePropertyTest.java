import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private Object instance;

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata propertyMetadata;

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Test
    public void testDeserializeAndSet() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Mockito.when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        Mockito.when(objectIdReader.getIdType()).thenReturn(Object.class);
        Mockito.when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);

        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        // verify deserialization
        Mockito.verify(valueDeserializer).deserialize(jsonParser, deserializationContext);

        // verify binding item
        Mockito.verify(deserializationContext).findObjectId(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(objectIdReader.generator).bindItem(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Mockito.when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        Mockito.when(objectIdReader.getIdType()).thenReturn(Object.class);
        Mockito.when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // verify deserialization
        Mockito.verify(valueDeserializer).deserialize(jsonParser, deserializationContext);

        // verify binding item
        Mockito.verify(deserializationContext).findObjectId(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(objectIdReader.generator).bindItem(instance);

        // verify set and return
        Mockito.verify(objectIdReader.idProperty).setAndReturn(instance, result);

        // verify return result
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Mockito.when(objectIdReader.idProperty).thenReturn(null);

        try {
            property.set(instance, "value");
            Assert.fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetAndReturn() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        Mockito.when(objectIdReader.idProperty).thenReturn(null);

        try {
            property.setAndReturn(instance, "value");
            Assert.fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}