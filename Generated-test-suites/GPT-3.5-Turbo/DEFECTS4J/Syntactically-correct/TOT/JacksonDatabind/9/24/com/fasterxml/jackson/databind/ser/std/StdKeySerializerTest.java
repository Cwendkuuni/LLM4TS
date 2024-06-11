import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new ObjectMapper().getJsonFactory().createGenerator(System.out);

        Date date = new Date();
        serializer.serialize(date, jgen, SerializerProvider.createDefault());
        
        assertEquals(jgen.getOutputContext().getCurrentName(), SerializerProvider.defaultDateFormat().format(date));
    }

    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new ObjectMapper().getJsonFactory().createGenerator(System.out);

        String str = "test";
        serializer.serialize(str, jgen, SerializerProvider.createDefault());

        assertEquals(jgen.getOutputContext().getCurrentName(), str);
    }

    @Test
    public void testSerializeNull() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new ObjectMapper().getJsonFactory().createGenerator(System.out);

        serializer.serialize(null, jgen, SerializerProvider.createDefault());

        assertEquals(jgen.getOutputContext().getCurrentName(), "null");
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = SerializerProvider.createDefault();
        assertNotNull(serializer.getSchema(provider, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}