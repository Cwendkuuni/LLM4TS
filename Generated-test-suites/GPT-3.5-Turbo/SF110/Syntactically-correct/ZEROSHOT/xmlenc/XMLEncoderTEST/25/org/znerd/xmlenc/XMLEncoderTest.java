package org.znerd.xmlenc;

import org.junit.Test;
import org.junit.Assert;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;

public class XMLEncoderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoderNullEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoderInvalidEncoding() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder.getEncoder("invalidEncoding");
    }
    
    @Test
    public void testGetEncoderUTF8() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        Assert.assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test
    public void testGetEncoderASCII() throws IllegalArgumentException, UnsupportedEncodingException {
        XMLEncoder encoder = XMLEncoder.getEncoder("US-ASCII");
        Assert.assertEquals("US-ASCII", encoder.getEncoding());
    }
    
    // More tests for other methods in the XMLEncoder class

}