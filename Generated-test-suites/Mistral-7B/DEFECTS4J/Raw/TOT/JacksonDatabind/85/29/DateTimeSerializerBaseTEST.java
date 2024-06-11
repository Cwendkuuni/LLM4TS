import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerializeDeserializeDate() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> dateSerializer = new DateSerializer(
                LocalDateTime.class, false, null);

        ObjectMapper mapper = new ObjectMapper();
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 12, 30);
        JsonNode jsonNode = mapper.valueToTree(date);
        LocalDateTime deserializedDate = mapper.treeToValue(jsonNode, LocalDateTime.class);
        Assertions.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializeDeserializeDateWithTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new DateSerializer(
                ZonedDateTime.class, false, null);

        ObjectMapper mapper = new ObjectMapper();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 1, 1, 12, 30, 0, 0, ZoneId.systemDefault());
        JsonNode jsonNode = mapper.valueToTree(zonedDateTime);
        ZonedDateTime deserializedZonedDateTime = mapper.treeToValue(jsonNode, ZonedDateTime.class);
        Assertions.assertEquals(zonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testSerializeDeserializeDateWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> dateSerializer = new DateSerializer(
                LocalDateTime.class, false,  new SimpleDateFormat("dd-MM-yyyy HH:mm"));

        ObjectMapper mapper = new ObjectMapper();
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 12, 30);
        String dateString = "01-01-2022 12:30";
        JsonNode jsonNode = mapper.valueToTree(dateString);
        LocalDateTime deserializedDate = mapper.treeToValue(jsonNode, LocalDateTime.class);
        Assertions.assertEquals(date, deserializedDate);
    }

    @Test
    public void testSerializeDeserializeDateWithCustomFormatAndTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new DateSerializer(
                ZonedDateTime.class, false,  new SimpleDateFormat("dd-MM-yyyy HH:mm z"));

        ObjectMapper mapper = new ObjectMapper();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 1, 1, 12, 30, 0, 0, ZoneId.of("UTC"));
        String zonedDateTimeString = "01-01-2022 12:30 UTC";
        JsonNode jsonNode = mapper.valueToTree(zonedDateTimeString);
        ZonedDateTime deserializedZonedDateTime = mapper.treeToValue(jsonNode, ZonedDateTime.class);
        Assertions.assertEquals(zonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testSerializeDeserializeDateWithEmptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<Instant> instantSerializer = new DateSerializer(
                Instant.class, true, null);

        ObjectMapper mapper = new ObjectMapper();
        Instant emptyValue = null;
        JsonNode jsonNode = mapper.valueToTree(emptyValue);
        Instant deserializedEmptyValue = mapper.treeToValue(jsonNode, Instant.class);
        Assertions.assertNull(deserializedEmptyValue);
    }

    @Test
    public void testSerializeDeserializeDateWithSchema() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> dateSerializer = new DateSerializer(
                LocalDateTime.class, false, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator();
        JsonSchema schema = schemaGenerator.findSchemaForAnnnotatedClass(dateSerializer.getClass());
        JsonNode jsonNode = mapper.treeToNode(schema);
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 12, 30);
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.set("date", JsonNodeFactory.valueNode(date));
        ObjectNode schemaObject = mapper.createObjectNode();
        schemaObject.setAll(jsonNode);
        JsonNode validationResult = mapper.validate(jsonObject, schemaObject);
        Assertions.assertNull(validationResult.get("errors"));
    }
}