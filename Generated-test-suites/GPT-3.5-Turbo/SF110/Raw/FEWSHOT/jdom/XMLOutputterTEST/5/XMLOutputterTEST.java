
package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XMLOutputterTest {
    
    @Test
    public void testSetAndGetFormat() {
        Format format = new Format();
        XMLOutputter outputter = new XMLOutputter();
        
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }
    
    @Test
    public void testSetAndGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new XMLOutputProcessor();
        XMLOutputter outputter = new XMLOutputter();
        
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testOutputDocument() {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(doc, out));
    }
    
    @Test
    public void testOutputDocType() {
        DocType doctype = new DocType();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(doctype, out));
    }
    
    @Test
    public void testOutputElement() {
        Element element = new Element();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(element, out));
    }
    
    @Test
    public void testOutputElementContent() {
        Element element = new Element();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.outputElementContent(element, out));
    }
    
    @Test
    public void testOutputList() {
        List<Content> list = new ArrayList<>();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(list, out));
    }
    
    @Test
    public void testOutputCDATA() {
        CDATA cdata = new CDATA();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(cdata, out));
    }
    
    @Test
    public void testOutputText() {
        Text text = new Text();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(text, out));
    }
    
    @Test
    public void testOutputComment() {
        Comment comment = new Comment();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(comment, out));
    }
    
    @Test
    public void testOutputProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(pi, out));
    }
    
    @Test
    public void testOutputEntityRef() {
        EntityRef entity = new EntityRef();
        XMLOutputter outputter = new XMLOutputter();
        
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(entity, out));
    }
    
    @Test
    public void testOutputStringDocument() {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(doc);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringDocType() {
        DocType doctype = new DocType();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(doctype);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringElement() {
        Element element = new Element();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(element);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringList() {
        List<Content> list = new ArrayList<>();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(list);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringCDATA() {
        CDATA cdata = new CDATA();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(cdata);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringText() {
        Text text = new Text();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(text);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringComment() {
        Comment comment = new Comment();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(comment);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(pi);
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringEntityRef() {
        EntityRef entity = new EntityRef();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(entity);
        assertNotNull(output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "This is a test";
        XMLOutputter outputter = new XMLOutputter();

        String escapedStr = outputter.escapeAttributeEntities(str);
        assertNotNull(escapedStr);
    }
    
    @Test
    public void testEscapeElementEntities() {
        String str = "This is a test";
        XMLOutputter outputter = new XMLOutputter();

        String escapedStr = outputter.escapeElementEntities(str);
        assertNotNull(escapedStr);
    }
    
    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clonedOutputter = outputter.clone();
        
        assertNotNull(clonedOutputter);
    }
    
    @Test
    public void testToString() {
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.toString();
        assertNotNull(output);
    }
    
}
