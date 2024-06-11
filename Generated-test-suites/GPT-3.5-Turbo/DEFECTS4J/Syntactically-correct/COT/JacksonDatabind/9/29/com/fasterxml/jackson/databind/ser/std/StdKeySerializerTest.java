
import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

public class StdKeySerializerTest {

    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        // Test serialize with Date as value
        Date date = new Date();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new StdSerializerProvider();
        serializer.serialize(date, jgen, provider);

        // Test serialize with other type as value
        String value = "test";
        jgen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, jgen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        // Test getSchema with Type as parameter
        SerializerProvider provider = new StdSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, String.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        // Test acceptJsonFormatVisitor with JavaType as parameter
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }

}
