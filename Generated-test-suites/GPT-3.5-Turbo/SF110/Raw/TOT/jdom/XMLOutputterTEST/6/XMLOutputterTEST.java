import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.*;

public class XMLOutputterTest {

    @Test
    public void testSetAndGetFormat() {
        Format format = Format.getPrettyFormat();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        xmlOutputter.setFormat(format);
        
        Assert.assertEquals(format, xmlOutputter.getFormat());
    }
    
    @Test
    public void testSetAndGetXMLOutputProcessor() {
        DefaultXMLProcessor processor = new DefaultXMLProcessor();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        xmlOutputter.setXMLOutputProcessor(processor);
        
        Assert.assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testOutputDocumentToOutputStream() {
        Document doc = new Document();
        doc.setRootElement(new Element("root"));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(doc, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<root></root>"));
    }
    
    @Test
    public void testOutputDocTypeToOutputStream() {
        DocType doctype = new DocType("root", "publicId", "systemId");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(doctype, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<!DOCTYPE root PUBLIC \"publicId\" \"systemId\">"));
    }
    
    @Test
    public void testOutputElementToOutputStream() {
        Element element = new Element("root");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(element, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<root/>"));
    }
    
    @Test
    public void testOutputElementContentToOutputStream() {
        Element element = new Element("root");
        element.addContent("content");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.outputElementContent(element, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("content"));
    }
    
    @Test
    public void testOutputListToOutputStream() {
        Element element1 = new Element("root1");
        Element element2 = new Element("root2");
        element1.addContent(element2);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(element1.getContent(), out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<root1><root2/></root1>"));
    }
    
    @Test
    public void testOutputCDATAStringToOutputStream() {
        CDATA cdata = new CDATA("cdata");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(cdata, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<![CDATA[cdata]]>"));
    }
    
    @Test
    public void testOutputTextStringToOutputStream() {
        Text text = new Text("text");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(text, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("text"));
    }
    
    @Test
    public void testOutputCommentToOutputStream() {
        Comment comment = new Comment("comment");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(comment, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<!--comment-->"));
    }
    
    @Test
    public void testOutputProcessingInstructionToOutputStream() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(pi, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("<?target data?>"));
    }
    
    @Test
    public void testOutputEntityRefToOutputStream() {
        EntityRef entity = new EntityRef("entity");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        try {
            xmlOutputter.output(entity, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        
        String output = out.toString();
        
        Assert.assertTrue(output.contains("&entity;"));
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        String escaped = xmlOutputter.escapeAttributeEntities("<&>");
        
        Assert.assertEquals("&lt;&amp;&gt;", escaped);
    }
    
    @Test
    public void testEscapeElementEntities() {
        XMLOutputter xmlOutputter = new XMLOutputter();
        
        String escaped = xmlOutputter.escapeElementEntities("<&>");
        
        Assert.assertEquals("&lt;&amp;&gt;", escaped);
    }
    
}