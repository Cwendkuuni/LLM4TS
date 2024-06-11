import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import static org.mockito.Mockito.*;

public class JsonGeneratorImplTest {
    
    @Mock
    private IOContext mockIOContext;
    
    @Mock
    private ObjectCodec mockCodec;

    @Mock
    private CharacterEscapes mockCharacterEscapes;

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonGeneratorImpl = new JsonGeneratorImpl(mockIOContext, 0, mockCodec);
    }

    @Test
    public void testEnable() {
        jsonGeneratorImpl.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        
        // Verify the correct feature is enabled
        assert(jsonGeneratorImpl.isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER));
        
        // Verify that QUOTE_FIELD_NAMES is not enabled
        assert(!jsonGeneratorImpl.isEnabled(Feature.QUOTE_FIELD_NAMES));
        
        // Verify that _cfgUnqNames is false
        assert(!jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        jsonGeneratorImpl._checkStdFeatureChanges(0, Feature.ALLOW_COMMENTS.getMask());
        
        // Verify that QUOTE_FIELD_NAMES is not enabled
        assert(!jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        
        // Verify that the maximumNonEscapedChar is set correctly
        assert(jsonGeneratorImpl._maximumNonEscapedChar == 128);
    }

    @Test
    public void testGetHighestEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        
        // Verify that the maximumNonEscapedChar is retrieved correctly
        assert(jsonGeneratorImpl.getHighestEscapedChar() == 128);
    }

    @Test
    public void testSetCharacterEscapes() {
        when(mockCharacterEscapes.getEscapeCodesForAscii()).thenReturn(new int[] {32});
        jsonGeneratorImpl.setCharacterEscapes(mockCharacterEscapes);
        
        // Verify that the characterEscapes is set correctly
        assert(jsonGeneratorImpl._characterEscapes == mockCharacterEscapes);
        
        // Verify that the outputEscapes is set correctly
        assert(jsonGeneratorImpl._outputEscapes.length == 1);
        assert(jsonGeneratorImpl._outputEscapes[0] == 32);
    }

    @Test
    public void testGetCharacterEscapes() {
        jsonGeneratorImpl.setCharacterEscapes(mockCharacterEscapes);
        
        // Verify that the characterEscapes is retrieved correctly
        assert(jsonGeneratorImpl.getCharacterEscapes() == mockCharacterEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = mock(SerializableString.class);
        jsonGeneratorImpl.setRootValueSeparator(sep);
        
        // Verify that the rootValueSeparator is set correctly
        assert(jsonGeneratorImpl._rootValueSeparator == sep);
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "field1";
        String value = "value1";
        
        jsonGeneratorImpl.writeFieldName(fieldName);
        jsonGeneratorImpl.writeString(value);
        
        // Verify that writeFieldName and writeString are called with the correct parameters
        verify(jsonGeneratorImpl).writeFieldName(fieldName);
        verify(jsonGeneratorImpl).writeString(value);
    }
}