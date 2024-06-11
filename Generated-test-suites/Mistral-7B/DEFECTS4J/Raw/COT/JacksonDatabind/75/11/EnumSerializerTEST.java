import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;

public class EnumSerializerTest {

 private EnumSerializer enumSerializer;
 private EnumValues enumValues;

 @Before
 public void setup() {
 MyEnum enumType = MyEnum.values()[0];
 enumValues = EnumValues.constructFromName(null, (Class<Enum<?>>) enumType.getDeclaringClass());
 enumSerializer = new EnumSerializer(enumValues, null);
 }

 @Test
 public void testSerializeAsIndex() throws IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 enumSerializer.serialize(MyEnum.VALUE1, gen, null);
 assertTrue(gen.getCurrentEventId() == JsonGenerator.Event.START_NUMBER);
 assertEquals(MyEnum.VALUE1.ordinal(), gen.getOutputValueAsInt());
 }

 @Test
 public void testSerializeAsString() throws IOException {
 JsonGenerator gen = new JsonFactory().createGenerator(System.out);
 enumSerializer.serialize(MyEnum.VALUE1, gen, null);
 assertTrue(gen.getCurrentEventId() == JsonGenerator.Event.START_STRING);
 assertEquals(MyEnum.VALUE1.name(), gen.getOutputValueAsString());
 }

 @Test
 public void testGetSchema() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = enumSerializer.getSchema(mapper.getSerializerProvider(), getTypeOf(MyEnum.class));
 assertTrue(schema.isObject());
 ObjectNode schemaObject = (ObjectNode) schema;
 assertTrue(schemaObject.has("type"));
 assertEquals("string", schemaObject.get("type").asText());
 assertTrue(schemaObject.has("enum"));
 ArrayNode enumNode = schemaObject.get("enum");
 assertEquals(2, enumNode.size());
 assertEquals(MyEnum.VALUE1.name(), enumNode.get(0).asText());
 assertEquals(MyEnum.VALUE2.name(), enumNode.get(1).asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormat.Value format = new JsonFormat.Value();
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(format, null);
 enumSerializer.acceptJsonFormatVisitor(visitor, getTypeOf(MyEnum.class));
 assertTrue(format.getShape() == Shape.STRING);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedSerializationShape() throws JsonMappingException {
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.INT);
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(format, null);
 enumSerializer.acceptJsonFormatVisitor(visitor, getTypeOf(MyEnum.class));
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(null, null);
 assertSame(enumSerializer, contextualSerializer);
 }

 @Test
 public void testCreateContextualWithJsonFormat() throws JsonMappingException {
 JsonFormat format = new JsonFormat();
 format.setShape(JsonFormat.Shape.STRING);
 JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(null, null);
 JsonSerializer<?> serializedSerializer = contextualSerializer.createContextual(null, null);
 assertNotSame(enumSerializer, serializedSerializer);
 assertEquals(format.getShape(), serializedSerializer.getShape());
 }

 @Test
 public void testGetEnumValues() {
 assertEquals(2, enumValues.size());
 }

 private static Type getTypeOf(Class<?> clazz) {
 return new TypeFactory().constructType(clazz);
 }

 enum MyEnum {
 @JsonFormat(shape = Shape.STRING)
 VALUE1,
 @JsonFormat(shape = Shape.NUMBER)
 VALUE2
 }
}