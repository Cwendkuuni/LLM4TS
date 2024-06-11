package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.jsoup.util.CharacterInput;
import org.jsoup.util.CharacterReader;
import org.jsoup.util.SimpleErrorHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader reader;
    private SimpleErrorHandler errors;

    @Before
    public void setUp() {
        reader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body><script>function test() { alert('Hello World!'); }</script></body>".getBytes()));
        errors = new SimpleErrorHandler();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadSimpleTag() {
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("head", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("title", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("Test", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("title", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("body", token.tagName);
        // test reading empty tag
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("script", token.tagName);
        Validate.isTrue(token.startPending.isSelfClosing());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("script", token.tagName);
    }

    @Test
    public void testReadMultiCharacterTag() {
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("html", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("head", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("title", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("Test", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("title", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("body", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("{", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("s", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("c", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("r", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("i", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("p", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals(">", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("script", token.tagName);
    }

    @Test
    public void testErrorHandling() {
        Validate.isTrue(errors.getNumberOfErrors() == 0);
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
        tokeniser.read();
        tokeniser.error(TokeniserState.Data);
        Assert.assertEquals(1, errors.getNumberOfErrors());
        Assert.assertEquals("Unexpected character '\\u0000' in input state [Data]", errors.getErrors()[0].message);
        errors.clear();
        tokeniser = new Tokeniser(new CharacterReader("<script>function test() { alert('Hello World!'); }</script>"), errors);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read(); // expect error here
        Assert.assertEquals(1, errors.getNumberOfErrors());
        Assert.assertEquals("Self closing flag not acknowledged", errors.getErrors()[0].message);
        errors.clear();
        tokeniser = new Tokeniser(new CharacterReader("&foo;"), errors);
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals(1, errors.getNumberOfErrors());
        Assert.assertEquals("Invalid named referenece 'foo'", errors.getErrors()[0].message);
    }

    @Test
    public void testReadCharacterReference() {
        tokeniser = new Tokeniser(new CharacterReader("&a;"), errors);
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("\u0061", token.charPending.data());
        tokeniser = new Tokeniser(new CharacterReader("&A;"), errors);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("\u0041", token.charPending.data());
        tokeniser = new Tokeniser(new CharacterReader("&#123;"), errors);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("\u007B", token.charPending.data());
        tokeniser = new Tokeniser(new CharacterReader("&#x7B;"), errors);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("\u007B", token.charPending.data());
        tokeniser = new Tokeniser(new CharacterReader("&xxx;"), errors);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals(Tokeniser.replacementChar, token.charPending.data());
    }

    @Test
    public void testReadComment() {
        tokeniser = new Tokeniser(new CharacterReader("<!-- Test comment -->"), errors);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Comment);
        Assert.assertEquals("Test comment", token.commentPending.data());
    }

    @Test
    public void testReadScriptTag() {
        tokeniser = new Tokeniser(new CharacterReader("<script>function test() { alert('Hello World!'); }</script>"), errors);
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.StartTag);
        Assert.assertEquals("script", token.tagName);
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("{", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("s", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("c", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("r", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("i", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("p", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals("t", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Character);
        Assert.assertEquals(">", token.charPending.data());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("script", token.tagName);
    }

    @Test
    public void testReadDoctypeTag() {
        tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html>"), errors);
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Doctype);
        Assert.assertEquals("html", token.doctypePending.publicId());
        Assert.assertEquals(null, token.doctypePending.systemId());
        Assert.assertEquals(null, token.doctypePending.internalSubset());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("!DOCTYPE", token.tagName);
    }

    @Test
    public void testReadDoctypeTagWithInternalSubset() {
        tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html SYSTEM \"about:legacy-compat\" [ <!ENTITY foo \"bar\"> ]>"), errors);
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Doctype);
        Assert.assertEquals("html", token.doctypePending.publicId());
        Assert.assertEquals("about:legacy-compat", token.doctypePending.systemId());
        Assert.assertEquals("<!ENTITY foo \"bar\">", token.doctypePending.internalSubset());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("!DOCTYPE", token.tagName);
    }

    @Test
    public void testReadDoctypeTagWithSystemSubset() {
        tokeniser = new Tokeniser(new CharacterReader("<!DOCTYPE html SYSTEM \"foo.dtd\">"), errors);
        Token token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.Doctype);
        Assert.assertEquals("html", token.doctypePending.publicId());
        Assert.assertEquals("foo.dtd", token.doctypePending.systemId());
        Assert.assertEquals(null, token.doctypePending.internalSubset());
        token = tokeniser.read();
        Validate.isTrue(token.type == TokenType.EndTag);
        Assert.assertEquals("!DOCTYPE", token.tagName);
    }
}