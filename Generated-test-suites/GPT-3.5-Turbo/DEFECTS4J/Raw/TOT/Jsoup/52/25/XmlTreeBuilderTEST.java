import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private Tokenizer tokenizer;
    private Token token;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
        tokenizer = new Tokenizer("");
        token = new Token();
    }
    
    @Test
    public void initialiseParseTest() {
        xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void processStartTagTest() {
        Element el = xmlTreeBuilder.insert(token.new StartTag("tag", null));
        assertNotNull(el);
        assertEquals(Tag.valueOf("tag"), el.tag());

        token = token.new StartTag("selfClosingTag", null);
        Element selfClosingTagEl = xmlTreeBuilder.insert(token);
        assertTrue(selfClosingTagEl.tag().isSelfClosing());
        assertTrue(selfClosingTagEl.tag().isKnownTag());
        assertTrue(tokenizer.selfClosingFlagAcknowledged);

        token = token.new StartTag("unknownTag", null);
        Element unknownTagEl = xmlTreeBuilder.insert(token);
        assertTrue(unknownTagEl.tag().isSelfClosing());

        assertTrue(tokenizer.selfClosingFlagAcknowledged);
        assertFalse(unknownTagEl.tag().isKnownTag());
    }
    
    @Test
    public void processEndTagTest() {
        Element el = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.add(el);

        token = token.new EndTag("tag");
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void processCommentTest() {
        Comment commentToken = token.new Comment("comment", "");

        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Comment);
        Comment comment = (Comment) doc.childNode(0);
        assertEquals("comment", comment.getData());

        // Test XML Declaration
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        commentToken = token.new Comment(xmlDeclaration, "");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof XmlDeclaration);
        XmlDeclaration declaration = (XmlDeclaration) doc.childNode(0);
        assertEquals(xmlDeclaration, declaration.toString());
        assertTrue(declaration.isProcessingInstruction());

        // Test Bogus Comment
        commentToken = token.new Comment("<!DOCTYPE html>", "");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof XmlDeclaration);
        declaration = (XmlDeclaration) doc.childNode(0);
        assertEquals("DOCTYPE html>", declaration.getDeclaration());
    }
    
    @Test
    public void processCharacterTest() {
        Token.Character characterToken = token.new Character("data");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) doc.childNode(0);
        assertEquals("data", textNode.getWholeText());
    }
    
    @Test
    public void processDoctypeTest() {
        Token.Doctype doctype = token.new Doctype("name", "", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) doc.childNode(0);
        assertEquals("name", documentType.getName());
    }
    
    @Test
    public void parseFragmentTest() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag>text</tag>", "", new ParseErrorList());
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("text", element.text());
    }
}