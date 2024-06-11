import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    Document xmlDoc;
    XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void initialiseParse_validInput_correctStackAndSettings() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        List<Element> stack = xmlTreeBuilder.getStack();
        Document doc = xmlTreeBuilder.getDoc();
        
        assertEquals(1, stack.size());
        assertEquals("root", stack.get(0).nodeName());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void process_startTag_insertsElement() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.StartTag startTag = new Token.StartTag("child3", "attr1=value1");
        xmlTreeBuilder.process(startTag);
        
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals("child3", childNodes.get(0).nodeName());
    }

    @Test
    public void process_endTag_removesElementFromStack() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.EndTag endTag = new Token.EndTag("child1");
        xmlTreeBuilder.process(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();

        assertEquals(1, stack.size());
        assertEquals("root", stack.get(0).nodeName());
    }

    @Test
    public void process_comment_insertsCommentNode() {
        String input = "<root><!-- This is a comment --></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Comment comment = new Token.Comment("This is a comment", baseUri);
        xmlTreeBuilder.process(comment);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.COMMENT_NODE, childNodes.get(0).nodeType());
        assertEquals("This is a comment", childNodes.get(0).outerHtml());
    }

    @Test
    public void process_character_insertsTextNode() {
        String input = "<root>Some text</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Character character = new Token.Character("Some text");
        xmlTreeBuilder.process(character);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.TEXT_NODE, childNodes.get(0).nodeType());
        assertEquals("Some text", childNodes.get(0).outerHtml());
    }
    
    @Test
    public void process_doctype_insertsDoctypeNode() {
        String input = "<!DOCTYPE html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        xmlTreeBuilder.process(doctype);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.DOCTYPE_NODE, childNodes.get(0).nodeType());
        assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void insertNode_validNode_appendsNodeToCurrentElement() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Element element = new Element("child3");
        xmlTreeBuilder.insertNode(element);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(element, xmlTreeBuilder.getCurrentElement().childNode(0));
    }

    @Test
    public void insert_startTag_insertsElementWithAttributes() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.StartTag startTag = new Token.StartTag("child3", "attr1=value1 attr2=value2");
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("child3", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
    }

    @Test
    public void insert_startTag_selfClosing_insertsElementAndUpdatesTag() {
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.StartTag startTag = new Token.StartTag("child3", "attr1=value1 attr2=value2", true);
        Element element = xmlTreeBuilder.insert(startTag);

        assertTrue(element.isSelfClosing());
        assertEquals("child3", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
    }

    @Test
    public void insert_comment_insertsCommentNode() {
        String input = "<root><!-- This is a comment --></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Comment comment = new Token.Comment("This is a comment", baseUri);
        xmlTreeBuilder.insert(comment);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.COMMENT_NODE, childNodes.get(0).nodeType());
        assertEquals("This is a comment", childNodes.get(0).outerHtml());
    }

    @Test
    public void insert_comment_withXmlDeclaration_insertsXmlDeclarationNode() {
        String input = "<root><!--?xml version='1.0' encoding='UTF-8'?></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Comment comment = new Token.Comment("?xml version='1.0' encoding='UTF-8'?");
        xmlTreeBuilder.insert(comment);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.DOCUMENT_TYPE_NODE, childNodes.get(0).nodeType());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", childNodes.get(0).outerHtml());
    }

    @Test
    public void insert_character_insertsTextNode() {
        String input = "<root>Some text</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Character character = new Token.Character("Some text");
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.TEXT_NODE, childNodes.get(0).nodeType());
        assertEquals("Some text", childNodes.get(0).outerHtml());
    }

    @Test
    public void insert_doctype_insertsDoctypeNode() {
        String input = "<!DOCTYPE html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.Doctype doctype = new Token.Doctype("html", "", "", baseUri);
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();

        assertEquals(1, childNodes.size());
        assertEquals(Node.DOCTYPE_NODE, childNodes.get(0).nodeType());
        assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void popStackToClose_existingElementName_removesElementFromStack() {
        String input = "<root><child1></child1><child2>Text<child3></child3></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.EndTag endTag = new Token.EndTag("child2");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();

        assertEquals(1, stack.size());
        assertEquals("root", stack.get(0).nodeName());
    }

    @Test
    public void popStackToClose_nonExistingElementName_noChangeInStack() {
        String input = "<root><child1></child1><child2>Text<child3></child3></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Token.EndTag endTag = new Token.EndTag("child4");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();

        assertEquals(3, stack.size());
        assertEquals("child1", stack.get(0).nodeName());
        assertEquals("child2", stack.get(1).nodeName());
        assertEquals("root", stack.get(2).nodeName());
    }

    @Test
    public void parseFragment_validInput_correctDocumentStructure() {
        String inputFragment = "<child1></child1><child2></child2>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(2, childNodes.size());
        assertEquals("child1", childNodes.get(0).nodeName());
        assertEquals("child2", childNodes.get(1).nodeName());
    }

    @Test
    public void parseFragment_invalidInput_errorInParseErrorList() {
        String inputFragment = "<child1><child2></child1>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertTrue(errors.size() > 0);
    }
}