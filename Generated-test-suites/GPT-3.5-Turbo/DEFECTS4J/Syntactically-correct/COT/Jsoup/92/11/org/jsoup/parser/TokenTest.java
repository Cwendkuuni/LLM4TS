import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };

        token.type = Token.TokenType.Doctype;
        assert token.tokenType().equals("Doctype");

        token.type = Token.TokenType.StartTag;
        assert token.tokenType().equals("StartTag");

        token.type = Token.TokenType.EndTag;
        assert token.tokenType().equals("EndTag");

        token.type = Token.TokenType.Comment;
        assert token.tokenType().equals("Comment");

        token.type = Token.TokenType.Character;
        assert token.tokenType().equals("Character");

        token.type = Token.TokenType.EOF;
        assert token.tokenType().equals("EOF");
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.name.append("doctypename");
        doctype.pubSysKey = "testpubsyskey";
        doctype.publicIdentifier.append("testpublic");
        doctype.systemIdentifier.append("testsystem");
        doctype.forceQuirks = true;

        doctype.reset();

        assert doctype.getName().equals("");
        assert doctype.getPubSysKey() == null;
        assert doctype.getPublicIdentifier().equals("");
        assert doctype.getSystemIdentifier().equals("");
        assert !doctype.isForceQuirks();
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token reset() {
                return this;
            }
        };

        tag.tagName = "testtagname";
        tag.normalName = "testnormalname";
        tag.pendingAttributeName = "testattrname";
        tag.pendingAttributeValue.append("testattrvalue");
        tag.pendingAttributeValueS= "testattrsvalue";
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
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testtagname";
        startTag.normalName = "testnormalname";
        startTag.pendingAttributeName = "testattrname";
        startTag.pendingAttributeValue.append("testattrvalue");
        startTag.pendingAttributeValueS= "testattrsvalue";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.nameAttr("testnameattr", new Attributes());

        assert startTag.toString().equals("<testnameattr>");

        startTag.attributes.put("attrname1", "attrvalue1");
        startTag.attributes.put("attrname2", "attrvalue2");

        assert startTag.toString().equals("<testnameattr attrname1=\"attrvalue1\" attrname2=\"attrvalue2\">");
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "testtagname";

        assert endTag.toString().equals("</testtagname>");
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testcommentdata");
        comment.bogus = true;

        assert comment.getData().equals("testcommentdata");
        assert comment.toString().equals("<!--testcommentdata-->");
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.data("testcharacterdata");

        assert character.getData().equals("testcharacterdata");
        assert character.toString().equals("testcharacterdata");
    }

    @Test
    public void testCData() {
        Token.CData cdata = new Token.CData("testcdata");

        assert cdata.toString().equals("<![CDATA[testcdata]]>");
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();

        assert token.isDoctype();
        assert !token.isStartTag();
        assert !token.isEndTag();
        assert !token.isComment();
        assert !token.isCharacter();
        assert !token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();

        assert token.asDoctype() instanceof Token.Doctype;
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();

        assert !token.isDoctype();
        assert token.isStartTag();
        assert !token.isEndTag();
        assert !token.isComment();
        assert !token.isCharacter();
        assert !token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();

        assert token.asStartTag() instanceof Token.StartTag;
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();

        assert !token.isDoctype();
        assert !token.isStartTag();
        assert token.isEndTag();
        assert !token.isComment();
        assert !token.isCharacter();
        assert !token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();

        assert token.asEndTag() instanceof Token.EndTag;
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();

        assert !token.isDoctype();
        assert !token.isStartTag();
        assert !token.isEndTag();
        assert token.isComment();
        assert !token.isCharacter();
        assert !token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();

        assert token.asComment() instanceof Token.Comment;
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();

        assert !token.isDoctype();
        assert !token.isStartTag();
        assert !token.isEndTag();
        assert !token.isComment();
        assert token.isCharacter();
        assert !token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testIsCData() {
        Token token = new Token.CData("testcdata");

        assert !token.isDoctype();
        assert !token.isStartTag();
        assert !token.isEndTag();
        assert !token.isComment();
        assert !token.isCharacter();
        assert token.isCData();
        assert !token.isEOF();
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        assert !token.isDoctype();
        assert !token.isStartTag();
        assert !token.isEndTag();
        assert !token.isComment();
        assert !token.isCharacter();
        assert !token.isCData();
        assert token.isEOF();
    }
}