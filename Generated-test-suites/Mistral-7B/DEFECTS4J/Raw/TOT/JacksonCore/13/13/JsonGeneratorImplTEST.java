package com.fasterxml.jackson.core.json; // or the package where the JsonGeneratorImpl class is located

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

 private static final IOContext context = new IOContext();
 private static final ObjectCodec codec = new ObjectMapper().getFactory().getEncoder();

 @Test
 public void testWriteStringField() throws IOException {
 JsonGeneratorImpl jsonGenerator = createJsonGenerator();

 // Write a simple string field
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1");
 jsonGenerator.writeEndObject();

 String jsonString = getJsonStringFromJsonGenerator(jsonGenerator);
 JsonParser jsonParser = codec.getFactory().createParser(jsonString);
 ObjectMap map = (ObjectMap) jsonParser.readValueAs(ObjectMap.class);
 assertEquals("value1", map.get("field1"));

 // Write a string field with special characters
 jsonGenerator = createJsonGenerator();
 jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
 @Override
 public int getEscapeCodeForChar(int c) {
 return JsonGeneratorImpl.sOutputEscapes[c & 0xFF];
 }
 });
 jsonGenerator.writeStartObject();
 jsonGenerator.writeStringField("field1", "value1\\/backslash\\" + "value2\"quoted\"");
 jsonGenerator.writeEndObject();

 jsonString = getJsonStringFromJsonGenerator(jsonGenerator);
 jsonParser = codec.getFactory().createParser(jsonString);
 map = (ObjectMap) jsonParser.readValueAs(ObjectMap.class);
 assertEquals("value1\\/backslash\\" + "value2\"quoted\"", map.get("field1"));
 }

 private JsonGeneratorImpl createJsonGenerator() {
 return new JsonGeneratorImpl(context, Feature.AUTO_CLOSE_TARGET.getMask(), codec);
 }

 private String getJsonStringFromJsonGenerator(JsonGeneratorImpl jsonGenerator) throws IOException {
 StringWriter writer = new StringWriter();
 jsonGenerator.setWriter(writer);
 jsonGenerator.close();
 return writer.toString();
 }
}