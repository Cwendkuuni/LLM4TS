import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.Document;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.CDATA;
import org.jdom2.Text;
import org.jdom2.Comment;
import org.jdom2.ProcessingInstruction;
import org.jdom2.EntityRef;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XMLOutputterTest {
    
    @Test
    public void testConstructor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter);
    }
    
    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = outputter.getFormat();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }
    
    @Test
    public void testGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter.getFormat());
    }
    
    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = outputter.getXMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testOutputDocument() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(doc, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputDocType() {
        XMLOutputter outputter = new XMLOutputter();
        DocType doctype = new DocType("html");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(doctype, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputElement() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("element");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(element, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputElementContent() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("element");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.outputElementContent(element, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputList() {
        XMLOutputter outputter = new XMLOutputter();
        List<Content> list = new ArrayList<>();
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(list, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputCDATA() {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new CDATA("data");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(cdata, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputText() {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new Text("text");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(text, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputComment() {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new Comment("comment");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(comment, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputProcessingInstruction() {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(pi, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputEntityRef() {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entity = new EntityRef("ref");
        OutputStream out = new ByteArrayOutputStream();
        try {
            outputter.output(entity, out);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testOutputStringDocument() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();
        String result = outputter.outputString(doc);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringDocType() {
        XMLOutputter outputter = new XMLOutputter();
        DocType doctype = new DocType("html");
        String result = outputter.outputString(doctype);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringElement() {
        XMLOutputter outputter = new XMLOutputter();
        Element element = new Element("element");
        String result = outputter.outputString(element);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringList() {
        XMLOutputter outputter = new XMLOutputter();
        List<Content> list = new ArrayList<>();
        String result = outputter.outputString(list);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringCDATA() {
        XMLOutputter outputter = new XMLOutputter();
        CDATA cdata = new CDATA("data");
        String result = outputter.outputString(cdata);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringText() {
        XMLOutputter outputter = new XMLOutputter();
        Text text = new Text("text");
        String result = outputter.outputString(text);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringComment() {
        XMLOutputter outputter = new XMLOutputter();
        Comment comment = new Comment("comment");
        String result = outputter.outputString(comment);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringProcessingInstruction() {
        XMLOutputter outputter = new XMLOutputter();
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        String result = outputter.outputString(pi);
        assertNotNull(result);
    }
    
    @Test
    public void testOutputStringEntityRef() {
        XMLOutputter outputter = new XMLOutputter();
        EntityRef entity = new EntityRef("ref");
        String result = outputter.outputString(entity);
        assertNotNull(result);
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "attribute";
        String result = outputter.escapeAttributeEntities(str);
        assertNotNull(result);
    }
    
    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "element";
        String result = outputter.escapeElementEntities(str);
        assertNotNull(result);
    }
    
    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clone = outputter.clone();
        assertTrue(outputter != clone);
        assertEquals(outputter.getFormat(), clone.getFormat());
        assertEquals(outputter.getXMLOutputProcessor(), clone.getXMLOutputProcessor());
    }
}