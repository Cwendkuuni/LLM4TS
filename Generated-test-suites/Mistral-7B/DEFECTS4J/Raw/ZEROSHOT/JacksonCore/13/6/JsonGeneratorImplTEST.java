package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.core.util.VersionUtil.versionFor;

public class JsonGeneratorImplTest {

 private JsonGeneratorImpl jsonGeneratorImpl;
 private IOContext ioContext;
 private ObjectCodec objectCodec;
 private CharacterEscapes characterEscapes;

 @Before
 public void setUp() {
 ioContext = mock(IOContext.class);
 objectCodec = mock(ObjectCodec.class);
 characterEscapes = mock(CharacterEscapes.class);
 jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec);
 jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
 }

 @Test
 public void testVersion() {
 assertThat(jsonGeneratorImpl.version()).isEqualTo(versionFor(JsonGeneratorImpl.class));
 }

 @Test
 public void testDefaultConfiguration() {
 // test default values
 assertThat(jsonGeneratorImpl._outputEscapes).isEqualTo(JsonGeneratorImpl.sOutputEscapes);
 assertThat(jsonGeneratorImpl._maximumNonEscapedChar).isEqualTo(127);
 assertFalse(jsonGeneratorImpl._cfgUnqNames);
 }

 @Test
 public void testSetHighestNonEscapedChar() {
 jsonGeneratorImpl.setHighestNonEscapedChar(100);
 assertThat(jsonGeneratorImpl._maximumNonEscapedChar).isEqualTo(100);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeHighestNonEscapedChar() {
 jsonGeneratorImpl.setHighestNonEscapedChar(-1);
 }

 @Test
 public void testSetCharacterEscapes() {
 CharacterEscapes esc = new CharacterEscapes() {};
 jsonGeneratorImpl.setCharacterEscapes(esc);
 assertThat(jsonGeneratorImpl._characterEscapes).isSameAs(esc);
 }

 @Test
 public void testGetCharacterEscapes() {
 CharacterEscapes esc = new CharacterEscapes() {};
 jsonGeneratorImpl.setCharacterEscapes(esc);
 assertThat(jsonGeneratorImpl.getCharacterEscapes()).isSameAs(esc);
 }

 @Test
 public void testSetRootValueSeparator() {
 SerializableString rootValueSeparator = new SerializableString("root");
 jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
 assertThat(jsonGeneratorImpl._rootValueSeparator).isEqualTo(rootValueSeparator);
 }

 @Test
 public void testEnableQuoteFieldNames() throws IOException {
 jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
 writeTestString("{\"field\":1}");
 }

 @Test
 public void testDisableQuoteFieldNames() throws IOException {
 jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
 jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_NAMES);
 writeTestString("{\\ field:1}");
 }

 private void writeTestString(String expected) throws IOException {
 StringWriter stringWriter = new StringWriter();
 jsonGeneratorImpl.writeValueAsString(new Object() {}, stringWriter);
 assertThat(stringWriter.toString()).isEqualTo(expected);
 }
}