import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReferenceProperty;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        JsonParser parser = mapper.getFactory().createParser("{\"reference\": 1}");
        Object result = property.deserializeSetAndReturn(parser, new TestContext(), new Object());
        assertNull(result);
    }
}