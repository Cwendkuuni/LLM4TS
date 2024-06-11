import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.type.TypeReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}, null, null, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        AtomicReference<Object> result = deserializer.getNullValue(ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getClass(), AtomicReference.class);
    }
}