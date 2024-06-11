package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EndTag;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

   @Test
   public void testDefaultSettings() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();

      Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
   }

   @Test
   public void testParseReaderString() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Reader input = new StringReader("<xml></xml>");
      String baseUri = "";
      Document document = xmlTreeBuilder.parse(input, baseUri);

      Assert.assertNotNull(document);
   }

   @Test
   public void testParseStringString() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      String input = "<xml></xml>";
      String baseUri = "";
      Document document = xmlTreeBuilder.parse(input, baseUri);

      Assert.assertNotNull(document);
   }

   @Test
   public void testInitialiseParse() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Reader input = new StringReader("<xml></xml>");
      String baseUri = "";
      ParseErrorList errors = ParseErrorList.noTracking();
      ParseSettings settings = ParseSettings.preserveCase;
      xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
   }

   @Test
   public void testProcess() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      StartTag startTag = new StartTag("tagName", "attributes");
      Comment commentToken = new Comment("data");
      Character characterToken = new Character("data");
      Doctype doctype = new Doctype("name", "publicIdentifier", "systemIdentifier");
      EndTag endTag = new EndTag("tagName");

      xmlTreeBuilder.process(startTag);
      xmlTreeBuilder.process(endTag);
      xmlTreeBuilder.process(commentToken);
      xmlTreeBuilder.process(characterToken);
      xmlTreeBuilder.process(doctype);
   }

   @Test
   public void testInsertNode() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Node node = new Node();

      xmlTreeBuilder.insertNode(node);
   }

   @Test
   public void testInsertStartTag() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.StartTag startTag = new Token.StartTag("tagName", "attributes");
      Element element = xmlTreeBuilder.insert(startTag);

      Assert.assertNotNull(element);
   }

   @Test
   public void testInsertComment() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Comment commentToken = new Token.Comment("data");

      xmlTreeBuilder.insert(commentToken);
   }

   @Test
   public void testInsertCharacter() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Character characterToken = new Token.Character("data");

      xmlTreeBuilder.insert(characterToken);
   }

   @Test
   public void testInsertDoctype() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");

      xmlTreeBuilder.insert(doctype);
   }

   @Test
   public void testPopStackToClose() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.EndTag endTag = new Token.EndTag("tagName");

      xmlTreeBuilder.popStackToClose(endTag);
   }

   @Test
   public void testParseFragment() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      String inputFragment = "<xml></xml>";
      String baseUri = "";
      ParseErrorList errors = ParseErrorList.noTracking();
      ParseSettings settings = ParseSettings.preserveCase;
      List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

      Assert.assertNotNull(nodes);
   }
}