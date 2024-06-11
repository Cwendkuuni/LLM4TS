import org.junit.*;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
  
  @Test
  public void testEnable() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
    assertFalse(generator._cfgUnqNames);
  }
  
  @Test
  public void test_checkStdFeatureChanges() {
    IOContext ctxt = new IOContext();
    int newFeatureFlags = 0;
    int changedFeatures = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, newFeatureFlags, codec);
    generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
    assertTrue(generator._cfgUnqNames);
  }
  
  @Test
  public void test_setHighestNonEscapedChar() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    generator.setHighestNonEscapedChar(127);
    assertEquals(127, generator._maximumNonEscapedChar);
  }
  
  @Test
  public void test_getHighestEscapedChar() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    generator._maximumNonEscapedChar = 127;
    assertEquals(127, generator.getHighestEscapedChar());
  }
  
  @Test
  public void test_setCharacterEscapes() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    CharacterEscapes esc = new CharacterEscapes();
    generator.setCharacterEscapes(esc);
    assertEquals(esc, generator._characterEscapes);
  }
  
  @Test
  public void test_getCharacterEscapes() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    CharacterEscapes esc = new CharacterEscapes();
    generator._characterEscapes = esc;
    assertEquals(esc, generator.getCharacterEscapes());
  }
  
  @Test
  public void test_setRootValueSeparator() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;
    generator.setRootValueSeparator(sep);
    assertEquals(sep, generator._rootValueSeparator);
  }
  
  @Test
  public void test_version() {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), generator.version());
  }
  
  @Test
  public void test_writeStringField() throws IOException {
    IOContext ctxt = new IOContext();
    int features = 0;
    ObjectCodec codec = new ObjectCodec();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
    MySerializableString fieldName = new MySerializableString();
    MyString value = new MyString();
    generator.writeStringField(fieldName, value);
    assertEquals(fieldName, generator._fieldName);
    assertEquals(value, generator._value);
  }
  
  private class ObjectCodec {}
  
  private class MySerializableString extends SerializableString {}
  
  private class MyString extends String {}
}