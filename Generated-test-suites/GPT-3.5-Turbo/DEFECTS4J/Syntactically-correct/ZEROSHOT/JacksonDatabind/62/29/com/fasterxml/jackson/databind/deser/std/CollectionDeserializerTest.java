import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JavaType collectionType = new TypeReference<List<String>>() {}.getType();
        List<String> result = new ArrayList<>();
        JsonDeserializer<Object> valueDeser = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeser = Mockito.mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        JsonDeserializer<Object> delegateDeser = Mockito.mock(JsonDeserializer.class);
        Boolean unwrapSingle = Boolean.TRUE;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(collectionType, valueDeser, valueTypeDeser, valueInstantiator, delegateDeser, unwrapSingle);

        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(p.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(valueDeser.getNullValue(ctxt)).thenReturn(null);
        Mockito.when(delegateDeser.deserialize(p, ctxt)).thenReturn(null);
        Mockito.when(valueInstantiator.createUsingDelegate(ctxt, null)).thenReturn(result);

        Collection<Object> actual = collectionDeserializer.deserialize(p, ctxt);
        assertEquals(result, actual);
        Mockito.verify(p).setCurrentValue(result);
        Mockito.verify(valueDeser).getNullValue(ctxt);
        Mockito.verify(delegateDeser).deserialize(p, ctxt);
        Mockito.verify(valueInstantiator).createUsingDelegate(ctxt, null);
    }
}