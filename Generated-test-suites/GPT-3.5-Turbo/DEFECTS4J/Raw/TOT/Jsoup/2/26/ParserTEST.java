import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    private Document doc;
    
    @Before
    public void setUp() {
        parser = new Parser("", "", false);
        doc = null;
    }
    
    @Test
    public void testParse_emptyHtml_returnsEmptyDocument() {
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.children().size());
    }
    
    @Test
    public void testParse_singleComment_returnsDocumentWithCommentNode() {
        parser.setTq(new TokenQueue("<!-- Comment -->"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node commentNode = doc.childNode(0);
        Assert.assertTrue(commentNode instanceof Comment);
        Assert.assertEquals(" Comment ", ((Comment) commentNode).getData());
    }
    
    @Test
    public void testParse_singleCdata_returnsDocumentWithCdataNode() {
        parser.setTq(new TokenQueue("<![CDATA[ CDATA ]]>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node cdataNode = doc.childNode(0);
        Assert.assertTrue(cdataNode instanceof TextNode);
        Assert.assertEquals(" CDATA ", ((TextNode) cdataNode).getWholeText());
    }
    
    @Test
    public void testParse_singleXmlDecl_returnsDocumentWithXmlDeclaration() {
        parser.setTq(new TokenQueue("<? Version=\"1.0\" ?>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node xmlDeclNode = doc.childNode(0);
        Assert.assertTrue(xmlDeclNode instanceof XmlDeclaration);
        Assert.assertEquals(" Version=\"1.0\" ", ((XmlDeclaration) xmlDeclNode).getData());
    }
    
    @Test
    public void testParse_singleEndTag_returnsDocumentWithClosedElement() {
        parser.setTq(new TokenQueue("</div>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node divNode = doc.childNode(0);
        Assert.assertTrue(divNode instanceof Element);
        Assert.assertEquals("div", ((Element) divNode).tagName());
        Assert.assertTrue(((Element) divNode).isClosed());
    }
    
    @Test
    public void testParse_singleStartTag_returnsDocumentWithOpenedElement() {
        parser.setTq(new TokenQueue("<div>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node divNode = doc.childNode(0);
        Assert.assertTrue(divNode instanceof Element);
        Assert.assertEquals("div", ((Element) divNode).tagName());
        Assert.assertFalse(((Element) divNode).isClosed());
    }
    
    @Test
    public void testParse_singleStartTagWithAttribute_returnsDocumentWithElementWithAttribute() {
        parser.setTq(new TokenQueue("<div class=\"main\">"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node divNode = doc.childNode(0);
        Assert.assertTrue(divNode instanceof Element);
        Assert.assertEquals("div", ((Element) divNode).tagName());
        Attribute classAttribute = ((Element) divNode).attributes().get("class");
        Assert.assertNotNull(classAttribute);
        Assert.assertEquals("main", classAttribute.getValue());
    }
    
    @Test
    public void testParse_singleStartTagWithEmptyElement_returnsDocumentWithElementIsClosed() {
        parser.setTq(new TokenQueue("<img />"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node imgNode = doc.childNode(0);
        Assert.assertTrue(imgNode instanceof Element);
        Assert.assertEquals("img", ((Element) imgNode).tagName());
        Assert.assertTrue(((Element) imgNode).isClosed());
    }
    
    @Test
    public void testParse_singleStartTagWithDataTag_returnsDocumentWithElementContainingDataNode() {
        parser.setTq(new TokenQueue("<script>function () {console.log('Hello');}</script>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node scriptNode = doc.childNode(0);
        Assert.assertTrue(scriptNode instanceof Element);
        Assert.assertEquals("script", ((Element) scriptNode).tagName());
        Assert.assertFalse(((Element) scriptNode).isClosed());
        Assert.assertEquals(1, scriptNode.childNodeSize());
        Node dataNode = scriptNode.childNode(0);
        Assert.assertTrue(dataNode instanceof DataNode);
        Assert.assertEquals("function () {console.log('Hello');}", ((DataNode) dataNode).getWholeData());
    }
    
    @Test
    public void testParse_singleStartTagWithDataTagAndClosingTag_returnsDocumentWithElementContainingDataNode() {
        parser.setTq(new TokenQueue("<title>Title</title>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node titleNode = doc.childNode(0);
        Assert.assertTrue(titleNode instanceof Element);
        Assert.assertEquals("title", ((Element) titleNode).tagName());
        Assert.assertFalse(((Element) titleNode).isClosed());
        Assert.assertEquals(1, titleNode.childNodeSize());
        Node dataNode = titleNode.childNode(0);
        Assert.assertTrue(dataNode instanceof TextNode);
        Assert.assertEquals("Title", ((TextNode) dataNode).getWholeText());
    }
    
    @Test
    public void testParse_singleStartTagWithBaseHref_returnsDocumentWithUpdatedBaseUri() {
        parser.setTq(new TokenQueue("<base href=\"https://www.example.com\">"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testParse_singleStartTagWithInvalidAncestor_returnsDocumentWithImplicitParentElement() {
        parser.setTq(new TokenQueue("<body>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        Node bodyNode = doc.childNode(0);
        Assert.assertTrue(bodyNode instanceof Element);
        Assert.assertEquals("body", ((Element) bodyNode).tagName());
        Assert.assertFalse(((Element) bodyNode).isClosed());
        Assert.assertEquals(2, bodyNode.childNodeSize());
        Node headNode = bodyNode.childNode(0);
        Assert.assertTrue(headNode instanceof Element);
        Assert.assertEquals("head", ((Element) headNode).tagName());
        Node emulatedChildNode = bodyNode.childNode(1);
        Assert.assertTrue(emulatedChildNode instanceof TextNode);
        Assert.assertEquals("&lt;", ((TextNode) emulatedChildNode).getWholeText());
    }
    
    @Test
    public void testParse_multipleStartAndEndTags_returnsDocumentWithAppropriateElementStructure() {
        parser.setTq(new TokenQueue("<html><head><title>Title</title></head><body><div></div></body></html>"));
        doc = parser.parse();
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.children().size());
        
        Node htmlNode = doc.childNode(0);
        Assert.assertTrue(htmlNode instanceof Element);
        Assert.assertEquals("html", ((Element) htmlNode).tagName());
        Assert.assertFalse(((Element) htmlNode).isClosed());
        Assert.assertEquals(2, htmlNode.childNodeSize());
        
        Node headNode = htmlNode.childNode(0);
        Assert.assertTrue(headNode instanceof Element);
        Assert.assertEquals("head", ((Element) headNode).tagName());
        Assert.assertFalse(((Element) headNode).isClosed());
        Assert.assertEquals(1, headNode.childNodeSize());
        
        Node titleNode = headNode.childNode(0);
        Assert.assertTrue(titleNode instanceof Element);
        Assert.assertEquals("title", ((Element) titleNode).tagName());
        Assert.assertFalse(((Element) titleNode).isClosed());
        Assert.assertEquals(1, titleNode.childNodeSize());
        
        Node titleDataNode = titleNode.childNode(0);
        Assert.assertTrue(titleDataNode instanceof TextNode);
        Assert.assertEquals("Title", ((TextNode) titleDataNode).getWholeText());
        
        Node bodyNode = htmlNode.childNode(1);
        Assert.assertTrue(bodyNode instanceof Element);
        Assert.assertEquals("body", ((Element) bodyNode).tagName());
        Assert.assertFalse(((Element) bodyNode).isClosed());
        Assert.assertEquals(1, bodyNode.childNodeSize());
        
        Node divNode = bodyNode.childNode(0);
        Assert.assertTrue(divNode instanceof Element);
        Assert.assertEquals("div", ((Element) divNode).tagName());
        Assert.assertTrue(((Element) divNode).isClosed());
    }
    
}