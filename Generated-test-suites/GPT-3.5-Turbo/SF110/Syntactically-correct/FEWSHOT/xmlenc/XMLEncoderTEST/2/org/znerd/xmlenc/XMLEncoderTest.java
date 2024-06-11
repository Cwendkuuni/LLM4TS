package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

    private XMLEncoder encoder;
    
    @Before
    public void setup() {
        encoder = XMLEncoder.getEncoder("UTF-8");
    }
    
    @Test
    public void testGetEncoder() {
        assertNotNull(encoder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithInvalidEncoding() {
        XMLEncoder.getEncoder("invalid encoding");
    }
    
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        try {
            encoder.declaration(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText() {
        StringWriter writer = new StringWriter();
        try {
            encoder.text(writer, "Hello world", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Hello world", writer.toString());
    }
    
    @Test
    public void testTextWithSpecialCharacters() {
        StringWriter writer = new StringWriter();
        try {
            encoder.text(writer, "<>&\"'", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("&lt;&gt;&amp;&quot;&apos;", writer.toString());
    }
    
    @Test
    public void testTextWithInvalidCharacter() {
        StringWriter writer = new StringWriter();
        try {
            encoder.text(writer, '\u0080');
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testWhitespace() {
        StringWriter writer = new StringWriter();
        try {
            encoder.whitespace(writer, "    ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("    ", writer.toString());
    }
    
    @Test
    public void testAttribute() {
        StringWriter writer = new StringWriter();
        try {
            encoder.attribute(writer, "attr", "value", '\"', true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(" attr=\"value\"", writer.toString());
    }
}