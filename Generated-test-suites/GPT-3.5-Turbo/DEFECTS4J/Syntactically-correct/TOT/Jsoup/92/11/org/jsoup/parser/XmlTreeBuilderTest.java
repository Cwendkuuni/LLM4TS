import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testParse_ValidXml_ReturnsDocumentObject() {
        String inputXml = "<root><element1>Text</element1></root>";
        
        Document document = xmlTreeBuilder.parse(inputXml, "");
        
        assertNotNull(document);
        assertEquals(1, document.childNodeSize());
        assertTrue(document.child(0) instanceof Element);
    }
    
    @Test
    public void testParse_EmptyXml_ReturnsEmptyDocumentObject() {
        String inputXml = "";
        
        Document document = xmlTreeBuilder.parse(inputXml, "");
        
        assertNotNull(document);
        assertEquals(0, document.childNodeSize());
    }
    
    @Test
    public void testParse_InvalidXml_ReturnsNull() {
        String inputXml = "<root><element1>Text</element1>";
        
        Document document = xmlTreeBuilder.parse(inputXml, "");
        
        assertNull(document);
    }
    
    @Test
    public void testParseFragment_ValidFragment_ReturnsListOfNodes() {
        String inputXmlFragment = "<element1>Text</element1>";
        
        List<Node> nodeList = xmlFragmentBuilder.parseFragment(inputXmlFragment, "");
        
        assertNotNull(nodeList);
        assertEquals(1, nodeList.size());
        assertTrue(nodeList.get(0) instanceof Element);
    }
    
    @Test
    public void testParseFragment_EmptyFragment_ReturnsEmptyListOfNodes() {
        String inputXmlFragment = "";
        
        List<Node> nodeList = xmlFragmentBuilder.parseFragment(inputXmlFragment, "");
        
        assertNotNull(nodeList);
        assertEquals(0, nodeList.size());
    }
    
    @Test
    public void testParseFragment_InvalidFragment_ReturnsEmptyListOfNodes() {
        String inputXmlFragment = "<element1>Text</element1";
        
        List<Node> nodeList = xmlFragmentBuilder.parseFragment(inputXmlFragment, "");
        
        assertNotNull(nodeList);
        assertEquals(0, nodeList.size());
    }
}