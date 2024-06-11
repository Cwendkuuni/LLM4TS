//Import statements
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            public String toString() {
                return "";
            }
        };
        Assert.assertEquals("", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        Assert.assertEquals("test", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("test");
        Assert.assertEquals("test", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("test");
        Assert.assertEquals("test", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttributeFirstNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            @Override
            public String toString() {
                return "";
            }
        };
        tag.pendingAttributeName = "test";
        tag.attributes = null;
        tag.newAttribute();
        Assert.assertNotNull(tag.attributes);
        Assert.assertEquals("test", tag.attributes.get("test"));
        Assert.assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            @Override
            public String toString() {
                return "";
            }
        };
        tag.pendingAttributeName = "test";
        tag.pendingAttributeValue = new StringBuilder("value");
        tag.attributes = new Attributes();
        tag.newAttribute();
        Assert.assertEquals("value", tag.attributes.get("test"));
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "test";
        Assert.assertEquals("test", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.Tag();
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagNameString() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = null;
        tag.appendTagName("test");
        Assert.assertEquals("test", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = null;
        tag.appendTagName('t');
        Assert.assertEquals("t", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeNameString() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeName = null;
        tag.appendAttributeName("test");
        Assert.assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeName = null;
        tag.appendAttributeName('t');
        Assert.assertEquals("t", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValueString() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue = null;
        tag.appendAttributeValue("test");
        Assert.assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue = null;
        tag.appendAttributeValue('t');
        Assert.assertEquals("t", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals("<>", startTag.toString());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        Assert.assertEquals("test", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals("</>", endTag.toString());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        Assert.assertEquals("test", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        Assert.assertEquals("test", comment.getData());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        Assert.assertEquals("<!-->", comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("test");
        Assert.assertEquals("test", character.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("test");
        Assert.assertEquals("test", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        Assert.assertEquals(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        Assert.assertEquals(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        Assert.assertEquals(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        Assert.assertEquals(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("");
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("");
        Token token = character;
        Assert.assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeEnums() {
        Assert.assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        Assert.assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        Assert.assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        Assert.assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        Assert.assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        Assert.assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}