import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {
    
    @InjectMocks
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Mock
    private Element element;
    
    @Mock
    private Document document;
    
    @Mock
    private Token.StartTag startTag;
    
    @Mock
    private Token.EndTag endTag;
    
    @Mock
    private Token.Comment commentToken;
    
    @Mock
    private Token.Character characterToken;
    
    @Mock
    private Token.Doctype doctype;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParse() {
        String input = "<xml><tag>data</tag></xml>";
        String baseUri = "https://example.com";
        Document parsedDocument = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(parsedDocument);
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag>data</tag></xml>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_StartTag() {
        when(startTag.asStartTag()).thenReturn(startTag);
        when(startTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        
        boolean result = xmlTreeBuilder.process(startTag);
        assertTrue(result);
        
        verify(startTag).asStartTag();
        verify(startTag).name();
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
    }
    
    @Test
    public void testProcess_EndTag() {
        when(endTag.asEndTag()).thenReturn(endTag);
        when(endTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.popStackToClose(any())).thenReturn();
        
        boolean result = xmlTreeBuilder.process(endTag);
        assertTrue(result);
        
        verify(endTag).asEndTag();
        verify(endTag).name();
        verify(xmlTreeBuilder).popStackToClose(any());
    }
    
    @Test
    public void testProcess_Comment() {
        when(commentToken.asComment()).thenReturn(commentToken);
        when(commentToken.getData()).thenReturn("comment");
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        
        xmlTreeBuilder.process(commentToken);
        
        verify(commentToken).asComment();
        verify(commentToken).getData();
        verify(xmlTreeBuilder).baseUri;
    }
    
    @Test
    public void testProcess_Character() {
        when(characterToken.asCharacter()).thenReturn(characterToken);
        when(characterToken.getData()).thenReturn("data");
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        
        xmlTreeBuilder.process(characterToken);
        
        verify(characterToken).asCharacter();
        verify(characterToken).getData();
        verify(xmlTreeBuilder).baseUri;
    }
    
    @Test
    public void testProcess_Doctype() {
        when(doctype.getName()).thenReturn("doctype");
        when(doctype.getPublicIdentifier()).thenReturn("publicIdentifier");
        when(doctype.getSystemIdentifier()).thenReturn("systemIdentifier");
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        
        xmlTreeBuilder.process(doctype);
        
        verify(doctype).getName();
        verify(doctype).getPublicIdentifier();
        verify(doctype).getSystemIdentifier();
        verify(xmlTreeBuilder).baseUri;
    }
    
    @Test
    public void testInsertNode() {
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        
        xmlTreeBuilder.insertNode(element);
        
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
    }
    
    @Test
    public void testInsert_StartTag() {
        when(startTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.settings).thenReturn(ParseSettings.preserveCase);
        when(element.tagName()).thenReturn("tag");
        when(element.baseUri()).thenReturn("https://example.com");
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        when(startTag.isSelfClosing()).thenReturn(false);
        when(xmlTreeBuilder.stack.add(any())).thenReturn(true);
        
        Element result = xmlTreeBuilder.insert(startTag);
        assertNotNull(result);
        
        verify(startTag).name();
        verify(xmlTreeBuilder).settings;
        verify(element).tagName();
        verify(element).baseUri();
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
        verify(startTag).isSelfClosing();
        verify(xmlTreeBuilder).stack.add(any());
    }
    
    @Test
    public void testInsert_StartTag_SelfClosing() {
        when(startTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.settings).thenReturn(ParseSettings.preserveCase);
        when(element.tagName()).thenReturn("tag");
        when(element.baseUri()).thenReturn("https://example.com");
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        when(startTag.isSelfClosing()).thenReturn(true);
        when(xmlTreeBuilder.settings.normalizeTag("tag")).thenReturn("tag");
        when(element.attributes()).thenReturn(new Attributes());
        when(xmlTreeBuilder.tokeniser.acknowledgeSelfClosingFlag()).thenReturn();
        when(element.isKnownTag()).thenReturn(false);
        when(element.setSelfClosing()).thenReturn();
        
        Element result = xmlTreeBuilder.insert(startTag);
        assertNotNull(result);
        
        verify(startTag).name();
        verify(xmlTreeBuilder).settings;
        verify(element).tagName();
        verify(element).baseUri();
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
        verify(startTag).isSelfClosing();
        verify(xmlTreeBuilder.settings).normalizeTag("tag");
        verify(element).attributes();
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(element).isKnownTag();
        verify(element).setSelfClosing();
    }
    
    @Test
    public void testInsert_Comment() {
        when(commentToken.getData()).thenReturn("comment");
        when(commentToken.bogus).thenReturn(false);
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        when(xmlTreeBuilder.doc).thenReturn(document);
        when(document.child(0)).thenReturn(element);
        when(xmlTreeBuilder.settings.normalizeTag(element.tagName())).thenReturn("tag");
        when(element.baseUri()).thenReturn("https://example.com");
        when(element.attributes()).thenReturn(new Attributes());
        
        xmlTreeBuilder.insert(commentToken);
        
        verify(commentToken).getData();
        verify(commentToken).bogus();
        verify(xmlTreeBuilder).baseUri;
        verify(xmlTreeBuilder).doc;
        verify(document).child(0);
        verify(xmlTreeBuilder).settings.normalizeTag(element.tagName());
        verify(element).baseUri();
        verify(element).attributes();
    }
    
    @Test
    public void testInsert_Comment_Bogus() {
        when(commentToken.getData()).thenReturn("comment");
        when(commentToken.bogus).thenReturn(true);
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        when(xmlTreeBuilder.doc).thenReturn(document);
        when(document.child(0)).thenReturn(element);
        when(xmlTreeBuilder.settings.normalizeTag(element.tagName())).thenReturn("tag");
        when(element.baseUri()).thenReturn("https://example.com");
        when(element.attributes()).thenReturn(new Attributes());
        
        xmlTreeBuilder.insert(commentToken);
        
        verify(commentToken).getData();
        verify(commentToken).bogus();
        verify(xmlTreeBuilder).baseUri;
        verify(xmlTreeBuilder).doc;
        verify(document).child(0);
        verify(xmlTreeBuilder).settings.normalizeTag(element.tagName());
        verify(element).baseUri();
        verify(element).attributes();
    }
    
    @Test
    public void testInsert_Character() {
        when(characterToken.getData()).thenReturn("data");
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        
        xmlTreeBuilder.insert(characterToken);
        
        verify(characterToken).getData();
        verify(xmlTreeBuilder).baseUri;
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
    }
    
    @Test
    public void testInsert_Doctype() {
        when(doctype.getName()).thenReturn("doctype");
        when(doctype.getPublicIdentifier()).thenReturn("publicIdentifier");
        when(doctype.getSystemIdentifier()).thenReturn("systemIdentifier");
        when(xmlTreeBuilder.baseUri).thenReturn("https://example.com");
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        when(element.appendChild(any())).thenReturn(element);
        
        xmlTreeBuilder.insert(doctype);
        
        verify(doctype).getName();
        verify(doctype).getPublicIdentifier();
        verify(doctype).getSystemIdentifier();
        verify(xmlTreeBuilder).baseUri;
        verify(xmlTreeBuilder).currentElement();
        verify(element).appendChild(any());
    }
    
    @Test
    public void testPopStackToClose() {
        List<Element> stack = new ArrayList<>();
        stack.add(element);
        stack.add(element);
        stack.add(element);
        stack.add(element);
        when(endTag.name()).thenReturn("tag");
        when(element.nodeName()).thenReturn("tag");
        when(element.nodeName()).thenReturn("tag");
        when(element.nodeName()).thenReturn("tag");
        when(element.nodeName()).thenReturn("tag");
        
        xmlTreeBuilder.stack = stack;
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
        
        verify(endTag).name();
        verify(element, times(4)).nodeName();
        verify(element, times(4)).nodeName();
        verify(element, times(4)).nodeName();
        verify(element, times(4)).nodeName();
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>data</tag>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> childNodes = new ArrayList<>();
        when(xmlTreeBuilder.doc).thenReturn(document);
        when(document.childNodes()).thenReturn(childNodes);
        
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(childNodes, result);
        
        verify(xmlTreeBuilder).doc;
        verify(document).childNodes();
    }
}