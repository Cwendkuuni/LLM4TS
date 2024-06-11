
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Test
    public void testParse() {
        String xmlInput = "<root><element>Text</element></root>";
        Reader input = new StringReader(xmlInput);
        String baseUri = "http://example.com";

        xmlTreeBuilder = new XmlTreeBuilder();
        Document result = xmlTreeBuilder.parse(input, baseUri);

        assertThat(result, is(notNullValue()));
        assertThat(result.baseUri(), is(baseUri));
        assertThat(result.nodeName(), is("root"));
    }

    @Test
    public void testParse_String() {
        String xmlInput = "<root><element>Text</element></root>";
        String baseUri = "http://example.com";

        xmlTreeBuilder = new XmlTreeBuilder();
        Document result = xmlTreeBuilder.parse(xmlInput, baseUri);

        assertThat(result, is(notNullValue()));
        assertThat(result.baseUri(), is(baseUri));
        assertThat(result.nodeName(), is("root"));
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("testElement");
        xmlTreeBuilder.insertNode(element);

        assertThat(xmlTreeBuilder.currentElement().children(), hasItem(element));
    }

    @Test
    public void testInsert_StartTag() {
        xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.StartTag startTag = new XmlTreeBuilder.Token.StartTag("testTag", "attr=\"value\"");

        Element element = xmlTreeBuilder.insert(startTag);

        assertThat(element, is(notNullValue()));
        assertThat(element.nodeName(), is("testTag"));
    }

    @Test
    public void testInsert_Comment() {
        xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Comment comment = new XmlTreeBuilder.Token.Comment("test comment");

        xmlTreeBuilder.insert(comment);

        List<Element> children = xmlTreeBuilder.currentElement().children();
        assertThat(children.get(0).nodeName(), is("#comment"));
        assertThat(children.get(0).toString(), is("<!--test comment-->"));
    }

    @Test
    public void testInsert_Character() {
        xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Character character = new XmlTreeBuilder.Token.Character("test character");

        xmlTreeBuilder.insert(character);

        List<Element> children = xmlTreeBuilder.currentElement().children();
        assertThat(children.get(0).nodeName(), is("#text"));
        assertThat(children.get(0).toString(), is("test character"));
    }

    @Test
    public void testInsert_Doctype() {
        xmlTreeBuilder = new XmlTreeBuilder();
        XmlTreeBuilder.Token.Doctype doctype = new XmlTreeBuilder.Token.Doctype("html", "publicId", "systemId");

        xmlTreeBuilder.insert(doctype);

        List<Element> children = xmlTreeBuilder.currentElement().children();
        assertThat(children.get(0).nodeName(), is("#doctype"));
        assertThat(children.get(0).toString(), is("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">"));
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        Element element3 = new Element("element1");

        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        XmlTreeBuilder.Token.EndTag endTag = new XmlTreeBuilder.Token.EndTag("element1");
        xmlTreeBuilder.popStackToClose(endTag);

        assertThat(xmlTreeBuilder.stack.size(), is(1));
        assertThat(xmlTreeBuilder.stack.get(0), is(element2));
    }

    @Test
    public void testParseFragment() {
        String fragment = "<p>Fragment</p>";
        String baseUri = "http://example.com";
        xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);

        List<org.jsoup.nodes.Node> nodes = xmlTreeBuilder.parseFragment(fragment, baseUri, parser);

        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).nodeName(), is("p"));
        assertThat(nodes.get(0).toString(), is("<p>Fragment</p>"));
    }

    @Test
    public void testParseFragmentWithContext() {
        Element context = new Element("contextElement");
        String fragment = "<p>Fragment</p>";
        String baseUri = "http://example.com";
        xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);

        List<org.jsoup.nodes.Node> nodes = xmlTreeBuilder.parseFragment(fragment, context, baseUri, parser);

        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).nodeName(), is("p"));
        assertThat(nodes.get(0).toString(), is("<p>Fragment</p>"));
    }
}
