import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class));
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer._deserializeCustom(mock(JsonParser.class), mock(DeserializationContext.class));
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        Object result = deserializer.deserializeWithType(mock(JsonParser.class), mock(DeserializationContext.class), mock(TypeDeserializer.class));
        assertNotNull(result);
    }
    
    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(mock(JsonParser.class), mock(DeserializationContext.class));
        assertNotNull(result);
    }
    
    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> result = deserializer.createContextual(mock(DeserializationContext.class), mock(BeanProperty.class));
        assertNotNull(result);
    }
}