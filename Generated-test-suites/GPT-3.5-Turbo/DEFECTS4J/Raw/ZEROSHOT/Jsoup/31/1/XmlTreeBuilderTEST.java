package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        // Add assertions here
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token();

        // Add assertions here

        xmlTreeBuilder.process(token);

        // Add assertions here
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Node();

        // Add assertions here

        xmlTreeBuilder.insertNode(node);

        // Add assertions here
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();

        // Add assertions here

        xmlTreeBuilder.insert(startTag);

        // Add assertions here
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();

        // Add assertions here

        xmlTreeBuilder.insert(commentToken);

        // Add assertions here
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();

        // Add assertions here

        xmlTreeBuilder.insert(characterToken);

        // Add assertions here
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();

        // Add assertions here

        xmlTreeBuilder.insert(doctype);

        // Add assertions here
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();

        // Add assertions here

        xmlTreeBuilder.popStackToClose(endTag);

        // Add assertions here
    }
}