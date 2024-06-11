import org.junit.Test;
import org.jdom2.output.*;
import org.jdom2.*;

import static org.junit.Assert.*;

public class XMLOutputterTest {
    
    @Test
    public void testSetFormat() {
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter();
        
        outputter.setFormat(format);
        
        assertEquals(format, outputter.getFormat());
    }
    
    @Test
    public void testGetFormat() {
        Format format = Format.getCompactFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        
        Format retrievedFormat = outputter.getFormat();
        
        assertEquals(format, retrievedFormat);
    }
    
    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(processor);
        
        XMLOutputProcessor retrievedProcessor = outputter.getXMLOutputProcessor();
        
        assertEquals(processor, retrievedProcessor);
    }
    
    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new PrettyXMLProcessor();
        XMLOutputter outputter = new XMLOutputter();
        
        outputter.setXMLOutputProcessor(processor);
        
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testOutputDocument() {
        Document document = new Document();
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(document, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputDocType() {
        DocType docType = new DocType();
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(docType, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputElement() {
        Element element = new Element("test");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(element, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputElementContent() {
        Element element = new Element("test");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.outputElementContent(element, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputList() {
        List<Content> list = new ArrayList<>();
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(list, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputCDATA() {
        CDATA cdata = new CDATA("test");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(cdata, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputText() {
        Text text = new Text("test");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(text, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputComment() {
        Comment comment = new Comment("test");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(comment, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(pi, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputEntityRef() {
        EntityRef entityRef = new EntityRef("entity");
        XMLOutputter outputter = new XMLOutputter();
        
        try {
            outputter.output(entityRef, System.out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testOutputStringDocument() {
        Document document = new Document();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(document);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringDocType() {
        DocType docType = new DocType();
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(docType);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringElement() {
        Element element = new Element("test");
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
        CDATA cdata = new CDATA("test");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(cdata);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringText() {
        Text text = new Text("test");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(text);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringComment() {
        Comment comment = new Comment("test");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(comment);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(pi);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputStringEntityRef() {
        EntityRef entityRef = new EntityRef("entity");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputString(entityRef);
        
        assertNotNull(output);
    }
    
    @Test
    public void testOutputElementContentString() {
        Element element = new Element("test");
        XMLOutputter outputter = new XMLOutputter();
        
        String output = outputter.outputElementContentString(element);
        
        assertNotNull(output);
    }
    
    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        
        XMLOutputter clonedOutputter = outputter.clone();
        
        assertNotSame(outputter, clonedOutputter);
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        String str = "<>&\"'";
        XMLOutputter outputter = new XMLOutputter();
        
        String escapedString = outputter.escapeAttributeEntities(str);
        
        assertEquals("&lt;&gt;&amp;&quot;&apos;", escapedString);
    }
    
    @Test
    public void testEscapeElementEntities() {
        String str = "<>&\"'";
        XMLOutputter outputter = new XMLOutputter();
        
        String escapedString = outputter.escapeElementEntities(str);
        
        assertEquals("&lt;&gt;&amp;\"'", escapedString);
    }
  
}