import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        String objectId = "objectIdValue";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("objectId"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getDeserializer()).thenReturn(new StdDeserializer<>());
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("objectId", objectId);

        JsonParser jsonParser = new JsonFactory().createParser(jsonNode);

        Object targetInstance = new Object();

        objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), targetInstance);

        ObjectId id = mock(ReadableObjectId.class);
        when(id.bindItem(targetInstance)).thenReturn(targetInstance);
        when(objectIdReader.idProperty).thenReturn(new ObjectIdValueProperty(objectIdValueProperty, new JsonDeserializer<>()));

        Object result = objectIdValueProperty.setAndReturn(targetInstance, objectId);

        assertEquals(objectId, result);
        verify(objectIdReader, times(1)).getIdType();
        verify(objectIdReader, times(1)).propertyName;
        verify(objectIdReader, times(1)).getDeserializer();
        verify(objectIdReader, times(1)).generator;
        verify(objectIdReader, times(1)).resolver;
        verify(objectIdReader.idProperty, times(1)).setAndReturn(targetInstance, objectId);
    }
}