package com.fasterxml.jackson.core.json;// test package should match the production package

import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

private JsonGeneratorImpl jsonGenerator;

@Before
public void setup() {
jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
}

// Testing writeFieldName method
@Test
public void testWriteFieldName() throws IOException {
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeEndObject();
Assert.assertEquals("{\"testField\":null}", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeString method
@Test
public void testWriteString() throws IOException {
jsonGenerator.writeString("testString");
Assert.assertEquals("\"testString\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing setRootValueSeparator method
@Test
public void testSetRootValueSeparator() throws IOException {
jsonGenerator.setRootValueSeparator(new SerializableString(","));
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
Assert.assertEquals("{},\"testField\":\"testString\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeStartObject method with default root value separator
@Test
public void testWriteStartObject() throws IOException {
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
Assert.assertEquals("{\"testField\":\"testString\"}", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeStartObject method with custom root value separator
@Test
public void testWriteStartObjectWithCustomRootValueSeparator() throws IOException {
jsonGenerator.setRootValueSeparator(new SerializableString(":"));
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
Assert.assertEquals(":{\"testField\":\"testString\"}", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeStartArray method
@Test
public void testWriteStartArray() throws IOException {
jsonGenerator.writeStartArray();
jsonGenerator.writeString("testString");
jsonGenerator.writeEndArray();
Assert.assertEquals("[\"testString\"]", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeEndObject method
@Test
public void testWriteEndObject() throws IOException {
jsonGenerator.writeStartObject();
jsonGenerator.writeStringField("testField", "testString");
jsonGenerator.writeEndObject();
Assert.assertEquals("{\"testField\":\"testString\"}", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeNull method
@Test
public void testWriteNull() throws IOException {
jsonGenerator.writeNull();
Assert.assertEquals("null", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeBoolean method
@Test
public void testWriteBoolean() throws IOException {
jsonGenerator.writeBoolean(true);
Assert.assertEquals("true", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.writeBoolean(false);
Assert.assertEquals("false", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeNumber method
@Test
public void testWriteNumber() throws IOException {
jsonGenerator.writeNumber(123);
Assert.assertEquals("123", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.writeNumber(123.45);
Assert.assertEquals("123.45", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeRawValue method
@Test
public void testWriteRawValue() throws IOException {
jsonGenerator.writeRawValue("testString".getBytes());
Assert.assertEquals("\"testString\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeValueAsString method
@Test
public void testWriteValueAsString() throws IOException {
ObjectCodec objectCodec = null; // assume ObjectCodec is properly set up
jsonGenerator.writeValueAsString("testString", objectCodec);
Assert.assertEquals("\"testString\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing setHighestEscapedChar method
@Test
public void testSetHighestEscapedChar() throws IOException {
jsonGenerator.setHighestNonEscapedChar(127);
jsonGenerator.writeString("test\u0080String");
Assert.assertEquals("\"test\\u0080String\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing setCharacterEscapes method with standard CharacterEscapes
@Test
public void testSetCharacterEscapesWithStandardEscapes() throws IOException {
jsonGenerator.setCharacterEscapes(null);
jsonGenerator.writeRawValue("testString".getBytes());
Assert.assertEquals("\"testString\"", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.setCharacterEscapes(null);
jsonGenerator.writeString("test\\u0080String");
Assert.assertEquals("\"test\\u0080String\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing setCharacterEscapes method with custom CharacterEscapes
@Test
public void testSetCharacterEscapesWithCustomEscapes() throws IOException {
CharacterEscapes customEscapes = new CharacterEscapes() {
@Override
public int[] getEscapeCodesForAscii() {
return new int[]{128, 129}; // Custom escape codes
}
};
jsonGenerator.setCharacterEscapes(customEscapes);
jsonGenerator.writeRawValue("testString".getBytes());
Assert.assertEquals("\"testString\"", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.writeString("test\\u0081String");
Assert.assertEquals("\"test\\u0081String\"", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.writeString("test\\u0082String");
Assert.assertEquals("\"test\\u0081String\\u0082String\"", jsonGenerator.getCharacterOutput().toString());
}

// Testing Quote Field Names feature
@Test
public void testQuoteFieldNames() throws IOException {
jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
Assert.assertEquals("{\"testField\":\"testString\"}", jsonGenerator.getCharacterOutput().toString());
jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("testField");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
Assert.assertEquals("{\"testField\":testString}", jsonGenerator.getCharacterOutput().toString());
}

// Testing disabled Quote Field Names feature with empty field name
@Test(expected = JsonMappingException.class)
public void testDisabledQuoteFieldNamesWithEmptyFieldName() throws IOException {
jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName("");
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
}

// Testing disabled Quote Field Names feature with null field name
@Test(expected = JsonMappingException.class)
public void testDisabledQuoteFieldNamesWithNullFieldName() throws IOException {
jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
jsonGenerator.writeStartObject();
jsonGenerator.writeFieldName(null);
jsonGenerator.writeString("testString");
jsonGenerator.writeEndObject();
}

// Testing writeStartArray with empty array
@Test
public void testWriteStartArrayWithEmptyArray() throws IOException {
jsonGenerator.writeStartArray();
jsonGenerator.writeEndArray();
Assert.assertEquals("[]", jsonGenerator.getCharacterOutput().toString());
}

// Testing writeValueAsString with null value
@Test
public void testWriteValueAsStringWithNullValue() throws IOException {
jsonGenerator.writeValueAsString(null, null);
Assert.assertEquals("null", jsonGenerator.getCharacterOutput().toString());
}
}