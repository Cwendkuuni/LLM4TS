
package org.jdom2.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jdom2.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XMLOutputterTest {
    private XMLOutputter outputter;
    
    @Before
    public void setup() {
        outputter = new XMLOutputter();
    }
    
    @Test
    public void testSetFormat() {
        Format format = new Format();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }
    
    @Test
    public void testGetFormat() {
        Format format = outputter.getFormat();
        assertNull(format);
    }
    
    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new TestXMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testGetXmLOutputProcessor() {
        XMLOutputProcessor processor = outputter.getXMLOutputProcessor();
        assertNull(processor);
    }
    
    @Test
    public void testOutputDocument() {
        Document doc = new Document();
        StringWriter out = new StringWriter();
        try {
            outputter.output(doc, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputDocType() {
        DocType doctype = new DocType();
        StringWriter out = new StringWriter();
        try {
            outputter.output(doctype, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputElement() {
        Element element = new Element("element");
        StringWriter out = new StringWriter();
        try {
            outputter.output(element, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputElementContent() {
        Element element = new Element("element");
        StringWriter out = new StringWriter();
        try {
            outputter.outputElementContent(element, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputList() {
        Element element = new Element("element");
        List<Content> list = new ArrayList<>();
        list.add(element);
        StringWriter out = new StringWriter();
        try {
            outputter.output(list, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputCDATA() {
        CDATA cdata = new CDATA("cdata");
        StringWriter out = new StringWriter();
        try {
            outputter.output(cdata, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputText() {
        Text text = new Text("text");
        StringWriter out = new StringWriter();
        try {
            outputter.output(text, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputComment() {
        Comment comment = new Comment("comment");
        StringWriter out = new StringWriter();
        try {
            outputter.output(comment, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutputProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        StringWriter out = new StringWriter();
        try {
            outputter.output(pi, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", out.toString());
    }
    
    private class TestXMLOutputProcessor implements XMLOutputProcessor {
        @Override
        public void process(Writer out, Format format, Object obj) throws IOException {
            // do nothing
        }
    }
}
