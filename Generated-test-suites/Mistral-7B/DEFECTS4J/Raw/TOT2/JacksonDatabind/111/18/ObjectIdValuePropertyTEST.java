import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonTest;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ObjectIdValuePropertyTest extends JsonTest {

    ObjectIdValueProperty objectIdValueProperty;
    ObjectMapper objectMapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader(), new PropertyMetadata());
        objectMapper = new ObjectMapper();
    }

    private ObjectIdReader mockObjectIdReader() {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn("id");
        when(mockObjectIdReader.getIdType()).thenReturn(String.class);
        when(mockObjectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        return mockObjectIdReader;
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        String expectedId = "testId";
        ObjectNode testNode = objectMapper.createObjectNode();
        testNode.put("id", expectedId);

        JsonParser jsonParser = objectMapper.treeAsTokens(testNode);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertEquals(expectedId, objectIdValueProperty.getValue(instance));
    }
}