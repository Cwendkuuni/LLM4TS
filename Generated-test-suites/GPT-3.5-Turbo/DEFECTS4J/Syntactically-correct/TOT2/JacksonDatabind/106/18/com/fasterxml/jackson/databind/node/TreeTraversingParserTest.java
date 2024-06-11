import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        JsonNode node = objectMapper.valueToTree("test");
        parser = new TreeTraversingParser(node, objectMapper);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertEquals(null, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        assertEquals(parser, parser.skipChildren());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(0.0, parser.getDoubleValue(), 0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0, parser.getFloatValue(), 0);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        OutputStream os = new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        };
        assertEquals(0, parser.readBinaryValue(null, os));
    }

    @Test
    public void testHandleEOF() throws JsonParseException {
        try {
            parser._handleEOF();
        } catch (JsonParseException e) {
            assertNotNull(e);
        }
    }
}