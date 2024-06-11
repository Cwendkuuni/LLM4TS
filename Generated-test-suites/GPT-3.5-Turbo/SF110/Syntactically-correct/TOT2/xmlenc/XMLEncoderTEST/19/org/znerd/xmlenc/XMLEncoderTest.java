package org.znerd.xmlenc;
import org.junit.Test;
import org.junit.Assert;
import java.io.StringWriter;
import java.io.Writer;
import java.io.UnsupportedEncodingException;
import org.znerd.xmlenc.*;
import java.io.IOException;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() {
        try {
            XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
            Assert.assertNotNull(encoder);
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testConstructor() {
        try {
            XMLEncoder encoder = new XMLEncoder("UTF-8");
            Assert.assertEquals("UTF-8", encoder.getEncoding());
        } catch (Exception e) {
            Assert.fail("Exception thrown");
        }
    }

    // Add test cases for other methods here

}