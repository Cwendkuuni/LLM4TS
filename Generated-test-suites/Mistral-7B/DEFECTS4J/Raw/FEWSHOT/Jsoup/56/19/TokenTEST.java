package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeSetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("HTML5");
        doctype.getName().equals("HTML5");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier().equals("");
        doctype.setPublicIdentifier("PublicID");
        doctype.getPublicIdentifier().equals("PublicID");
        doctype.reset();
        doctype.getPublicIdentifier().equals("");
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getSystemIdentifier().equals("");
        doctype.setSystemIdentifier("SystemID");
        doctype.getSystemIdentifier().equals("SystemID");
        doctype.reset();
        doctype.getSystemIdentifier().equals("");
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name().equals("");
        tag.reset();
        tag.name().equals("");
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("p");
        tag.getName().equals("p");
        tag.reset();
        tag.getName().equals("");
    }

    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("P");
        tag.normalName().equals("p");
        tag.reset();
        tag.normalName().equals(null);
    }

    @Test
    public void testTagAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.getAttributes() != null;
        tag.reset();
        tag.getAttributes() == null;
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.newAttribute();
        tag.getAttributes().size() == 1;
        tag.reset();
        tag.getAttributes() == null;
    }

    @Test
    public void testTagSetAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeValue("value");
        tag.getAttributes().get("value").toString().equals("value");
        tag.reset();
        tag.getAttributes().isEmpty();
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeValue("value");
        Attributes attributes = tag.getAttributes();
        attributes.get("value").toString().equals("value");
        tag.reset();
        tag.getAttributes().isEmpty();
    }
}