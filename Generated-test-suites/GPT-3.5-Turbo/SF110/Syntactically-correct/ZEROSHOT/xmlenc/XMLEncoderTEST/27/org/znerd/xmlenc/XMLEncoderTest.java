import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetEncoder_nullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected=UnsupportedEncodingException.class)
    public void testGetEncoder_invalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalidEncoding");
    }
    
    @Test
    public void testGetEncoder_supportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        assertNotNull(encoder);
        assertEquals("US-ASCII", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_oneCharacter() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, 'A');
        String expected = "A";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_multipleCharacters_escapeAmpersandsFalse() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "Hello World!", false);
        String expected = "Hello World!";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_multipleCharacters_escapeAmpersandsTrue() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.text(writer, "Hello & World!", true);
        String expected = "Hello &amp; World!";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testWhitespace() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.whitespace(writer, "   ");
        String expected = "   ";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testAttribute() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        encoder.attribute(writer, "name", "value", '\"', false);
        String expected = " name=\"value\"";
        assertEquals(expected, writer.toString());
    }
}