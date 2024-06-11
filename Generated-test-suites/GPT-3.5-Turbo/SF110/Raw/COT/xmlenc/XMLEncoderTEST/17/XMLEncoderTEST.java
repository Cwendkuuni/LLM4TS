import org.junit.Test;
import java.io.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder.getEncoder("UTF-8");
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testConstructor() {
        try {
            String encoding = "UTF-8";
            XMLEncoder encoder = new XMLEncoder(encoding);
            assertEquals(encoding, encoder.getEncoding());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("InvalidEncoding");
    }

    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for declaration() can be added to check for edge cases

    @Test
    public void testTextWithoutAmpersandEscape() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "This is a test text", false);
            assertEquals("This is a test text", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for text() can be added to check for different inputs

    @Test
    public void testTextWithAmpersandEscape() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "<This is a test & text>", true);
            assertEquals("&lt;This is a test &amp; text&gt;", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for text() can be added to check for different inputs

    @Test
    public void testTextWithChar() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, 'A');
            assertEquals("A", writer.toString());
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for text() can be added to check for different characters

    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidChar() throws InvalidXMLException, IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        encoder.text(writer, (char) 128);
    }

    @Test
    public void testWhitespaceWithString() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, "  ");
            assertEquals("  ", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for whitespace() can be added to check for different inputs

    @Test
    public void testWhitespaceWithCharArray() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, new char[] {' ', ' '}, 0, 2);
            assertEquals("  ", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for whitespace() can be added to check for different inputs

    @Test
    public void testAttributeWithoutAmpersandEscape() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "value", '\"', false);
            assertEquals(" name=\"value\"", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for attribute() can be added to check for different inputs

    @Test
    public void testAttributeWithAmpersandEscape() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "<value&>", '\"', true);
            assertEquals(" name=\"&lt;value&amp;&gt;\"", writer.toString());
        } catch (NullPointerException e) {
            fail("NullPointerException should not be thrown");
        } catch (InvalidXMLException e) {
            fail("InvalidXMLException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Additional test cases for attribute() can be added to check for different inputs

}