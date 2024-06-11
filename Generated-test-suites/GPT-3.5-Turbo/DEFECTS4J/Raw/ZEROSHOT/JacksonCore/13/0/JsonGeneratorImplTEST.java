import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;
    private IOContext ioContext;
    private ObjectCodec objectCodec;
    
    @Before
    public void setUp() {
        ioContext = mock(IOContext.class);
        objectCodec = mock(ObjectCodec.class);
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, objectCodec) {
            @Override
            public JsonGenerator enable(Feature f) {
                return null;
            }

            @Override
            public JsonGenerator setHighestNonEscapedChar(int charCode) {
                return null;
            }

            @Override
            public int getHighestEscapedChar() {
                return 0;
            }

            @Override
            public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
                return null;
            }

            @Override
            public CharacterEscapes getCharacterEscapes() {
                return null;
            }

            @Override
            public JsonGenerator setRootValueSeparator(SerializableString sep) {
                return null;
            }
        };
    }
    
    @Test
    public void testWriteStringField() throws Exception {
        String fieldName = "fieldName";
        String value = "value";
        jsonGeneratorImpl.writeStringField(fieldName, value);
        verify(jsonGeneratorImpl).writeFieldName(fieldName);
        verify(jsonGeneratorImpl).writeString(value);
    }
}