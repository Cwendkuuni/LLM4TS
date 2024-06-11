import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        assertNotNull(generator);
    }

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        JsonGenerator result = generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertNotNull(result);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ctxt = new IOContext();
        int features = 0;
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        int charCode = 127;
        JsonGenerator result = generator.setHighestNonEscapedChar(charCode);
        assertNotNull(result);
    }

    @Test
    public void testGetHighestEscapedChar() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        int result = generator.getHighestEscapedChar();
        assertEquals(0, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        CharacterEscapes esc = new CharacterEscapes();
        JsonGenerator result = generator.setCharacterEscapes(esc);
        assertNotNull(result);
    }

    @Test
    public void testGetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        CharacterEscapes result = generator.getCharacterEscapes();
        assertNull(result);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        SerializableString sep = new SerializableString();
        JsonGenerator result = generator.setRootValueSeparator(sep);
        assertNotNull(result);
    }

    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        Version result = generator.version();
        assertNotNull(result);
    }

    @Test
    public void testWriteStringField() throws IOException {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = new ObjectCodec();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, features, codec);
        String fieldName = "test";
        String value = "testValue";
        generator.writeStringField(fieldName, value);
    }
}