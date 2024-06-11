import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {
  @Test
  public void testDefaultSettings() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    ParseSettings settings = builder.defaultSettings();
    Assert.assertEquals(ParseSettings.preserveCase, settings);
  }

  @Test
  public void testParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Document document = builder.parse("<xml></xml>", "");
    Assert.assertEquals("<xml></xml>", document.toString());
  }

  @Test
  public void testInitialiseParse() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    builder.initialiseParse("<xml></xml>", "", new ParseErrorList(), new ParseSettings());
    Assert.assertEquals(true, builder.stack.contains(builder.doc));
    Assert.assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
  }

  @Test
  public void testProcess() {
    XmlTreeBuilder builder = new XmlTreeBuilder();

    Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
    builder.process(startTag);

    Token.EndTag endTag = new Token.EndTag("tag");
    builder.process(endTag);

    Token.Comment comment = new Token.Comment("comment");
    builder.process(comment);

    Token.Character character = new Token.Character("data");
    builder.process(character);

    Token.Doctype doctype = new Token.Doctype("name", "public", "system");
    builder.process(doctype);

    Assert.assertEquals(1, builder.doc.childNodeSize());
  }

  @Test
  public void testInsertNode() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Element element = new Element(Tag.valueOf("tag"), "");
    builder.insertNode(element);
    Assert.assertEquals(1, builder.currentElement().childNodeSize());
  }

  @Test
  public void testInsertStartTag() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
    Element element = builder.insert(startTag);
    Assert.assertEquals(Tag.valueOf("tag"), element.tag());
    Assert.assertEquals(1, builder.currentElement().childNodeSize());
    Assert.assertEquals(1, builder.stack.size());
  }

  @Test
  public void testInsertComment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Comment commentToken = new Token.Comment("comment");
    builder.insert(commentToken);
    Assert.assertEquals(1, builder.currentElement().childNodeSize());
  }

  @Test
  public void testInsertCharacter() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Character characterToken = new Token.Character("data");
    builder.insert(characterToken);
    Assert.assertEquals(1, builder.currentElement().childNodeSize());
  }

  @Test
  public void testInsertDoctype() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.Doctype doctypeToken = new Token.Doctype("name", "public", "system");
    builder.insert(doctypeToken);
    Assert.assertEquals(1, builder.currentElement().childNodeSize());
  }

  @Test
  public void testPopStackToClose() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    Token.EndTag endTag = new Token.EndTag("tag");
    builder.stack.add(new Element(Tag.valueOf("tag"), ""));
    builder.popStackToClose(endTag);
    Assert.assertEquals(0, builder.stack.size());
  }

  @Test
  public void testParseFragment() {
    XmlTreeBuilder builder = new XmlTreeBuilder();
    List<Node> nodes = builder.parseFragment("<xml></xml>", "", new ParseErrorList(), new ParseSettings());
    Assert.assertEquals(1, nodes.size());
  }
}

