import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {
    
    private JsonDeserializer<String> valueDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private StringCollectionDeserializer stringCollectionDeserializer;
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = new JsonFactory().createParser("[\"one\",\"two\",\"three\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();
        stringCollectionDeserializer.deserialize(p, ctxt, result);
        assertEquals(3, result.size());
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
        assertTrue(result.contains("three"));
    }
    
    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonParser p = new JsonFactory().createParser("[\"one\",\"two\",\"three\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();
        stringCollectionDeserializer.deserializeUsingCustom(p, ctxt, result, valueDeserializer);
        assertEquals(3, result.size());
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
        assertTrue(result.contains("three"));
    }
    
    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser p = new JsonFactory().createParser("\"one\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Collection<String> result = new ArrayList<>();
        stringCollectionDeserializer.handleNonArray(p, ctxt, result);
        assertEquals(1, result.size());
        assertTrue(result.contains("one"));
    }
}