package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerializers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.ser.std.EnumValues;

public class EnumSerializerTest {

 private EnumValues enumValues;
 private EnumSerializer enumSerializer;
 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 enumValues = EnumValues.constructFromName(MyEnum.class);
 serializerProvider = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 enumSerializer = new EnumSerializer(enumValues, null);
 }

 @Test
 public void testSerializeEnum() throws IOException {
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeNumber(0)).thenCallRealMethod();
 when(jsonGenerator.writeString("VALUE_1")).thenCallRealMethod();

 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

 enumSerializer.serialize(MyEnum.VALUE_1, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeNumber(0);
 }

 @Test
 public void testSerializeEnumWithToString() throws IOException {
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeNumber(0)).thenCallRealMethod();
 when(jsonGenerator.writeString("VALUE_1")).thenCallRealMethod();
 when(jsonGenerator.writeString("TO_STRING_VALUE_1")).thenCallRealMethod();

 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);

 enumSerializer.serialize(MyEnum.VALUE_1, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeString("TO_STRING_VALUE_1");
 }

 @Test
 public void testSerializeEnumWithName() throws IOException {
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.writeString("VALUE_1_NAME")).thenCallRealMethod();

 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
 when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(false);

 enumSerializer.serialize(MyEnum.VALUE_1, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeString("VALUE_1_NAME");
 }

 @Test
 public void testSerializeArrayOfEnum() throws IOException {
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 ObjectNode rootNode = mock(ObjectNode.class);
 ArrayNode enumArrayNode = mock(ArrayNode.class);

 when(jsonGenerator.writeStartArray()).thenCallRealMethod();
 when(jsonGenerator.writeValue(enumArrayNode)).thenCallRealMethod();
 when(jsonGenerator.writeEnd()).thenCallRealMethod();

 when(rootNode.putArray("enumArray")).thenReturn(enumArrayNode);
 when(objectMapper.writeValueAsTree(new Object[] { MyEnum.VALUE_1, MyEnum.VALUE_2 }, null)).thenReturn(rootNode);
 when(serializerProvider.getEncoder()).thenReturn(objectMapper.getSerializerProvider());

 EnumSerializer enumSerializer = new EnumSerializer(enumValues, null);
 JsonNode result = enumSerializer.serialize(new Object[] { MyEnum.VALUE_1, MyEnum.VALUE_2 }, jsonGenerator, serializerProvider);

 verify(jsonGenerator).writeStartArray();
 verify(jsonGenerator).writeValue(enumArrayNode);
 verify(jsonGenerator).writeEnd();
 verify(rootNode).put("enumArray", enumArrayNode);

 assertEquals(MyEnum.VALUE_1.name(), result.get("enumArray").get(0).asText());
 assertEquals(MyEnum.VALUE_2.name(), result.get("enumArray").get(1).asText());
 }

 @Test
 public void testGetSchema() throws IOException {
 JsonNode schema = enumSerializer.getSchema(serializerProvider, MyEnum.class);

 assertEquals("string", schema.get("type").asText());
 Set<String> expectedValues = new LinkedHashSet<>(Arrays.asList(MyEnum.VALUE_1.name(), MyEnum.VALUE_2.name()));
 Set<String> schemaValues = new LinkedHashSet<>();

 for (JsonNode enumNode : schema.get("enum")) {
 schemaValues.add(enumNode.asText());
 }

 assertEquals(expectedValues, schemaValues);
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws IOException {
 JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.ARRAY);
 jsonFormatValue.setShape(jsonFormat);

 ObjectNode schemaNode = mock(ObjectNode.class);
 JsonFormatVisitorWrapper jsonFormatVisitor = mock(JsonFormatVisitorWrapper.class);
 when(schemaNode.putArray("enum")).thenReturn(mock(ArrayNode.class));

 when(jsonFormatVisitor.expectStringFormat(MyEnum.class)).thenReturn(mock(JsonStringFormatVisitor.class));
 when(jsonFormatVisitor.getProvider()).thenReturn(serializerProvider);

 enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, new ClassType(MyEnum.class));

 verify(schemaNode).putArray("enum");
 }

 @Test
 public void testAcceptJsonFormatVisitorWithToString() throws IOException {
 JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormatValue.setShape(jsonFormat);

 ObjectNode schemaNode = mock(ObjectNode.class);
 JsonFormatVisitorWrapper jsonFormatVisitor = mock(JsonFormatVisitorWrapper.class);
 Set<String> expectedValues = EnumSet.allOf(MyEnum.class).stream().map(Enum::name).collect(Collectors.toSet());

 when(jsonFormatVisitor.expectStringFormat(MyEnum.class)).thenReturn(mock(JsonStringFormatVisitor.class));
 when(jsonFormatVisitor.getProvider()).thenReturn(serializerProvider);
 when(jsonFormatVisitor.getProvider().isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

 enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, new ClassType(MyEnum.class));

 verify(schemaNode).putArray("enum");
 for (String expectedValue : expectedValues) {
 verify(jsonFormatVisitor.getProvider().getEncoder()).writeValueAsString(expectedValue);
 }
 }

 @Test
 public void testAcceptJsonFormatVisitorWithName() throws IOException {
 JsonFormat.Value jsonFormatValue = new JsonFormat.Value();
 JsonFormat jsonFormat = new JsonFormat();
 jsonFormat.setShape(JsonFormat.Shape.STRING);
 jsonFormatValue.setShape(jsonFormat);

 ObjectNode schemaNode = mock(ObjectNode.class);
 JsonFormatVisitorWrapper jsonFormatVisitor = mock(JsonFormatVisitorWrapper.class);
 Set<String> expectedValues = EnumSet.allOf(MyEnum.class).stream().map(Enum::name).collect(Collectors.toSet());

 when(jsonFormatVisitor.expectStringFormat(MyEnum.class)).thenReturn(mock(JsonStringFormatVisitor.class));
 when(jsonFormatVisitor.getProvider()).thenReturn(serializerProvider);
 when(jsonFormatVisitor.getProvider().isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

 enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitor, new ClassType(MyEnum.class));

 verify(schemaNode).putArray("enum");
 for (Enum<?> value : MyEnum.values()) {
 verify(jsonFormatVisitor.getProvider().getEncoder()).writeValueAsString(value.name());
 }
 }

}

 enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 VALUE_1,
 @JsonFormat(shape = JsonFormat.Shape.STRING, value = "TO_STRING_VALUE_1")
 TO_STRING_VALUE_1
}