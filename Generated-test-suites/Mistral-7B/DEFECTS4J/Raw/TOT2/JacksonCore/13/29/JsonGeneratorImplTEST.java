import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstructor() throws Exception {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testEnableFeature() throws Exception {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImpl._cfgUnqNames == false);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(new IOContext(), 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2, 3 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[] { 1, 2, 3 }, jsonGeneratorImpl._outputEscapes);
    }

    @Test
    public void testWriteStringField() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonWriter writer = jsonFactory.createWriter(System.out);
        JsonGenerator jsonGenerator = writer.getRootValueGenerator();
        jsonGenerator.writeStringField("fieldName", "fieldValue");
        String jsonString = new ObjectMapper().writeValueAsString(jsonGenerator);
        assertEquals("\"fieldName\":\"fieldValue\"", jsonString);
    }

    @Test
    public void testWriteFieldName() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonWriter writer = jsonFactory.createWriter(System.out);
        JsonGenerator jsonGenerator = writer.getRootValueGenerator();
        jsonGenerator.writeFieldName("fieldName");
        String jsonString = new ObjectMapper().writeValueAsString(jsonGenerator);
        assertEquals("\"fieldName\":", jsonString);
    }

    @Test(expected = IOException.class)
    public void testWriteNullFieldValue() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonWriter writer = jsonFactory.createWriter(System.out);
        JsonGenerator jsonGenerator = writer.getRootValueGenerator();
        jsonGenerator.writeStringField("fieldName", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteInvalidFieldName() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonWriter writer = jsonFactory.createWriter(System.out);
        JsonGenerator jsonGenerator = writer.getRootValueGenerator();
        jsonGenerator.writeStringField("", "fieldValue");
    }
}