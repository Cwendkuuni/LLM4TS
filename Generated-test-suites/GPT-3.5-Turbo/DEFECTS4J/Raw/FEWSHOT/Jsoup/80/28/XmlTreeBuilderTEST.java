package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder treeBuilder;
    private Element currentElement;
    private Stack<Element> stack;
    private Document doc;
    private String baseUri;
    private ParseErrorList errors;
    private ParseSettings settings;
    
    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
        currentElement = mock(Element.class);
        stack = mock(Stack.class);
        doc = mock(Document.class);
        baseUri = "https://www.example.com";
        errors = mock(ParseErrorList.class);
        settings = mock(ParseSettings.class);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("input");
        
        when(doc.outputSettings()).thenReturn(mock(Document.OutputSettings.class));
        
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(doc, treeBuilder.stack);
    }
    
    @Test
    public void testProcessStartTag() {
        StartTag startTag = mock(StartTag.class);
        Token token = mock(Token.class);
        
        when(startTag.name()).thenReturn("tag");
        when(settings.normalizeAttributes(startTag.attributes)).thenReturn(mock(Attributes.class));
        
        treeBuilder.stack = stack;
        
        Element el = treeBuilder.insert(startTag);
        
        assertEquals(startTag.name(), el.tag().getName());
        assertEquals(el, treeBuilder.stack.get(treeBuilder.stack.size()-1));
    }
    
    @Test
    public void testProcessEndTag() {
        EndTag endTag = mock(EndTag.class);
        Token token = mock(Token.class);
        
        when(endTag.tagName).thenReturn("tag");
        when(settings.normalizeTag(endTag.tagName)).thenReturn("normalized-tag");
        
        treeBuilder.currentElement = currentElement;
        treeBuilder.stack = stack;
        
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(doc, treeBuilder.doc);
    }
    
    @Test
    public void testProcessComment() {
        Comment commentToken = mock(Comment.class);
        Token token = mock(Token.class);
        
        when(commentToken.getData()).thenReturn("comment");
        
        treeBuilder.insert(commentToken);
        
        // assert comment insertion
    }
    
    @Test
    public void testProcessCharacter() {
        Character characterToken = mock(Character.class);
        Token token = mock(Token.class);
        
        when(characterToken.getData()).thenReturn("data");
        when(characterToken.isCData()).thenReturn(false);
        
        treeBuilder.insert(characterToken);
        
        // assert character insertion
    }
    
    @Test
    public void testProcessDoctype() {
        Doctype doctypeToken = mock(Doctype.class);
        Token token = mock(Token.class);
        
        when(doctypeToken.getName()).thenReturn("name");
        when(settings.normalizeTag(doctypeToken.getName())).thenReturn("normalized-name");
        when(doctypeToken.getPublicIdentifier()).thenReturn("public-identifier");
        when(doctypeToken.getSystemIdentifier()).thenReturn("system-identifier");
        when(doctypeToken.getPubSysKey()).thenReturn("pub-sys-key");
        
        treeBuilder.insert(doctypeToken);
        
        // assert doctype insertion
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilderSpy = mock(XmlTreeBuilder.class);
        List<Node> childNodes = mock(List.class);
        ParseErrorList errors = mock(ParseErrorList.class);
        ParseSettings settings = mock(ParseSettings.class);
        
        String inputFragment = "input";
        String baseUri = "https://www.example.com";
        
        when(treeBuilderSpy.parseFragment(inputFragment, baseUri, errors, settings)).thenCallRealMethod();

        treeBuilderSpy.initialiseParse(new StringReader(inputFragment), baseUri, errors, settings);
        treeBuilderSpy.runParser();
        
        // assert parseFragment result
    }
}