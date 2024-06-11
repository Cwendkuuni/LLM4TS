
package org.znerd.xmlenc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class XMLEncoderTest {
    
    @Test
    public void testDeclaration() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").declaration(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }
    
    @Test
    public void testText() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").text(writer, "Hello World", true);
        assertEquals("Hello World", writer.toString());
    }
    
    @Test
    public void testTextWithEscaping() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").text(writer, "<Hello> &amp; World", true);
        assertEquals("&lt;Hello&gt; &amp; World", writer.toString());
    }
    
    @Test
    public void testTextWithNoEscaping() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").text(writer, "<Hello> &amp; World", false);
        assertEquals("<Hello> &amp; World", writer.toString());
    }
    
    @Test
    public void testWhitespace() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").whitespace(writer, "   ");
        assertEquals("   ", writer.toString());
    }
    
    @Test
    public void testAttribute() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").attribute(writer, "name", "value", '\"', true);
        assertEquals(" name=\"value\"", writer.toString());
    }
    
    @Test
    public void testAttributeWithEscaping() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").attribute(writer, "name", "<value> & World", '\"', true);
        assertEquals(" name=\"&lt;value&gt; &amp; World\"", writer.toString());
    }
    
    @Test
    public void testAttributeWithNoEscaping() throws IOException {
        StringWriter writer = new StringWriter();
        XMLEncoder.getEncoder("UTF-8").attribute(writer, "name", "<value> & World", '\"', false);
        assertEquals(" name=\"<value> & World\"", writer.toString());
    }
    
}
