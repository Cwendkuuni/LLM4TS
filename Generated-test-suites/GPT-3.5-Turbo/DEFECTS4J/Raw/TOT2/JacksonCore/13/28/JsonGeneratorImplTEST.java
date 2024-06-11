import static org.junit.Assert.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
        
        try {
            generator.writeStringField("testField", "testValue");
        } catch (IOException e) {
            fail("IOException should not be thrown for valid input");
        }
    }

    @Test
    public void testEnable() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
        
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), Feature.ESCAPE_NON_ASCII.getMask(), new ObjectCodec());
        
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
        CharacterEscapes esc = new CharacterEscapes();
        
        generator.setCharacterEscapes(esc);
        assertEquals(esc, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
        SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;
        
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl generator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec());
        assertNotNull(generator.version());
    }

}