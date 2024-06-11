import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);
        Assert.assertFalse("Errors should be empty", errors.size() > 0);
        Assert.assertEquals("Document should not be null", xmlTreeBuilder.doc, xmlTreeBuilder.parseFragment(input, "", errors).get(0));
    }

    @Test
    public void testProcessStartTag() {
        String input = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        // Test with valid start tag
        xmlTreeBuilder.initialiseParse(input, "", errors);
        Assert.assertEquals("Number of children in root element should be 1", 1, xmlTreeBuilder.doc.childNodes().size());

        // Test with invalid start tag
        input = "<root><invalidTag></root>";
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);
        Assert.assertEquals("Number of children in root element should be 0", 0, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertFalse("Errors should contain an error for the invalid tag", errors.isEmpty());

        // Test with self-closing tag
        input = "<root><selfClosingTag/></root>";
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, "", errors);
        Assert.assertEquals("Number of children in root element should be 1", 1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertTrue("Self-closing tag should be present as a TextNode", xmlTreeBuilder.doc.childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testProcessEndTag() {
        String input = "<root><child><grandChild/></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);
        xmlTreeBuilder.runParser();

        // Test with valid end tag
        Assert.assertEquals("Number of children in root element should be 1", 1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertEquals("Number of children in root's first child should be 1", 1, xmlTreeBuilder.doc.childNodes().get(0).childNodes().size());
        xmlTreeBuilder.popStackToClose(new Token.EndTag("grandChild"));
        Assert.assertEquals("Number of children in root element should be 0", 0, xmlTreeBuilder.doc.childNodes().size());

        // Test with non-existent end tag
        Assert.assertNull("Popping non-existent end tag should not change the node hierarchy", xmlTreeBuilder.popStackToClose(new Token.EndTag("nonexistentTag")));
    }

    @Test
    public void testProcessCharacter() {
        String input = "<root>&#123;</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc;
        Assert.assertEquals("Number of children in root element should be 1", 1, root.childNodes().size());
        TextNode textNode = (TextNode) root.childNodes().get(0);
        Assert.assertEquals("Character representation should be '{'", "{'", textNode.text());
    }

    @Test
    public void testProcessComment() {
        String input = "<root><!--comment-->
                         <child></child>
                       </root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);
        xmlTreeBuilder.runParser();

        Element root = xmlTreeBuilder.doc;
        Assert.assertEquals("Number of children in root element should be 1", 1, root.childNodes().size());
        TextNode commentNode = (TextNode) root.childNodes().get(0);
        Assert.assertEquals("Comment text should be 'comment'", "comment", commentNode.text());
    }

    @Test
    public void testProcessDoctype() {
        String input = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #IMPLIED > ]><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);
        xmlTreeBuilder.runParser();

        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        Assert.assertEquals("Document type name should be 'root'", "root", doctypeNode.name());
        Assert.assertNotNull("Document type public identifier should be present", doctypeNode.publicIdentifier());
        Assert.assertNull("Document type system identifier should be null", doctypeNode.systemIdentifier());
    }
}