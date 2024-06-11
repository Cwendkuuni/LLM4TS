import static org.junit.Assert.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        // Test constructor initialization
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        
        assertNotNull(generator);
        assertEquals(ctxt, generator._ioContext);
        assertEquals(0, generator._maximumNonEscapedChar);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        // Test enabling Quote Field Names feature
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        // Test setting the highest non-escaped char
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        generator.setHighestNonEscapedChar(100);
        
        assertEquals(100, generator._maximumNonEscapedChar);
    }

    @Test
    public void testSetCharacterEscapes() {
        // Test setting custom character escapes
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        CharacterEscapes escapes = new CharacterEscapes();
        generator.setCharacterEscapes(escapes);
        
        assertEquals(escapes, generator._characterEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        // Test setting root value separator
        IOContext ctxt = new IOContext();
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        SerializableString separator = new SerializableString(";");
        generator.setRootValueSeparator(separator);
        
        assertEquals(separator, generator._rootValueSeparator);
    }

}