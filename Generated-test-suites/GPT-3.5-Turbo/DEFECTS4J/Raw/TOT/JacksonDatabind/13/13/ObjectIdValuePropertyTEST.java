import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.Mock;

public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader objectIdReader;

    @Mock
    private PropertyMetadata metadata;

    @Mock
    private JsonDeserializer<?> deserializer;

    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testWithFieldDeserializer() {
        JsonDeserializer<?> newDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(newDeserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        Mockito.doReturn(1).when(deserializer).deserialize(jsonParser, deserializationContext);
        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnThrowsException() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdValueProperty.setAndReturn(instance, value);
    }
}