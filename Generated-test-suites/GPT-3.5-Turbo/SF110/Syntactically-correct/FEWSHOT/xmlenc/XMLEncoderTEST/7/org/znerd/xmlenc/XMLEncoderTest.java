
package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class XMLEncoderTest {

    private XMLEncoder encoder;
    private Writer writer;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        encoder = XMLEncoder.getEncoder("UTF-8");
        writer = new StringWriter();
    }

    @Test
    public void testGetEncoder() {
        assertNotNull(encoder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXMLEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testXMLEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalid");
    }

    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test(expected = NullPointerException.class)
    public void testDeclaration_NullWriter() throws NullPointerException, IOException {
        encoder.declaration(null);
    }

    // Add more test cases for declaration() method

    @Test(expected = NullPointerException.class)
    public void testText_NullWriter() throws NullPointerException, InvalidXMLException, IOException {
        encoder.text(null, "text", false);
    }

    // Add more test cases for text() method

    @Test(expected = NullPointerException.class)
    public void testWhitespace_NullWriter() throws NullPointerException, InvalidXMLException, IOException {
        encoder.whitespace(null, "text");
    }

    // Add more test cases for whitespace() method

    @Test(expected = NullPointerException.class)
    public void testAttribute_NullWriter() throws NullPointerException, IOException {
        encoder.attribute(null, "name", "value", '\"', false);
    }

    // Add more test cases for attribute() method

}
