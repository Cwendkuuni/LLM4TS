package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertEquals(token.type, null);
        assertEquals(TokenType.Doctype, TokenType.valueOf("Doctype"));
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.name.append("test");
        doctype.pubSysKey = "testKey";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        
        doctype.reset();

        assertEquals(doctype.forceQuirks, false);
        assertEquals(doctype.name.toString(), "");
        assertEquals(doctype.pubSysKey, null);
        assertEquals(doctype.publicIdentifier.toString(), "");
        assertEquals(doctype.systemIdentifier.toString(), "");
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "testKey";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;

        assertEquals(doctype.getName(), "test");
        assertEquals(doctype.getPubSysKey(), "testKey");
        assertEquals(doctype.getPublicIdentifier(), "pubId");
        assertEquals(doctype.getSystemIdentifier(), "sysId");
        assertEquals(doctype.isForceQuirks(), true);
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTag";
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");

        startTag.reset();

        assertEquals(startTag.tagName, null);
        assertEquals(startTag.normalName, null);
        assertEquals(startTag.pendingAttributeName, null);
        assertEquals(startTag.pendingAttributeValue.toString(), "");
        assertEquals(startTag.pendingAttributeValueS, null);
        assertEquals(startTag.hasEmptyAttributeValue, false);
        assertEquals(startTag.hasPendingAttributeValue, false);
        assertEquals(startTag.selfClosing, false);
        assertEquals(startTag.attributes, null);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");

        startTag.nameAttr("testTag", attributes);

        assertEquals(startTag.tagName, "testTag");
        assertEquals(startTag.normalName, "testtag");
        assertEquals(startTag.attributes, attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        startTag.nameAttr("testTag", attributes);

        assertEquals(startTag.toString(), "<testTag attr1=\"value1\" attr2=\"value2\">");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "testTag";

        assertEquals(endTag.toString(), "</testTag>");
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.bogus = true;
        comment.data.append("test comment");

        comment.reset();

        assertEquals(comment.bogus, false);
        assertEquals(comment.data.toString(), "");
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");

        assertEquals(comment.getData(), "test comment");
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");

        assertEquals(comment.toString(), "<!--test comment-->");
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test character";

        character.reset();

        assertEquals(character.data, null);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "test character";

        assertEquals(character.getData(), "test character");
    }
    
    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("test cdata");

        assertEquals(cdata.toString(), "<![CDATA[test cdata]]>");
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertEquals(eof.reset(), eof);
    }
}