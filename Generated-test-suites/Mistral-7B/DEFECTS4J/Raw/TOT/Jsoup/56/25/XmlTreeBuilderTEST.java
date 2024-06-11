import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parse.ParseSettings;
import org.jsoup.parse.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseValidXml() {
        String xmlInput = "<root><child element=\"value\"/></root>";
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> parsedNodes = builder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) parsedNodes.get(0);
        assertEquals("root", root.tagName());
        Element child = (Element) root.child(0);
        assertEquals("child", child.tagName());
        assertEquals("value", child.attr("element"));
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlInput = "<root><tag/></root>";
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> parsedNodes = builder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) parsedNodes.get(0);
        Element tag = (Element) root.child(0);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlInput = "<root><!-- This is a comment --></root>";
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> parsedNodes = builder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) parsedNodes.get(0);
        Comment comment = (Comment) root.child(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidTokenType() {
        String xmlInput = "<root><!-- This is a comment --><badToken/></root>"; // This token type is not defined
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> parsedNodes = builder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @Test(expected = org.jsoup.parser.ParseError.class)
    public void testParseXmlWithParseError() {
        String xmlInput = "<root><child><![CDATA[<script>alert('Hello World')]]></child></root>";
        Document xmlDoc = Jsoup.parse(xmlInput, Parser.xmlParser());

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> parsedNodes = builder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }
}