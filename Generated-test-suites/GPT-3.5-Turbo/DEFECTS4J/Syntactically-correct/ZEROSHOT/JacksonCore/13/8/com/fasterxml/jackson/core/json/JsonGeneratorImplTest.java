import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import java.io.IOException;

public class JsonGeneratorImplTest {
    private JsonGeneratorImpl jsonGenerator;
    
    @Before
    public void setup() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = null;
        jsonGenerator = new JsonGeneratorImpl(ctxt, features, codec);
        jsonGenerator.setHighestNonEscapedChar(127);
        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return CharTypes.get7BitOutputEscapes();
            }
        });
        jsonGenerator.setRootValueSeparator(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }
    
    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "fieldName";
        String value = "fieldValue";
        
        jsonGenerator.writeStringField(fieldName, value);
    }
    
    @Test
    public void testEnable() {
        JsonGenerator.Feature feature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;
        jsonGenerator.enable(feature);
    }
    
    @Test
    public void test_checkStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        
        jsonGenerator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 127;
        
        jsonGenerator.setHighestNonEscapedChar(charCode);
    }
    
    @Test
    public void testGetHighestEscapedChar() {
        int charCode = jsonGenerator.getHighestEscapedChar();
    }
    
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return CharTypes.get7BitOutputEscapes();
            }
        };
        
        jsonGenerator.setCharacterEscapes(esc);
    }
    
    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes esc = jsonGenerator.getCharacterEscapes();
    }
    
    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        
        jsonGenerator.setRootValueSeparator(sep);
    }
}