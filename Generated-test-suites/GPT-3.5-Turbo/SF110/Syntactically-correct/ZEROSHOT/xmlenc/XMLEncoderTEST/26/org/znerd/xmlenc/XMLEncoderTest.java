
package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            Assert.assertNotNull(encoder);
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("InvalidEncoding");
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertNotNull(encoder);
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructor_InvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("InvalidEncoding");
    }

    @Test
    public void testDeclaration() {
        StringWriter writer = new StringWriter();
        XMLEncoder encoder = new XMLEncoder("UTF-8");

        try {
            encoder.declaration(writer);
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // Add test methods for other public methods in XMLEncoder class

}

