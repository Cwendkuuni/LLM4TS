import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }
  
    @Test
    public void test_setHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator._maximumNonEscapedChar);
    }
  
    @Test
    public void test_getHighestEscapedChar() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator.getHighestEscapedChar());
    }
  
    @Test
    public void test_setCharacterEscapes() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        CharacterEscapes esc = new CharacterEscapes();
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator.getCharacterEscapes());
    }
  
    @Test
    public void test_setRootValueSeparator() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        SerializableString sep = new SerializableString();
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }
  
}