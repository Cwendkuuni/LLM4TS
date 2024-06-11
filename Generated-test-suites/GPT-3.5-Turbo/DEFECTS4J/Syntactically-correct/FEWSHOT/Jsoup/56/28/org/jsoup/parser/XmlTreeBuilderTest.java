package org.jsoup.parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.ArrayList;
/* Other import statements */

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    /** 
     * Test case for defaultSettings() method. 
     * Should return ParseSettings.preserveCase.
     */
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(expectedSettings, actualSettings);
    }
    
    /** 
     * Test case for parse(String input, String baseUri) method. 
     * Should return a Document object.
     */
    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "";
        Document expectedDocument = new Document(baseUri);
        Element root = new Element(Tag.valueOf("xml"), baseUri);
        expectedDocument.appendChild(root);
        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }
    
    /** 
     * Test case for initialiseParse(String input, String baseUri, ParseErrorList errors, ParseSettings settings) method.
     * Should update the stack and set the syntax of output settings to XML.
     */
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        Document document = new Document(baseUri);
        Stack<Element> stack = new Stack<Element>();
        stack.add(document);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(stack, xmlTreeBuilder.getStack());
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }
    
    /** 
     * Test case for process(Token token) method with StartTag token.
     * Should insert a new element into the currentElement.
     */
    @Test
    public void testProcessWithStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("xml");
        Element expectedElement = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(expectedElement);
        xmlTreeBuilder.getStack().add(expectedElement);
        
        xmlTreeBuilder.process(token);
        
        Element actualElement = xmlTreeBuilder.getCurrentElement();
        assertEquals(expectedElement, actualElement);
        assertTrue(xmlTreeBuilder.getStack().contains(expectedElement));
    }
    
    /** 
     * Test case for process(Token token) method with EndTag token.
     * Should remove the first occurrence of the element with the same name from the stack.
     */
    @Test
    public void testProcessWithEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.EndTag("xml");
        Element element = new Element(Tag.valueOf("xml"), "");
        ArrayList<Element> stack = new ArrayList<>();
        stack.add(element);
        xmlTreeBuilder.setStack(stack);
        
        xmlTreeBuilder.process(token);
        
        assertFalse(xmlTreeBuilder.getStack().contains(element));
    }
    
    /** 
     * Test case for process(Token token) method with Comment token.
     * Should insert a new Comment into the currentElement.
     */
    @Test
    public void testProcessWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.process(token);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof Comment);
    }
    
    /** 
     * Test case for process(Token token) method with Character token.
     * Should insert a new TextNode into the currentElement.
     */
    @Test
    public void testProcessWithCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("This is a character");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.process(token);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof TextNode);
    }
    
    /** 
     * Test case for process(Token token) method with Doctype token.
     * Should insert a new DocumentType into the currentElement.
     */
    @Test
    public void testProcessWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.DocType tokenDoctype = new Token.Doctype("html");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.process(tokenDoctype);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof DocumentType);
    }
    
    /** 
     * Test case for insert(Token.StartTag startTag) method.
     * Should insert a new element into the currentElement and add it to the stack.
     */
    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag tokenStartTag = new Token.StartTag("xml");
        Element expectedElement = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(expectedElement);
        xmlTreeBuilder.getStack().add(expectedElement);
        
        Element actualElement = xmlTreeBuilder.insert(tokenStartTag);
        
        assertEquals(expectedElement, actualElement);
        assertTrue(xmlTreeBuilder.getStack().contains(expectedElement));
    }
    
    /** 
     * Test case for insert(Token.Comment commentToken) method.
     * Should insert a new Comment into the currentElement.
     */
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.insert(commentToken);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof Comment);
    }
    
    /** 
     * Test case for insert(Token.Character characterToken) method.
     * Should insert a new TextNode into the currentElement.
     */
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.insert(characterToken);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof TextNode);
    }
    
    /** 
     * Test case for insert(Token.Doctype d) method.
     * Should insert a new DocumentType into the currentElement.
     */
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        Element element = new Element(Tag.valueOf("xml"), "");
        xmlTreeBuilder.setCurrentElement(element);
        Document document = new Document("");
        xmlTreeBuilder.setDoc(document);
        int initialChildNodeSize = document.childNodes().size();
        
        xmlTreeBuilder.insert(doctypeToken);
        
        int actualChildNodeSize = document.childNodes().size();
        assertEquals(initialChildNodeSize + 1, actualChildNodeSize);
        assertTrue(document.childNodes().get(initialChildNodeSize) instanceof DocumentType);
    }
    
    /** 
     * Test case for popStackToClose(Token.EndTag endTag) method.
     * Should remove the first occurrence of the element with the same name from the stack.
     */
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag token = new Token.EndTag("xml");
        Element element1 = new Element(Tag.valueOf("html"), "");
        Element element2 = new Element(Tag.valueOf("xml"), "");
        Element element3 = new Element(Tag.valueOf("xml"), "");
        ArrayList<Element> stack = new ArrayList<>();
        stack.add(element1);
        stack.add(element2);
        stack.add(element3);
        xmlTreeBuilder.setStack(stack);
        
        xmlTreeBuilder.popStackToClose(token);
        
        assertFalse(xmlTreeBuilder.getStack().contains(element2));
    }
    
    /** 
     * Test case for parseFragment(String inputFragment, String baseUri, ParseErrorList errors, ParseSettings settings) method.
     * Should return a list of child nodes of a Document object.
     */
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<xml><p>This is a paragraph</p></xml>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true, true);
        Document document = new Document(baseUri);
        ArrayList<Node> expectedNodes = new ArrayList<>();
        Element rootElement = new Element(Tag.valueOf("xml"), baseUri);
        Element pElement = new Element(Tag.valueOf("p"), baseUri);
        pElement.appendChild(new TextNode("This is a paragraph", baseUri));   
        rootElement.appendChild(pElement);
        expectedNodes.add(rootElement);
        expectedNodes.add(pElement);
        expectedNodes.add(new TextNode("This is a paragraph", baseUri));
        
        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(expectedNodes, actualNodes);
    }
}