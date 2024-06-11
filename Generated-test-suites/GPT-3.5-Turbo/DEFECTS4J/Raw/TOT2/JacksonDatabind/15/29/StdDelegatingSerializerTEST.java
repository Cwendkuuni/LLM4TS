import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_NullValue() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<Object>(null, null, null);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        serializer.serialize(null, generator, provider);

        Mockito.verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Object convertedValue = serializer.convertValue("test");

        assertEquals("test", convertedValue);
    }

    @Test
    public void testGetDelegatee() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        JsonSerializer<?> delegatee = serializer.getDelegatee();

        assertEquals(null, delegatee);
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        boolean isEmpty = serializer.isEmpty(null);

        assertTrue(isEmpty);
    }

    @Test
    public void testSchemaFunctionality() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        serializer.getSchema(provider, TypeFactory.defaultInstance().constructType(String.class));
        serializer.getSchema(provider, TypeFactory.defaultInstance().constructType(String.class), false);

        JsonSerializer<?> delegatee = Mockito.mock(JsonSerializer.class);
        serializer.acceptJsonFormatVisitor(delegatee, TypeFactory.defaultInstance().constructType(String.class));
    }
}