import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumberNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class DateTimeSerializerBaseTest {

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);

        // Test with null value
        assertTrue(serializer.isEmpty(null));

        // Test with zero timestamp
        Object testObject = new Object() {
            private long timestamp = 0;
        };
        assertTrue(serializer.isEmpty(testObject));

        // Test with an instance of the serializer
        assertFalse(serializer.isEmpty(serializer));

        // Test with a non-null and non-zero timestamp
        Object nonEmptyObject = new Object() {
            private long timestamp = 1234567890L;
        };
        assertFalse(serializer.isEmpty(nonEmptyObject));
    }
}