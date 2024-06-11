package org.jdom2.output;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class XMLOutputterTest {

    @Test
    public void testSetFormatAndGetFormat() {
        Format format = Format.getRawFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        
        outputter.setFormat(Format.getPrettyFormat());
        
        assertEquals(Format.getPrettyFormat(), outputter.getFormat());
    }
    
    @Test
    public void testOutputDocument() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document(new Element("root"));
        StringWriter writer = new StringWriter();
        
        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        
        assertTrue(writer.toString().contains("<root></root>"));
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        Format format = Format.getRawFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        
        assertEquals("&lt;&gt;&amp;&quot;&apos;", outputter.escapeAttributeEntities("<>&\"'"));
    }

    // Add more test cases for other methods as needed

}