import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() throws Exception {
        objectIdReader = mock(ObjectIdReader.class);
        JsonDeserializer<ObjectId> objectIdDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        when(objectIdReader.propertyName).thenReturn(PropertyName.construct("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectId.class);
        when(objectIdReader.getDeserializer()).thenReturn(objectIdDeserializer);
        when(objectIdReader. generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        when(objectIdDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new ObjectId(1L));
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenAnswer(i -> i.getMockControl().callRealMethod());
        when(propertyDefinition.getName()).thenReturn("idProperty");
        ObjectNode testInstance = new ObjectMapper().createObjectNode();
        ObjectNode testValue = new ObjectMapper().createObjectNode();
        jsonParser = new JsonFactory().createParser(testValue.toString());
        deserializationContext = new TestDeserializationContext();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyDefinition, null, valueDeserializer);
    }

    @Test
    public void testDeserializeAndSetNullInstance() throws Exception {
        try {
            objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot call deserializeAndSet() with null: 'instance'", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSetEmptyStringInstance() throws Exception {
        ObjectNode emptyStringInstance = new ObjectMapper().createObjectNode();
        emptyStringInstance.put("", "");
        try {
            objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, emptyStringInstance);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializeSetAndReturnInvalidObjectId() throws Exception {
        when(objectIdDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new JsonProcessingException("Invalid ObjectId"));
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, testInstance);
        assertEquals(null, result);
        verify(testInstance, never()).set("id", any(ObjectId.class));
    }

    @Test
    public void testSetMissingObjectIdProperty() throws Exception {
        ObjectNode testInstance = new ObjectMapper().createObjectNode();
        try {
            objectIdValueProperty.setAndReturn(testInstance, new ObjectId(1L));
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, testInstance);
        verify(objectIdReader).getIdType();
        verify(objectIdDeserializer).deserialize(jsonParser, deserializationContext);
        verify(objectIdReader.generator).bindId(anyLong());
        verify(testInstance).set("id", any(ObjectId.class));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectId id = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, testInstance);
        assertEquals(id, testInstance.get("id"));
        verify(objectIdReader).getIdType();
        verify(objectIdDeserializer).deserialize(jsonParser, deserializationContext);
        verify(objectIdReader.generator).bindId(anyLong());
        verify(testInstance).set("id", any(ObjectId.class));
    }

    @Test
    public void testSet() throws Exception {
        objectIdValueProperty.set(testInstance, new ObjectId(1L));
        assertEquals(new ObjectId(1L), testInstance.get("id"));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object result = objectIdValueProperty.setAndReturn(testInstance, new ObjectId(1L));
        assertEquals(new ObjectId(1L), result);
        assertEquals(new ObjectId(1L), testInstance.get("id"));
    }
}