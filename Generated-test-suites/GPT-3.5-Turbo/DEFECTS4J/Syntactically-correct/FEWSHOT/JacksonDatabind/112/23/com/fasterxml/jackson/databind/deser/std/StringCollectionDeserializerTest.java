import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

public class StringCollectionDeserializerTest {
    
    private StringCollectionDeserializer deserializer;
    private JsonDeserializer<String> valueDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private JavaType collectionType;
    private NullValueProvider nullValueProvider;
    
    @Before
    public void setUp() {
        collectionType = mock(JavaType.class);
        valueDeserializer = mock(JsonDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        deserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator);
        deserializer = deserializer.withResolved(delegateDeserializer, valueDeserializer, nullValueProvider, true);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        assertEquals(null, deserializer.deserialize(parser, context));
    }
    
    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        assertEquals(null, deserializer.deserializeUsingCustom(parser, context, null, customDeserializer));
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        assertEquals(null, deserializer.handleNonArray(parser, context, null));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        assertEquals(null, deserializer.deserializeWithType(parser, context, null));
    }
    
    @Test
    public void testGetContentDeserializer() throws IOException {
        assertEquals(valueDeserializer, deserializer.getContentDeserializer());
    }
    
    @Test
    public void testGetValueInstantiator() throws IOException {
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
    }
    
    @Test
    public void testIsCachable() throws IOException {
        assertEquals(true, deserializer.isCachable());
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType valueType = mock(JavaType.class);
        BeanProperty property = mock(BeanProperty.class);
        assertEquals(deserializer, deserializer.createContextual(context, property));
        verify(context, times(1)).findConvertingContentDeserializer((DeserializationContext) any(), (BeanProperty) any(), (JsonDeserializer<?>) any());
        verify(context, times(1)).findContextualValueDeserializer(valueType, property);
    }
}