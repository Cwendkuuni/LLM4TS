
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;
public class TreeTraversingParserTest {
    
    @Test
    public void testSetCodec() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCodec());
        parser.setCodec(new ObjectCodec());
        assertNotNull(parser.getCodec());
    }
    
    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(Version.class, parser.version().getClass());
    }
    
    @Test
    public void testClose() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
    }
    
    @Test
    public void testSkipChildren() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.skipChildren());
    }
    
    @Test
    public void testisClosed() throws IOException{
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCurrentName());
    }
    
    @Test
    public void testOverrideCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.overrideCurrentName("name");
        assertEquals("name", parser.getCurrentName());
    }
    
    @Test
    public void testGetParsingContext() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNotNull(parser.getParsingContext());
    }
    
    @Test
    public void testgetTokenLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonLocation.class, parser.getTokenLocation().getClass());
    }
    
    @Test
    public void testGetCurrentLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonLocation.class, parser.getCurrentLocation().getClass());
    }
    
    @Test
    public void testGetText() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getText());
    }
    
    @Test
    public void testGetNumberType() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberType());
    }
    
    @Test
    public void testGetBigIntegerValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBigIntegerValue());
    }
    
    @Test
    public void testGetDecimalValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getDecimalValue());
    }
    
    @Test
    public void testGetDoubleValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0, parser.getDoubleValue(), 0);
    }
    
    @Test
    public void testGetFloatValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0f, parser.getFloatValue(), 0);
    }
    
    @Test
    public void testGetIntValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getIntValue());
    }
    
    @Test
    public void testGetLongValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getLongValue());
    }
    
    @Test
    public void testGetNumberValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberValue());
    }
    
    @Test
    public void testGetEmbeddedObject() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getEmbeddedObject());
    }
    
    @Test
    public void testisNaN() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isNaN());
    }
    
    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBinaryValue(new Base64Variant()));
    }
    
    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.readBinaryValue(new Base64Variant(), new OutputStream()));
    }
    
    @Test
    public void test_handleEOF() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        try {
            parser._handleEOF();
        } catch (JsonParseException e) {
            fail("Exception should not be thrown");
        }
    }  
}
