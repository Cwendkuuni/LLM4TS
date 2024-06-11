package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlTreeBuilderTest {

	// Test defaultSettings()
	@Test
	public void testDefaultSettings() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
		assertEquals(ParseSettings.preserveCase, parseSettings);
	}

	// Test parse(String input, String baseUri)
	@Test
	public void testParse() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		String input = "<root><child></child></root>";
		String baseUri = "http://www.example.com";
		Document document = xmlTreeBuilder.parse(input, baseUri);
		assertEquals("<root>\n <child></child>\n</root>", document.toString());
		assertEquals("http://www.example.com", document.baseUri());
	}

	// Test initialiseParse(String input, String baseUri, ParseErrorList errors,
	// ParseSettings settings)
	@Test
	public void testInitialiseParse() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		String input = "<root><child></child></root>";
		String baseUri = "http://www.example.com";
		ParseErrorList errors = new ParseErrorList();
		ParseSettings settings = ParseSettings.preserveCase;

		xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
		assertEquals("http://www.example.com", xmlTreeBuilder.baseUri());
		assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings());
	}

	// Test process(Token token)
	@Test
	public void testProcess() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Token startTagToken = new Token.StartTag("child", "");
		Token endTagToken = new Token.EndTag("child");
		Token commentToken = new Token.Comment("This is a comment");
		Token characterToken = new Token.Character("This is a character");
		Token doctypeToken = new Token.Doctype("html");

		xmlTreeBuilder.process(startTagToken);
		xmlTreeBuilder.process(endTagToken);
		xmlTreeBuilder.process(commentToken);
		xmlTreeBuilder.process(characterToken);
		xmlTreeBuilder.process(doctypeToken);

		// Validate if the tokens are processed correctly

		assertEquals("<child></child>", xmlTreeBuilder.currentElement().toString());
		assertEquals("This is a comment", xmlTreeBuilder.currentElement().nextSibling().toString());
		assertEquals("This is a character", xmlTreeBuilder.currentElement().nextSibling().nextSibling().toString());
		assertEquals("<!doctype html>", xmlTreeBuilder.currentElement().nextSibling().nextSibling().nextSibling().toString());

	}

	// Test insertNode(Node node)
	@Test
	public void testInsertNode() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Document document = new Document("");
		xmlTreeBuilder.stack.add(document);

		Element element = new Element(Tag.valueOf("child"), "");
		xmlTreeBuilder.insertNode(element);

		assertEquals(element, xmlTreeBuilder.currentElement().child(0));
	}

	// Test insert(Token.StartTag startTag)
	@Test
	public void testInsertStartTag() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Token.StartTag startTag = new Token.StartTag("child", "");
		xmlTreeBuilder.baseUri = "http://www.example.com";
		xmlTreeBuilder.settings = ParseSettings.preserveCase;

		Element element = xmlTreeBuilder.insert(startTag);

		assertEquals("<child></child>", element.toString());
		assertEquals("http://www.example.com", element.baseUri());
		assertEquals(ParseSettings.preserveCase, element.ownerDocument().settings());
	}

	// Test insert(Token.Comment commentToken)
	@Test
	public void testInsertComment() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Token.Comment commentToken = new Token.Comment("This is a comment");

		xmlTreeBuilder.insert(commentToken);

		assertEquals("This is a comment", xmlTreeBuilder.currentElement().toString());
	}

	// Test insert(Token.Character characterToken)
	@Test
	public void testInsertCharacter() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Token.Character characterToken = new Token.Character("This is a character");

		xmlTreeBuilder.insert(characterToken);

		assertEquals("This is a character", xmlTreeBuilder.currentElement().toString());
	}

	// Test insert(Token.Doctype d)
	@Test
	public void testInsertDoctype() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Token.Doctype doctypeToken = new Token.Doctype("html");

		xmlTreeBuilder.insert(doctypeToken);

		assertEquals("<!doctype html>", xmlTreeBuilder.currentElement().toString());
	}

	// Test popStackToClose(Token.EndTag endTag)
	@Test
	public void testPopStackToClose() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		Element childElement1 = new Element(Tag.valueOf("child"), "");
		Element childElement2 = new Element(Tag.valueOf("child"), "");
		Element childElement3 = new Element(Tag.valueOf("child"), "");
		xmlTreeBuilder.stack.add(childElement3);
		xmlTreeBuilder.stack.add(childElement2);
		xmlTreeBuilder.stack.add(childElement1);

		Token.EndTag endTag = new Token.EndTag("child");
		xmlTreeBuilder.popStackToClose(endTag);

		assertEquals(0, xmlTreeBuilder.stack.size());
	}

	// Test parseFragment(String inputFragment, String baseUri, ParseErrorList errors,
	// ParseSettings settings)
	@Test
	public void testParseFragment() {
		XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
		String inputFragment = "<child></child>";
		String baseUri = "http://www.example.com";
		ParseErrorList errors = new ParseErrorList();
		ParseSettings settings = ParseSettings.preserveCase;

		List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
		assertEquals("<child></child>", nodes.get(0).toString());
	}

}