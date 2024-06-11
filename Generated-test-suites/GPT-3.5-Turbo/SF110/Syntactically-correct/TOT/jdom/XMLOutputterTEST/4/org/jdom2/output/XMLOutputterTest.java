import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.support.*;
import java.io.*;
import java.util.*;

public class XMLOutputterTest {
    
    private XMLOutputter xmlOutputter;
    
    @Before
    public void setUp() {
        Format format = new Format();
        XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
        xmlOutputter = new XMLOutputter(format, processor);
    }
    
    @Test
    public void testSetFormat() {
        Format format = new Format();
        xmlOutputter.setFormat(format);
        
        assertEquals(format, xmlOutputter.getFormat());
    }
    
    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }
    
    @Test
    public void testOutputDocument() throws IOException {
        Document doc = new Document(new Element("root"));
        StringWriter out = new StringWriter();
        xmlOutputter.output(doc, out);
        
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputEmptyElement() throws IOException {
        Element element = new Element("element");
        StringWriter out = new StringWriter();
        xmlOutputter.output(element, out);
        
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><element></element>";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("element");
        element.addContent("content");
        
        StringWriter out = new StringWriter();
        xmlOutputter.outputElementContent(element, out);
        
        String expectedOutput = "content";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputList() throws IOException {
        List<Content> list = new ArrayList<>();
        list.add(new Element("element1"));
        list.add(new Element("element2"));
        
        StringWriter out = new StringWriter();
        xmlOutputter.output(list, out);
        
        String expectedOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><element1></element1><element2></element2>";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputCDATA() throws IOException {
        CDATA cdata = new CDATA("cdata");
        StringWriter out = new StringWriter();
        xmlOutputter.output(cdata, out);
        
        String expectedOutput = "<![CDATA[cdata]]>";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputText() throws IOException {
        Text text = new Text("text");
        StringWriter out = new StringWriter();
        xmlOutputter.output(text, out);
        
        String expectedOutput = "text";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputComment() throws IOException {
        Comment comment = new Comment("comment");
        StringWriter out = new StringWriter();
        xmlOutputter.output(comment, out);
        
        String expectedOutput = "<!--comment-->";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testOutputProcessingInstruction() throws IOException {
        ProcessingInstruction pi = new ProcessingInstruction("target", "data");
        StringWriter out = new StringWriter();
        xmlOutputter.output(pi, out);
        
        String expectedOutput = "<?target data?>";
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        String str = "<&\">'";
        String expectedOutput = "&lt;&amp;&quot;&#39;";
        
        assertEquals(expectedOutput, xmlOutputter.escapeAttributeEntities(str));
    }
    
    @Test
    public void testEscapeElementEntities() {
        String str = "<&\">'";
        String expectedOutput = "&lt;&amp;&quot;&#39;";
        
        assertEquals(expectedOutput, xmlOutputter.escapeElementEntities(str));
    }
    
    @Test
    public void testClone() {
        XMLOutputter clonedOutputter = xmlOutputter.clone();
        
        assertNotNull(clonedOutputter);
        assertNotSame(xmlOutputter, clonedOutputter);
    }
    
    @Test
    public void testToString() {
        String expectedOutput = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '', expandEmptyElements = false, lineSeparator = '\n', textMode = PRESERVE]";
        
        assertEquals(expectedOutput, xmlOutputter.toString());
    }
}