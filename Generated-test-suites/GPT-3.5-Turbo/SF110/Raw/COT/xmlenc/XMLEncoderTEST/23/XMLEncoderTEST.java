import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder xmlEncoder = XMLEncoder.getEncoder("UTF-8");
            assert xmlEncoder != null;
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testConstructor() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            assert xmlEncoder != null;
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetEncoding() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            assert xmlEncoder.getEncoding().equals("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeclaration() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.declaration(writer);
            assert writer.toString().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testText() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.text(writer, "sample text", true);
            assert writer.toString().equals("sample text");
        } catch (IllegalArgumentException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testTextCharArray() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            char[] charArray = "sample text".toCharArray();
            xmlEncoder.text(writer, charArray, 0, charArray.length, true);
            assert writer.toString().equals("sample text");
        } catch (IllegalArgumentException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testTextChar() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.text(writer, 'a');
            assert writer.toString().equals("a");
        } catch (InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testTextCharWithEscape() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.text(writer, '&', true);
            assert writer.toString().equals("&amp;");
        } catch (InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testWhitespace() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.whitespace(writer, "   ");
            assert writer.toString().equals("   ");
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testWhitespaceCharArray() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            char[] charArray = "   ".toCharArray();
            xmlEncoder.whitespace(writer, charArray, 0, charArray.length);
            assert writer.toString().equals("   ");
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAttribute() {
        try {
            XMLEncoder xmlEncoder = new XMLEncoder("UTF-8");
            Writer writer = new StringWriter();
            xmlEncoder.attribute(writer, "name", "value", '\"', true);
            assert writer.toString().equals(" name=\"value\"");
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

}