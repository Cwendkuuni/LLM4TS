
package org.znerd.xmlenc;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetEncoderInvalidEncoding() {
        try {
            XMLEncoder.getEncoder("INVALID_ENCODING");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid encoding: INVALID_ENCODING", e.getLocalizedMessage());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetEncoderUnsupportedEncodingException() {
        try {
            XMLEncoder.getEncoder("UTF-16");
            fail("Expected UnsupportedEncodingException to be thrown");
        } catch (UnsupportedEncodingException e) {
            assertEquals("Unsupported encoding: UTF-16", e.getLocalizedMessage());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            assertNotNull(encoder);
            assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorNullEncoding() {
        try {
            new XMLEncoder(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("encoding == null", e.getLocalizedMessage());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeclaration() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            StringWriter writer = new StringWriter();
            encoder.declaration(writer);
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeclarationIOException() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Writer writer = new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException("IO exception");
                }
                @Override
                public void flush() throws IOException {}
                @Override
                public void close() throws IOException {}
            };
            encoder.declaration(writer);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("IO exception", e.getLocalizedMessage());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    // Rest of the test cases...


}

