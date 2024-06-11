package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    private ParseSettings settings;

    public XmlTreeBuilderTest(String input, String baseUri, ParseErrorList errors, ParseSettings settings) {
        this.input = input;
        this.baseUri = baseUri;
        this.errors = errors;
        this.settings = settings;
    }

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        // typical use cases
        Object[][] testData = {
                {"<root></root>", "https://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase},
                {"<tag attr1=\"value1\"/>", "https://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase}
        };
        return Arrays.asList(testData);
    }

    @Test
    public void testParse_ReturnsDocument() {
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        assertNotNull(document);
    }

    @Test
    public void testParseFragment_ReturnsListOfNodes() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<tag>Text</tag>", baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    @Test
    public void testInsertNode_InsertsNodeIntoCurrentElement() {
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        xmlTreeBuilder.insertNode(new Element("tag"));
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testInsert_InsertsStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        xmlTreeBuilder.insert(startTag);
        
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        assertEquals(1, document.children().size());
        assertEquals("tag", document.children().get(0).tagName());
    }

    @Test
    public void testInsert_InsertsCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        assertEquals(1, document.childNodes().size());
        assertEquals("comment", document.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsert_InsertsCharacterToken() {
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);

        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        assertEquals(1, document.childNodes().size());
        assertEquals("text", document.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsert_InsertsDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);

        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);
        assertEquals(1, document.childNodes().size());
        assertEquals("html", document.childNodes().get(0).nodeName());
    }

    @Test
    public void testPopStackToClose_PopsStackToRemoveFirstOccurrenceOfElement() {
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri, errors, settings);

        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");

        xmlTreeBuilder.insertNode(element1);
        xmlTreeBuilder.insertNode(element2);
        xmlTreeBuilder.insertNode(element3);

        Token.EndTag endTag = new Token.EndTag("tag2");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, document.childNodes().size());
        assertEquals("tag3", document.childNodes().get(1).nodeName());
    }

    // add additional test cases, edge cases, and error scenarios as needed

}