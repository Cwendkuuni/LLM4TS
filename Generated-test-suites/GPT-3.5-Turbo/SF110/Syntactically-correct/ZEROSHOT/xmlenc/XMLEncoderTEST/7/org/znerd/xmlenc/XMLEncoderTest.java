
package org.znerd.xmlenc;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            Assert.assertNotNull(encoder);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("UTF-32");
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder(null);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testConstructorWithUnsupportedEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        new XMLEncoder("UTF-32");
    }

    @Test
    public void testGetEncoding() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeclaration() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.declaration(out);
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", out.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithString() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.text(out, "Hello World", false);
            Assert.assertEquals("Hello World", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithCharArray() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.text(out, "Hello World".toCharArray(), 0, 11, false);
            Assert.assertEquals("Hello World", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithChar() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.text(out, 'H');
            Assert.assertEquals("H", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTextWithCharAndEscapeAmpersands() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.text(out, 'H', true);
            Assert.assertEquals("H", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWhitespaceWithString() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(out, "   ");
            Assert.assertEquals("   ", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWhitespaceWithCharArray() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.whitespace(out, "   ".toCharArray(), 0, 3);
            Assert.assertEquals("   ", out.toString());
        } catch (IOException | InvalidXMLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAttribute() {
        XMLEncoder encoder;
        try (StringWriter out = new StringWriter()) {
            encoder = new XMLEncoder("UTF-8");
            encoder.attribute(out, "name", "value", '\"', false);
            Assert.assertEquals(" name=\"value\"", out.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void fail(String message) {
        Assert.fail(message);
    }
}
