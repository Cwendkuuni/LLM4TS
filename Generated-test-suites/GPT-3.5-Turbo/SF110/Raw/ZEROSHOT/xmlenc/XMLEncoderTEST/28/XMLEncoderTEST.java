package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.io.UnsupportedEncodingException;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            Assert.assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testConstructorWithValidEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws Exception {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithUnsupportedEncoding() throws Exception {
        new XMLEncoder("invalidEncoding");
    }

    @Test
    public void testDeclaration() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.declaration(writer);
            String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "Text with & character", true);
            String expected = "Text with &amp; character";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithoutEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.text(writer, "Text with & character", false);
            String expected = "Text with & character";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testWhitespace() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(writer, "Whitespace Test");
            String expected = "Whitespace Test";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | InvalidXMLException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAttributeWithEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "value with & character", '"', true);
            String expected = " name=\"value with &amp; character\"";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testAttributeWithoutEscapeAmpersands() {
        try {
            StringWriter writer = new StringWriter();
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            encoder.attribute(writer, "name", "value with & character", '"', false);
            String expected = " name=\"value with & character\"";
            Assert.assertEquals(expected, writer.toString());
        } catch (NullPointerException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}