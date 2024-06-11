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
        token.type = Token.TokenType.Doctype;
        assert(token.tokenType().equals("Token")); //basic test case for tokenType method
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assert(doctype.name.toString().equals(""));
        assert(doctype.publicIdentifier.toString().equals(""));
        assert(doctype.systemIdentifier.toString().equals(""));
        assert(!doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Tag reset() {
                return null;
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
        tag.attributes = new org.jsoup.nodes.Attributes();

        tag.reset();

        assert(tag.tagName == null);
        assert(tag.normalName == null);
        assert(tag.pendingAttributeName == null);
        assert(tag.pendingAttributeValue.toString().equals(""));
        assert(tag.pendingAttributeValueS == null);
        assert(!tag.hasEmptyAttributeValue);
        assert(!tag.hasPendingAttributeValue);
        assert(!tag.selfClosing);
        assert(tag.attributes == null);
    }

    @Test
    public void testStartTagCreation() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag").nameAttr("name", new org.jsoup.nodes.Attributes());
        assert(startTag.toString().equals("<tag>"));
    }

    @Test
    public void testEndTagCreation() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");
        assert(endTag.toString().equals("</tag>"));
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assert(comment.data.toString().equals(""));
        assert(!comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("data");

        character.reset();

        assert(character.data == null);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assert(eof == eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        Doctype doctype = new Doctype();
        token.type = Token.TokenType.Doctype;
        assert(token.isDoctype());
        assert(!token.isStartTag());
        assert(!token.isEndTag());
        assert(!token.isComment());
        assert(!token.isCharacter());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        StartTag startTag = new StartTag();
        token.type = Token.TokenType.StartTag;
        assert(!token.isDoctype());
        assert(token.isStartTag());
        assert(!token.isEndTag());
        assert(!token.isComment());
        assert(!token.isCharacter());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        EndTag endTag = new EndTag();
        token.type = Token.TokenType.EndTag;
        assert(!token.isDoctype());
        assert(!token.isStartTag());
        assert(token.isEndTag());
        assert(!token.isComment());
        assert(!token.isCharacter());
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        Comment comment = new Comment();
        token.type = Token.TokenType.Comment;
        assert(!token.isDoctype());
        assert(!token.isStartTag());
        assert(!token.isEndTag());
        assert(token.isComment());
        assert(!token.isCharacter());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        Character character = new Character();
        token.type = Token.TokenType.Character;
        assert(!token.isDoctype());
        assert(!token.isStartTag());
        assert(!token.isEndTag());
        assert(!token.isComment());
        assert(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        Token.EOF eof = new Token.EOF();
        token.type = Token.TokenType.EOF;
        assert(!token.isDoctype());
        assert(!token.isStartTag());
        assert(!token.isEndTag());
        assert(!token.isComment());
        assert(!token.isCharacter());
        assert(token.isEOF());
    }
}