package org.jsoup.parser;
import org.junit.Test;
public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assert token.tokenType().equals("Token");
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype docType = new Token.Doctype();
        docType.name.append("HTML");
        docType.publicIdentifier.append("public");
        docType.systemIdentifier.append("system");
        docType.forceQuirks = true;
        docType.reset();
        assert docType.getName().equals("");
        assert docType.getPublicIdentifier().equals("");
        assert docType.getSystemIdentifier().equals("");
        assert docType.isForceQuirks() == false;
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.normalName = "tag";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        startTag.reset();
        assert startTag.tagName == null;
        assert startTag.normalName == null;
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.pendingAttributeValueS == null;
        assert startTag.hasEmptyAttributeValue == false;
        assert startTag.hasPendingAttributeValue == false;
        assert startTag.selfClosing == false;
        assert startTag.attributes == null;

        endTag.reset();
        assert endTag.tagName == null;
        assert endTag.normalName == null;
        assert endTag.pendingAttributeName == null;
        assert endTag.pendingAttributeValue.length() == 0;
        assert endTag.pendingAttributeValueS == null;
        assert endTag.hasEmptyAttributeValue == false;
        assert endTag.hasPendingAttributeValue == false;
        assert endTag.selfClosing == false;
        assert endTag.attributes == null;
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "Tag";
        startTag.normalName = "tag";
        assert startTag.name().equals("Tag");
        assert startTag.normalName().equals("tag");
    }

    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = null;
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.hasPendingAttributeValue = true;
        startTag.newAttribute();
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.hasPendingAttributeValue == false;
        assert startTag.attributes.size() == 1;
        assert startTag.attributes.get("attr").equals("value");

        startTag.attributes = null;
        startTag.pendingAttributeName = "empty";
        startTag.hasEmptyAttributeValue = true;
        startTag.newAttribute();
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.hasEmptyAttributeValue == false;
        assert startTag.attributes.size() == 1;
        assert startTag.attributes.get("empty").equals("");
    }

    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = null;
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.hasPendingAttributeValue = true;
        startTag.finaliseTag();
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.hasPendingAttributeValue == false;
        assert startTag.attributes.size() == 1;

        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.hasPendingAttributeValue = true;
        startTag.finaliseTag();
        assert startTag.pendingAttributeName == null;
        assert startTag.pendingAttributeValue.length() == 0;
        assert startTag.hasPendingAttributeValue == false;
        assert startTag.attributes.size() == 2;
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");

        assert startTag.toString().equals("<tag attr1=\"value1\" attr2=\"value2\">");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        assert endTag.toString().equals("</tag>");
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        comment.reset();
        assert comment.getData().equals("");
        assert comment.bogus == false;
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        character.reset();
        assert character.getData() == null;
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assert eof == eof;
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token();
        token.type = Token.TokenType.Doctype;
        assert token.isDoctype();

        Token token2 = new Token();
        token2.type = Token.TokenType.StartTag;
        assert !token2.isDoctype();
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        assert token.isStartTag();

        Token token2 = new Token();
        token2.type = Token.TokenType.Doctype;
        assert !token2.isStartTag();
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token();
        token.type = Token.TokenType.EndTag;
        assert token.isEndTag();

        Token token2 = new Token();
        token2.type = Token.TokenType.StartTag;
        assert !token2.isEndTag();
    }

    @Test
    public void testIsComment() {
        Token token = new Token();
        token.type = Token.TokenType.Comment;
        assert token.isComment();

        Token token2 = new Token();
        token2.type = Token.TokenType.StartTag;
        assert !token2.isComment();
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token();
        token.type = Token.TokenType.Character;
        assert token.isCharacter();

        Token token2 = new Token();
        token2.type = Token.TokenType.StartTag;
        assert !token2.isCharacter();
    }

    @Test
    public void testIsEOF() {
        Token token = new Token();
        token.type = Token.TokenType.EOF;
        assert token.isEOF();

        Token token2 = new Token();
        token2.type = Token.TokenType.StartTag;
        assert !token2.isEOF();
    }

}