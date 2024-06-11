package com.example.junit4.json;

import com.fasterxml.jackson.core.*;
import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

@Test
public void testJsonGeneratorImplConstructor() {
    IOContext context = new IOContext();
    ObjectCodec codec = new ObjectMapper().getFactory().getCodec();
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, codec);
    assertNotNull(jsonGenerator);
}

@Test
public void testEnableFeature() {
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    assertFalse(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
    assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
}

@Test
public void testGetRootValueSeparator() {
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    SerializableString defaultRootValueSeparator = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
    assertEquals(defaultRootValueSeparator, jsonGenerator.getRootValueSeparator());
}

@Test(expected = IllegalArgumentException.class)
public void testJsonGeneratorImplConstructorWithNullContext() {
    ObjectCodec codec = new ObjectMapper().getFactory().getCodec();
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, codec);
}

@Test(expected = IllegalArgumentException.class)
public void testJsonGeneratorImplConstructorWithNullCodec() {
    IOContext context = new IOContext();
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
}

@Test
public void testSetCharacterEscapesWithNullCharacterEscapes() {
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    jsonGenerator.setCharacterEscapes(null);
    assertNull(jsonGenerator.getCharacterEscapes());
}

@Test
public void testSetCharacterEscapesWithValidCharacterEscapes() {
    CharacterEscapes characterEscapes = new CharacterEscapes() {};
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    jsonGenerator.setCharacterEscapes(characterEscapes);
    assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
}

@Test(expected = IllegalArgumentException.class)
public void testSetRootValueSeparatorWithNullSerializableString() {
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    jsonGenerator.setRootValueSeparator(null);
}

@Test
public void testSetRootValueSeparatorWithValidSerializableString() {
    JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectMapper().getFactory().getCodec());
    SerializableString rootValueSeparator = new SerializableString(":");
    jsonGenerator.setRootValueSeparator(rootValueSeparator);
    assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
}

}