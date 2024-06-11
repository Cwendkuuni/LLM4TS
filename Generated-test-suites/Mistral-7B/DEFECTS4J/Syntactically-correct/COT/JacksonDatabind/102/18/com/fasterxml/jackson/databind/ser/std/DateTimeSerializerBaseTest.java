import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateSerializer;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        dateTimeSerializer = objectMapper.findValueSerIALizer(Date.class);
    }

    // Test for basic functionality of constructor
    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Assert.assertNotNull(dateTimeSerializer);
    }

    // Test for withFormat method
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> serializedDateSerializer = dateTimeSerializer.withFormat(false, null);
        DateTimeSerializerBase<Date> customDateFormatSerializer = dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy"));

        Assert.assertNotSame(dateTimeSerializer, serializedDateSerializer);
        Assert.assertNotSame(dateTimeSerializer, customDateFormatSerializer);

        serializedDateSerializer.serialize(new Date(), null, null);
        customDateFormatSerializer.serialize(new Date(), null, null);
    }

    // Test for createContextual method with null property
    @Test(expected = JsonMappingException.class)
    public void testCreateContextualWithNullProperty() {
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null);
        serializer.serialize(null, null, null);
    }

    // Test for createContextual method with null property and format overrides
    @Test
    public void testCreateContextualWithNullPropertyAndFormatOverrides() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("format", "dd-MM-yyyy");

        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        serializer.serialize(null, null, null);
    }

    // Test for createContextual method with property and no format overrides
    @Test
    public void testCreateContextualWithPropertyAndNoFormatOverrides() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("format", null);

        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        serializer.serialize(new Date(), null, null);
    }

    // Test for createContextual method with property and custom format override
    @Test
    public void testCreateContextualWithPropertyAndCustomFormatOverride() throws JsonMappingException {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("format", "dd-MM-yyyy");

        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        Assert.assertTrue(serializer instanceof StdDateSerializer);
        SimpleDateFormat format = ((StdDateSerializer) serializer).getFormat();
        Assert.assertEquals("dd-MM-yyyy", format.toPattern());

        serializer.serialize(new Date(), null, null);
    }

    // Test for isEmpty method
    @Test
    public void testIsEmpty() {
        Assert.assertFalse(dateTimeSerializer.isEmpty(null, null));
    }

    // Test for getSchema method
    @Test
    public void testGetSchema() {
        JsonNode schema = dateTimeSerializer.getSchema(null, Date.class);
        Assert.assertEquals("string", schema.asText());
    }

    // Test for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null);
        dateTimeSerializer.acceptJsonFormatVisitor(visitor, Date.class);
    }

    // Test for serialize method with null value
    @Test(expected = IOException.class)
    public void testSerializeWithNullValue() throws IOException {
        JsonGenerator gen = null;
        dateTimeSerializer.serialize(null, gen, null);
    }

    // Test for serialize method with non-null value
    @Test
    public void testSerializeWithNonNullValue() throws IOException {
        JsonGenerator gen = null;
        Date date = new Date();
        dateTimeSerializer.serialize(date, gen, null);
    }

    // Test for _asTimestamp method
    @Test
    public void testAsTimestamp() {
        Assert.assertTrue(dateTimeSerializer._asTimestamp(null));
        Assert.assertFalse(dateTimeSerializer._asTimestamp(new ObjectMapper()));
    }

    // Test for _acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitorWithFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", "dd-MM-yyyy");

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        dateTimeSerializer._acceptJsonFormatVisitor(visitor, Date.class.getType(), true);
        Assert.assertTrue(((StdDateSerializer) visitor.getSerializer()).getFormat() instanceof SimpleDateFormat);
        SimpleDateFormat format = ((StdDateSerializer) visitor.getSerializer()).getFormat();
        Assert.assertEquals("dd-MM-yyyy", format.toPattern());
    }

    // Test for _acceptJsonFormatVisitor method with null format
    @Test
    public void testAcceptJsonFormatVisitorWithNullFormat() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", null);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        dateTimeSerializer._acceptJsonFormatVisitor(visitor, Date.class.getType(), true);
        Assert.assertNull(((StdDateSerializer) visitor.getSerializer()).getFormat());
    }

    // Test for _serializeAsString method with null generator
    @Test(expected = IOException.class)
    public void testSerializeAsStringWithNullGenerator() throws IOException {
        dateTimeSerializer._serializeAsString(new Date(), null, null);
    }

    // Test for _serializeAsString method with valid date and format
    @Test
    public void testSerializeAsStringWithValidDateAndFormat() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", "dd-MM-yyyy HH:mm:ss z");
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        JsonParser parser = mapper.getFactory().createParser("2022-12-31 23:59:59 EST");
        Date date = parser.readValueAs(Date.class);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, gen, null);
        gen.flush();

        String expectedResult = "\"2022-12-31T23:59:59-05:00\"";
        Assert.assertEquals(expectedResult, parser.getText());
    }

    // Test for _serializeAsString method with valid date and no format
    @Test
    public void testSerializeAsStringWithValidDateAndNoFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, null);
        JsonParser parser = mapper.getFactory().createParser("2022-12-31T23:59:59.000Z");
        Date date = parser.readValueAs(Date.class);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, gen, null);
        gen.flush();

        String expectedResult = "\"2022-12-31T23:59:59.000Z\"";
        Assert.assertEquals(expectedResult, parser.getText());
    }

    // Test for _serializeAsString method with null date
    @Test(expected = IOException.class)
    public void testSerializeAsStringWithNullDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, null);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(null, gen, null);
    }

    // Test for _serializeAsString method with invalid date format
    @Test(expected = IOException.class)
    public void testSerializeAsStringWithInvalidDateFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", "dd-MM-yyyyyy");

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        Date date = new Date();

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, gen, null);
    }

    // Test for _reusedCustomFormat
    @Test
    public void testReusedCustomFormat() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("format", "dd-MM-yyyy HH:mm:ss z");

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer = dateTimeSerializer.createContextual(null, null, node);
        JsonParser parser = mapper.getFactory().createParser("2022-12-31 23:59:59 EST");
        Date date = parser.readValueAs(Date.class);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, gen, null);
        gen.flush();

        String expectedResult = "\"2022-12-31T23:59:59-05:00\"";
        Assert.assertEquals(expectedResult, parser.getText());

        // Perform serialization again to test _reusedCustomFormat
        JsonParser parser2 = mapper.getFactory().createParser("2022-12-31 23:59:59 EST");
        Date date2 = parser2.readValueAs(Date.class);

        JsonGenerator gen2 = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date2, gen2, null);
        gen2.flush();

        String expectedResult2 = "\"2022-12-31T23:59:59-05:00\"";
        Assert.assertEquals(expectedResult2, parser2.getText());

        // Test for reused custom format with different timezones
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("format", "dd-MM-yyyy HH:mm:ss z");
        node2.put("timezone", "UTC");

        JsonFormatVisitorWrapper visitor2 = new JsonFormatVisitorWrapper(null, null, null, null, null);
        JsonSerializer<Object> serializer2 = dateTimeSerializer.createContextual(null, null, node2);
        JsonParser parser3 = mapper.getFactory().createParser("2022-12-31 23:59:59 EST");
        Date date3 = parser3.readValueAs(Date.class);

        JsonGenerator gen3 = mapper.getFactory().createGenerator(System.out);
        serializer2.serialize(date3, gen3, null);
        gen3.flush();

        String expectedResult3 = "\"2022-12-31T08:59:59-05:00\""; // EST -> UTC = GMT-5
        Assert.assertEquals(expectedResult3, parser3.getText());

        JsonParser parser4 = mapper.getFactory().createParser("2022-12-31 23:59:59 UTC");
        Date date4 = parser4.readValueAs(Date.class);

        JsonGenerator gen4 = mapper.getFactory().createGenerator(System.out);
        serializer2.serialize(date4, gen4, null);
        gen4.flush();

        String expectedResult4 = "\"2022-12-31T23:59:59Z\"";
        Assert.assertEquals(expectedResult4, parser4.getText());
    }
}