package org.jsoup.parser;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        // Test tokenType() method
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };

        assert token.tokenType() == null;
    }

    @Test
    public void testResetStringBuilder() {
        // Test reset() method with StringBuilder parameter
        StringBuilder sb = new StringBuilder("test");

        Token.reset(sb);
        assert sb.length() == 0;
    }

    @Test
    public void testDoctypeReset() {
        // Test reset() method in Doctype class
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assert doctype.name.length() == 0;
        assert doctype.publicIdentifier.length() == 0;
        assert doctype.systemIdentifier.length() == 0;
        assert !doctype.forceQuirks;
    }

    @Test
    public void testDoctypeGetters() {
        // Test getters in Doctype class
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        assert doctype.getName().equals("name");
        assert doctype.getPublicIdentifier().equals("publicIdentifier");
        assert doctype.getSystemIdentifier().equals("systemIdentifier");
        assert doctype.isForceQuirks();
    }

    @Test
    public void testTagReset() {
        // Test reset() method in Tag class
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return this;
            }
        };

        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "pendingAttributeName";
        tag.pendingAttributeValue.append("pendingAttributeValue");
        tag.pendingAttributeValueS = "pendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assert tag.tagName == null;
        assert tag.normalName == null;
        assert tag.pendingAttributeName == null;
        assert tag.pendingAttributeValue.length() == 0;
        assert tag.pendingAttributeValueS == null;
        assert !tag.hasEmptyAttributeValue;
        assert !tag.hasPendingAttributeValue;
        assert !tag.selfClosing;
        assert tag.attributes == null;
    }

    @Test
    public void testStartTagReset() {
        // Test reset() method in StartTag class
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue.append("pendingAttributeValue");
        startTag.pendingAttributeValueS = "pendingAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assert startTag.tagName == null;
        assert startTag.normalName == null;
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.pendingAttributeValueS == null;
        assert !startTag.hasEmptyAttributeValue;
        assert !startTag.hasPendingAttributeValue;
        assert !startTag.selfClosing;
        assert startTag.attributes == null;
    }

    @Test
    public void testStartTagNameAttr() {
        // Test nameAttr() method in StartTag class
        Token.StartTag startTag = new Token.StartTag();

        Attributes attributes = new Attributes();
        startTag.nameAttr("tagName", attributes);

        assert startTag.tagName.equals("tagName");
        assert startTag.attributes == attributes;
        assert startTag.normalName.equals("tagname");
    }

    @Test
    public void testStartTagToString() {
        // Test toString() method in StartTag class
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "tagName";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attribute", "value");

        assert startTag.toString().equals("<tagName attribute=\"value\">");
    }

    @Test
    public void testEndTagReset() {
        // Test reset() method in EndTag class
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "pendingAttributeName";
        endTag.pendingAttributeValue.append("pendingAttributeValue");
        endTag.pendingAttributeValueS = "pendingAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        endTag.reset();

        assert endTag.tagName == null;
        assert endTag.normalName == null;
        assert endTag.pendingAttributeName == null;
        assert endTag.pendingAttributeValue.length() == 0;
        assert endTag.pendingAttributeValueS == null;
        assert !endTag.hasEmptyAttributeValue;
        assert !endTag.hasPendingAttributeValue;
        assert !endTag.selfClosing;
        assert endTag.attributes == null;
    }

    @Test
    public void testEndTagToString() {
        // Test toString() method in EndTag class
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "tagName";

        assert endTag.toString().equals("</tagName>");
    }

    @Test
    public void testCommentReset() {
        // Test reset() method in Comment class
        Token.Comment comment = new Token.Comment();

        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assert comment.data.length() == 0;
        assert !comment.bogus;
    }

    @Test
    public void testCommentGetData() {
        // Test getData() method in Comment class
        Token.Comment comment = new Token.Comment();

        comment.data.append("data");

        assert comment.getData().equals("data");
    }

    @Test
    public void testCommentToString() {
        // Test toString() method in Comment class
        Token.Comment comment = new Token.Comment();

        comment.data.append("data");

        assert comment.toString().equals("<!--data-->");
    }

    @Test
    public void testCharacterReset() {
        // Test reset() method in Character class
        Token.Character character = new Token.Character();

        character.data = "data";

        character.reset();

        assert character.data == null;
    }

    @Test
    public void testCharacterData() {
        // Test data() method in Character class
        Token.Character character = new Token.Character();

        character.data("data");

        assert character.getData().equals("data");
    }

    @Test
    public void testCharacterToString() {
        // Test toString() method in Character class
        Token.Character character = new Token.Character();

        character.data = "data";

        assert character.toString().equals("data");
    }

    @Test
    public void testEOFReset() {
        // Test reset() method in EOF class
        Token.EOF eof = new Token.EOF();

        assert eof.reset() == eof;
    }
}