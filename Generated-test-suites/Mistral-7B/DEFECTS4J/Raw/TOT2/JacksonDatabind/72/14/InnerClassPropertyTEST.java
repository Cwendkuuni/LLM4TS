import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InnerClassPropertyTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testDeserializeAndSet() throws JsonProcessingException, IOException {
        Object bean = new Object();
        InnerClassProperty property = new InnerClassProperty(null, new Object().getClass().getDeclaredConstructor());
        property.deserializeAndSet(mapper.getFactory().createParser("{\"value\":1}"), new DeserializationContext(mapper), bean);
        assertEquals(1, property.getValue().hashCode());
    }
}