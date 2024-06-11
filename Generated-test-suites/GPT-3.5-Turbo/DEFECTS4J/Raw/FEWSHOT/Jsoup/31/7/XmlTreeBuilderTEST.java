package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

@Test
public void testInitialiseParse() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// test the initialiseParse method
// provide input, baseUri, and errors parameters
String input = "";
String baseUri = "";
ParseErrorList errors = new ParseErrorList();
builder.initialiseParse(input, baseUri, errors);

// assert statements
// check if the doc is added to the stack
assertEquals(1, builder.stack.size());
assertSame(builder.doc, builder.stack.get(0));
}
@Test
public void testProcess() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// test the process method with different token types
Token startTag = new Token(Token.TokenType.StartTag);
Token endTag = new Token(Token.TokenType.EndTag);
Token comment = new Token(Token.TokenType.Comment);
Token character = new Token(Token.TokenType.Character);
Token doctype = new Token(Token.TokenType.Doctype);
Token eof = new Token(Token.TokenType.EOF);

// assert statements
// check if the process method returns true for all token types
assertTrue(builder.process(startTag));
assertTrue(builder.process(endTag));
assertTrue(builder.process(comment));
assertTrue(builder.process(character));
assertTrue(builder.process(doctype));
assertTrue(builder.process(eof));
}
@Test
public void testInsertNode() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create a new element
Element element = new Element("tag");

// test the insertNode method
builder.insertNode(element);

// assert statements
// check if the element is appended to the current element
assertEquals(element, builder.currentElement().getChild(0));
}
@Test
public void testInsertStartTag() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create a new start tag
Token.StartTag startTag = new Token.StartTag("tag");

// test the insert method with a start tag
Element element = builder.insert(startTag);

// assert statements
// check if the element is appended to the current element
assertEquals(element, builder.currentElement().getChild(0));
}

@Test
public void testInsertComment() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create a new comment token
Token.Comment commentToken = new Token.Comment("comment");

// test the insert method with a comment token
builder.insert(commentToken);

// assert statements
// check if the comment is appended to the current element
assertEquals(commentToken.getData(), builder.currentElement().getChild(0).toString());
}

@Test
public void testInsertCharacter() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create a new character token
Token.Character characterToken = new Token.Character("character");

// test the insert method with a character token
builder.insert(characterToken);

// assert statements
// check if the character is appended to the current element
assertEquals(characterToken.getData(), builder.currentElement().getChild(0).toString());
}

@Test
public void testInsertDoctype() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create a new doctype token
Token.Doctype doctype = new Token.Doctype("name", "public", "system");

// test the insert method with a doctype token
builder.insert(doctype);

// assert statements
// check if the doctype is appended to the current element
assertEquals(doctype.getName(), builder.currentElement().getChild(0).toString());
}

@Test
public void testPopStackToClose() {
// create a new instance of XmlTreeBuilder
XmlTreeBuilder builder = new XmlTreeBuilder();

// create an end tag token
String tagName = "tag";
Token.EndTag endTag = new Token.EndTag(tagName);

// create a new element and add it to the stack
Element element = new Element(tagName);
builder.stack.push(element);

// test the popStackToClose method with an end tag token
builder.popStackToClose(endTag);

// assert statements
// check if the element is removed from the stack
assertEquals(0, builder.stack.size());
}

}