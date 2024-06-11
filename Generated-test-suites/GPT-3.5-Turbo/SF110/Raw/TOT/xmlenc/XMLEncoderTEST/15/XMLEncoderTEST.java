import org.junit.Before;
import org.junit.Test;
import org.znerd.xmlenc.InvalidXMLException;
import org.znerd.xmlenc.UnsupportedEncodingException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class XMLEncoderTest {
    
    private XMLEncoder xmlEncoder;
    
    @Before
    public void setup() throws UnsupportedEncodingException {
        xmlEncoder = new XMLEncoder("UTF-8");
    }
    
    @Test
    public void testGetEncoder() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("ISO-8859-15");
    }
    
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }
    
    @Test(expected = NullPointerException.class)
    public void testDeclarationWithNullWriter() throws NullPointerException, IOException {
        xmlEncoder.declaration(null);
    }
    
    @Test
    public void testDeclaration() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testTextWithStringNullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.text(null, "example", true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testTextWithCharArrayNullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.text(null, "example".toCharArray(), 0, 7, true);
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, (char) 128, true);
    }
    
    @Test(expected = IOException.class)
    public void testTextWithIOException() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {}
            
            @Override
            public void close() throws IOException {}
        };
        xmlEncoder.text(writer, "example", true);
    }
    
    @Test
    public void testTextWithString() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, "example", true);
        assertEquals("example", writer.toString());
    }
    
    @Test
    public void testTextWithCharArray() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.text(writer, "example".toCharArray(), 0, 7, true);
        assertEquals("example", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithStringNullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(null, "example");
    }
    
    @Test(expected = NullPointerException.class)
    public void testWhitespaceWithCharArrayNullWriter() throws NullPointerException, InvalidXMLException, IOException {
        xmlEncoder.whitespace(null, "example".toCharArray(), 0, 7);
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testWhitespaceWithInvalidCharacter() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.whitespace(writer, (char) 128);
    }
    
    @Test(expected = IOException.class)
    public void testWhitespaceWithIOException() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {}
            
            @Override
            public void close() throws IOException {}
        };
        xmlEncoder.whitespace(writer, "example");
    }
    
    @Test
    public void testWhitespaceWithString() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.whitespace(writer, "example");
        assertEquals("example", writer.toString());
    }
    
    @Test
    public void testWhitespaceWithCharArray() throws NullPointerException, InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.whitespace(writer, "example".toCharArray(), 0, 7);
        assertEquals("example", writer.toString());
    }
    
    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullWriter() throws NullPointerException, IOException {
        xmlEncoder.attribute(null, "name", "value", '\"', true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAttributeWithNullName() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, null, "value", '\"', true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAttributeWithInvalidQuotationMark() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '/', true);
    }
    
    @Test(expected = IOException.class)
    public void testAttributeWithIOException() throws NullPointerException, IOException {
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {}
            
            @Override
            public void close() throws IOException {}
        };
        xmlEncoder.attribute(writer, "name", "value", '\"', true);
    }
    
    @Test
    public void testAttribute() throws NullPointerException, IOException {
        StringWriter writer = new StringWriter();
        xmlEncoder.attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }
}