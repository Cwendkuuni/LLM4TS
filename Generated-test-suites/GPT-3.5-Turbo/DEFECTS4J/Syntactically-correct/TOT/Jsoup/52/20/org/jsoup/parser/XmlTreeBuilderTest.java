
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void initialiseParse_withValidInput_setsDocumentOnStack() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        assertEquals(1, builder.getStack().size());
        assertEquals("root", builder.getCurrentElement().nodeName());
    }
    
    @Test
    public void initialiseParse_withValidInput_setsXmlSyntaxOnOutputSettings() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(Document.OutputSettings.Syntax.xml, builder.getDocument().outputSettings().syntax());
    }
    
    @Test
    public void process_withStartTagToken_insertsElementIntoStack() {
        Token token = new Token.StartTag("div");
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(2, builder.getStack().size());
        assertEquals("div", builder.getCurrentElement().nodeName());
    }
    
    @Test
    public void process_withStartTagToken_setsSelfClosingFlagForUnknownTag() {
        Token token = new Token.StartTag("unknown");
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertTrue(token.isSelfClosing());
    }
    
    @Test
    public void process_withEndTagToken_removesElementFromStack() {
        Token token = new Token.EndTag("div");
        builder.getStack().push(new Element("div"));
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.getStack().size());
        assertEquals("parent", builder.getCurrentElement().nodeName());
    }
    
    @Test
    public void process_withCommentToken_insertsCommentIntoStack() {
        Token token = new Token.Comment("comment");
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.getCurrentElement().childNodeSize());
        assertEquals("comment", builder.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void process_withCharacterToken_insertsTextNodeIntoStack() {
        Token token = new Token.Character("text");
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.getCurrentElement().childNodeSize());
        assertEquals("text", builder.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void process_withDoctypeToken_insertsDocumentTypeIntoStack() {
        Token token = new Token.Doctype("html", "", "", "");
        builder.getStack().push(new Element("parent"));

        boolean result = builder.process(token);

        assertTrue(result);
        assertEquals(1, builder.getCurrentElement().childNodeSize());
        assertEquals("<!doctype html>", builder.getCurrentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void parseFragment_withValidInput_returnsListOfNodes() {
        String inputFragment = "<p>paragraph</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodeList = builder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(1, nodeList.size());
        assertEquals("p", nodeList.get(0).nodeName());
    }
}

