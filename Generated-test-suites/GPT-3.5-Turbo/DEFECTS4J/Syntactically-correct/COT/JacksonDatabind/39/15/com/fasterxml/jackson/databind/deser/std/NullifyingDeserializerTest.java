
import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer deser = new NullifyingDeserializer();
        JsonParser parser = newMock(JsonParser.class);
        DeserializationContext context = newMock(DeserializationContext.class);
        
        Object result = deser.deserialize(parser, context);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer deser = new NullifyingDeserializer();
        JsonParser parser = newMock(JsonParser.class);
        DeserializationContext context = newMock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = newMock(TypeDeserializer.class);
        
        Object result = deser.deserializeWithType(parser, context, typeDeserializer);
        
        Assert.assertNull(result);
    }
}
