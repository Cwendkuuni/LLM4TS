import org.junit.Test;
import org.znerd.xmlenc.*;

import java.io.*;

public class XMLEncoderTest {
    
    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testGetEncoderWithNullEncoding() {
        try {
            XMLEncoder.getEncoder(null);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testGetEncoderWithUnsupportedEncoding() {
        try {
            XMLEncoder.getEncoder("UTF-32");
            assert false;
        } catch (IllegalArgumentException e) {
            assert false;
        } catch (UnsupportedEncodingException e) {
            assert true;
        }
    }
    
    @Test
    public void testGetEncoding() {
        String encoding = "UTF-8";
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
            String result = encoder.getEncoding();
            assert result.equals(encoding);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            String result = writer.toString();
            assert result.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        } catch (NullPointerException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testTextWithEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "This is a test & example", true);
            String result = writer.toString();
            assert result.equals("This is a test &amp; example");
        } catch (NullPointerException | InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testTextWithoutEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "This is a test & example", false);
            String result = writer.toString();
            assert result.equals("This is a test & example");
        } catch (NullPointerException | InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testTextCharArray() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, "Test".toCharArray(), 0, 4, true);
            String result = writer.toString();
            assert result.equals("Test");
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testTextSingleCharWithEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, 'A', true);
            String result = writer.toString();
            assert result.equals("A");
        } catch (InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testTextSingleCharWithoutEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.text(writer, 'A', false);
            String result = writer.toString();
            assert result.equals("A");
        } catch (InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testWhitespaceWithString() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.whitespace(writer, "   ");
            String result = writer.toString();
            assert result.equals("   ");
        } catch (NullPointerException | InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testWhitespaceWithCharArray() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.whitespace(writer, "   ".toCharArray(), 0, 3);
            String result = writer.toString();
            assert result.equals("   ");
        } catch (NullPointerException | IndexOutOfBoundsException | InvalidXMLException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testAttributeWithEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "attribute", "This is a test & example", '"', true);
            String result = writer.toString();
            assert result.equals(" attribute=\"This is a test &amp; example\"");
        } catch (NullPointerException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }
    
    @Test
    public void testAttributeWithoutEscapeAmpersands() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "attribute", "This is a test & example", '"', false);
            String result = writer.toString();
            assert result.equals(" attribute=\"This is a test & example\"");
        } catch (NullPointerException | IOException | IllegalArgumentException | UnsupportedEncodingException e) {
            assert false;
        }
    }

    @Test
    public void testAttributeWithInvalidQuotationMark() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.attribute(writer, "attribute", "This is a test", '`', false);
            assert false;
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            assert true;
        }
    }
    
}