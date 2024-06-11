import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testTimestampSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Instant> timestampSerializer = mapper.findValueSerializer(Instant.class);
        Instant testInstance = Instant.now();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        timestampSerializer.serialize(testInstance, generator, null);
        generator.flush();

        ByteArrayOutputStream byteStream = (ByteArrayOutputStream) generator.getOutputStandaloneStream();
        String serializedInstance = new String(byteStream.toByteArray());
        assertNotNull(serializedInstance);
        assertEquals(Long.toString(testInstance.getEpochSecond()), serializedInstance);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<ZonedDateTime> dateTimeSerializer = mapper.findValueSerializer(ZonedDateTime.class);

        ZonedDateTime testInstance = ZonedDateTime.now(ZoneId.systemDefault());
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        dateTimeSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz"));
        dateTimeSerializer.serialize(testInstance, generator, null);
        generator.flush();

        ByteArrayOutputStream byteStream = (ByteArrayOutputStream) generator.getOutputStandaloneStream();
        String serializedInstance = new String(byteStream.toByteArray());
        assertNotNull(serializedInstance);
        System.out.println(serializedInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullInstance() throws JsonGenerationException, JsonMappingException {
        DateTimeSerializerBase<String> stringSerializer = mapper.findValueSerializer(String.class);

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        stringSerializer.serialize(null, generator, null);
    }

    @Test(expected = JsonGenerationException.class)
    public void testInvalidCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<ZonedDateTime> dateTimeSerializer = mapper.findValueSerializer(ZonedDateTime.class);

        ZonedDateTime testInstance = ZonedDateTime.now(ZoneId.systemDefault());
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        dateTimeSerializer = dateTimeSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z"));
        dateTimeSerializer.serialize(testInstance, generator, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnsupportedType() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Object> objectSerializer = mapper.findValueSerializer(Object.class);
        Object testInstance = new Object();

        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        objectSerializer.serialize(testInstance, generator, null);
    }
}