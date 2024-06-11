import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader("id"), new PropertyMetadata());
        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put("id", "123");
        JsonParser jsonParser = new JsonFactory().createParser(jsonNode);
        Object instance = new Object();

        property.deserializeAndSet(jsonParser, new TestContext(), instance);

        Object id = property.getIdValue(); // Assuming there's a getter for idValue
        assertEquals("123", id);
    }
}