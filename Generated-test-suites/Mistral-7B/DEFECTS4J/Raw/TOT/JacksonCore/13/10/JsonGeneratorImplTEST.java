package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGenerator;
 private StringWriter stringWriter;

 @Before
 public void setUp() throws IOException {
 stringWriter = new StringWriter();
 jsonGenerator = new JsonFactory(new ObjectMapper.DefaultObjectMapperBuilder().build())
 .enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
 .createGenerator(stringWriter);
 }

 @Test
 public void testWriteStringField() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String expectedJson = "{\"name\":\"John Doe\"}";
 assertEquals(expectedJson, stringWriter.toString());
 }

 @Test
 public void testWriteEmptyStringField() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "");
 jsonGenerator.writeEndObject();
 String expectedJson = "{\"name\":\"\"}";
 assertEquals(expectedJson, stringWriter.toString());
 }

 @Test
 public void testWriteNullField() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", null);
 jsonGenerator.writeEndObject();
 String expectedJson = "{\"name\":null}";
 assertEquals(expectedJson, stringWriter.toString());
 }

 @Test(expected = IOException.class)
 public void testWriteFieldWithInvalidCharacter() throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "\uD800"); // high surrogate
 jsonGenerator.writeEndObject();
 }

 @Test
 public void testWriteRootValueSeparator() throws IOException {
 jsonGenerator.setRootValueSeparator(new TextFormatter.QuotedStandard(":"));
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("name", "John Doe");
 jsonGenerator.writeEndObject();
 String expectedJson = "{\":name\":\"John Doe\"}";
 assertEquals(expectedJson, stringWriter.toString());
 }

 @After
 public void tearDown() throws IOException {
 jsonGenerator.close();
 stringWriter.close();
 }
}