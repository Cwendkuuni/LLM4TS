
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
            XMLEncoder.getEncoder("US-ASCII");
            XMLEncoder.getEncoder("ISO-8859-1");
            // Test for IllegalArgumentException when encoding is null
            assertThrows(IllegalArgumentException.class, () -> XMLEncoder.getEncoder(null));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test
    public void testXMLEncoderConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertEquals("UTF-8", encoder.getEncoding());
            // Test for IllegalArgumentException when encoding is null
            assertThrows(IllegalArgumentException.class, () -> new XMLEncoder(null));
            // Test for UnsupportedEncodingException when encoding is not supported
            assertThrows(UnsupportedEncodingException.class, () -> new XMLEncoder("UTF-9"));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            
            String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            assertEquals(expectedDeclaration, writer.toString());
            
            // Test for NullPointerException when out is null
            assertThrows(NullPointerException.class, () -> encoder.declaration(null));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test
    public void testText() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "This is a test", false);
            
            String expectedText = "This is a test";
            assertEquals(expectedText, writer.toString());
            
            StringWriter writer2 = new StringWriter();
            encoder.text(writer2, "This is a test &", true);
            String expectedText2 = "This is a test &amp;";
            assertEquals(expectedText2, writer2.toString());
            
            // Test for NullPointerException when out is null
            assertThrows(NullPointerException.class, () -> encoder.text(null, "This is a test", false));
            
            // Test for InvalidXMLException when the character is not valid
            StringWriter writer3 = new StringWriter();
            assertThrows(InvalidXMLException.class, () -> encoder.text(writer3, '€'));
            
            // Test for IOException
            Writer out = new StringWriter() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException();
                }
            };
            assertThrows(IOException.class, () -> encoder.text(out, "This is a test", false));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test
    public void testWhitespace() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, "  ");
            
            String expectedWhitespace = "  ";
            assertEquals(expectedWhitespace, writer.toString());
            
            // Test for NullPointerException when out is null
            assertThrows(NullPointerException.class, () -> encoder.whitespace(null, "  "));
            
            // Test for InvalidXMLException when the whitespace is not valid
            StringWriter writer2 = new StringWriter();
            assertThrows(InvalidXMLException.class, () -> encoder.whitespace(writer2, " "));

            // Test for IOException
            Writer out = new StringWriter() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException();
                }
            };
            assertThrows(IOException.class, () -> encoder.whitespace(out, "  "));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
    
    @Test
    public void testAttribute() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "value", '\"', false);
            
            String expectedAttribute = " name=\"value\"";
            assertEquals(expectedAttribute, writer.toString());
            
            StringWriter writer2 = new StringWriter();
            encoder.attribute(writer2, "name", "value&", '\"', true);
            String expectedAttribute2 = " name=\"value&amp;\"";
            assertEquals(expectedAttribute2, writer2.toString());
            
            // Test for NullPointerException when out is null
            assertThrows(NullPointerException.class, () -> encoder.attribute(null, "name", "value", '\"', false));
            
            // Test for IllegalArgumentException when quotation mark is not valid
            StringWriter writer3 = new StringWriter();
            assertThrows(IllegalArgumentException.class, () -> encoder.attribute(writer3, "name", "value", 'x', false));
            
            // Test for IOException
            Writer out = new StringWriter() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException();
                }
            };
            assertThrows(IOException.class, () -> encoder.attribute(out, "name", "value", '\"', false));
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
}
