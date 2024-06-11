import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    @Test
    public void testConstructorWithoutDelegate() {
        Converter<Object, Object> converter = value -> value;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertEquals(Object.class, serializer.handledType());
        assertTrue(serializer.getConverter() instanceof Converter);
    }

    @Test
    public void testConstructorWithDelegate() {
        Converter<Object, Object> converter = value -> value;
        JavaType delegateType = JavaType.forType(Object.class);
        JsonSerializer<Object> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        assertEquals(Object.class, serializer.handledType());
        assertEquals(converter, serializer.getConverter());
        assertEquals(delegateSerializer, serializer.getDelegatee());
    }

    // Add more test cases for other public methods...

}