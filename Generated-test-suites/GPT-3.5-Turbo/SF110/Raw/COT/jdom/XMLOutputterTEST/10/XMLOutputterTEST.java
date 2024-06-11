
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.FormatStack;

public class XMLOutputterTest {
    private XMLOutputter xmlOutputter;
    private Format format;
    
    @Before
    public void setup() {
        format = Format.getRawFormat();
        xmlOutputter = new XMLOutputter(format);
    }
    
    @Test
    public void testSetFormat() {
        Format newFormat = format.clone();
        newFormat.setIndent("    ");
        xmlOutputter.setFormat(newFormat);
        
        assertEquals(newFormat, xmlOutputter.getFormat());
    }
    
    @Test
    public void testGetFormat() {
        assertEquals(format, xmlOutputter.getFormat());
    }
    
    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testXMLOutputProcessor() {
        XMLOutputProcessor processor = xmlOutputter.getXMLOutputProcessor();
        assertNotNull(processor);
    }
    
    @Test
    public void testOutput_Document() throws IOException {
        Document doc = new Document();
        StringWriter out = new StringWriter();
        xmlOutputter.output(doc, out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutput_DocType() throws IOException {
        DocType doctype = new DocType("root");
        StringWriter out = new StringWriter();
        xmlOutputter.output(doctype, out);
        
        assertEquals("<!DOCTYPE root>", out.toString());
    }
    
    @Test
    public void testOutput_Element() throws IOException {
        Element element = new Element("root");
        StringWriter out = new StringWriter();
        xmlOutputter.output(element, out);
        
        assertEquals("<root/>", out.toString());
    }
    
    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("root");
        StringWriter out = new StringWriter();
        xmlOutputter.outputElementContent(element, out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutput_List() throws IOException {
        List<Content> list = new ArrayList<>();
        StringWriter out = new StringWriter();
        xmlOutputter.output(list, out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testOutput_CDATA() throws IOException {
        CDATA cdata = new CDATA("data");
        StringWriter out = new StringWriter();
        xmlOutputter.output(cdata, out);
        
        assertEquals("<![CDATA[data]]>", out.toString());
    }
    
    @Test
    public void testOutput_Text() throws IOException {
        Text text = new Text("text");
        StringWriter out = new StringWriter();
        xmlOutputter.output(text, out);
        
        assertEquals("text", out.toString());
    }
    
    @Test
    public void testOutput_Comment() throws IOException {
        Comment comment = new Comment("comment");
        StringWriter out = new StringWriter();
        xmlOutputter.output(comment, out);
        
        assertEquals("<!--comment-->", out.toString());
    }
    
    @Test
    public void testOutput_ProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        StringWriter out = new StringWriter();
        xmlOutputter.output(pi, out);
        
        assertEquals("<?target data?>", out.toString());
    }
    
    @Test
    public void testOutput_EntityRef() throws IOException {
        EntityRef entity = new EntityRef("entity");
        StringWriter out = new StringWriter();
        xmlOutputter.output(entity, out);
        
        assertEquals("&entity;", out.toString());
    }
    
    @Test
    public void testOutputString_Document() {
        Document doc = new Document();
        String output = xmlOutputter.outputString(doc);
        
        assertEquals("", output);
    }
    
    @Test
    public void testOutputString_DocType() {
        DocType doctype = new DocType("root");
        String output = xmlOutputter.outputString(doctype);
        
        assertEquals("<!DOCTYPE root>", output);
    }
    
    @Test
    public void testOutputString_Element() {
        Element element = new Element("root");
        String output = xmlOutputter.outputString(element);
        
        assertEquals("<root/>", output);
    }
    
    @Test
    public void testOutputString_List() {
        List<Content> list = new ArrayList<>();
        String output = xmlOutputter.outputString(list);
        
        assertEquals("", output);
    }
    
    @Test
    public void testOutputString_CDATA() {
        CDATA cdata = new CDATA("data");
        String output = xmlOutputter.outputString(cdata);
        
        assertEquals("<![CDATA[data]]>", output);
    }
    
    @Test
    public void testOutputString_Text() {
        Text text = new Text("text");
        String output = xmlOutputter.outputString(text);
        
        assertEquals("text", output);
    }
    
    @Test
    public void testOutputString_Comment() {
        Comment comment = new Comment("comment");
        String output = xmlOutputter.outputString(comment);
        
        assertEquals("<!--comment-->", output);
    }
    
    @Test
    public void testOutputString_ProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        String output = xmlOutputter.outputString(pi);
        
        assertEquals("<?target data?>", output);
    }
    
    @Test
    public void testOutputString_EntityRef() {
        EntityRef entity = new EntityRef("entity");
        String output = xmlOutputter.outputString(entity);
        
        assertEquals("&entity;", output);
    }
    
    @Test
    public void testOutputString_ElementContent() {
        Element element = new Element("root");
        String output = xmlOutputter.outputElementContentString(element);
        
        assertEquals("", output);
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        String str = "\"attribute\"";
        String escapedStr = xmlOutputter.escapeAttributeEntities(str);
        
        assertEquals("&quot;attribute&quot;", escapedStr);
    }
    
    @Test
    public void testEscapeElementEntities() {
        String str = "<element>";
        String escapedStr = xmlOutputter.escapeElementEntities(str);
        
        assertEquals("&lt;element&gt;", escapedStr);
    }
    
    @Test
    public void testClone() {
        XMLOutputter clonedOutputter = xmlOutputter.clone();
        
        assertNotSame(clonedOutputter, xmlOutputter);
        assertEquals(clonedOutputter.getFormat(), xmlOutputter.getFormat());
    }
    
    @Test
    public void testToString() {
        String expected = "XMLOutputter[omitDeclaration = " + format.omitDeclaration 
                            + ", encoding = " + format.encoding 
                            + ", omitEncoding = " + format.omitEncoding 
                            + ", indent = '    '"
                            + ", expandEmptyElements = " + format.expandEmptyElements 
                            + ", lineSeparator = '\\n'"
                            + ", textMode = " + format.mode + "]";
        String actual = xmlOutputter.toString();
        
        assertEquals(expected, actual);
    }
}
