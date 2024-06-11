import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            {
                type = TokenType.Doctype;
            }
        };

        String expected = "Doctype";

        assertEquals(expected, token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        String expected = "html";

        assertEquals(expected, doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//public");

        String expected = "//public";

        assertEquals(expected, doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("//system");

        String expected = "//system";

        assertEquals(expected, doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        boolean expected = true;

        assertEquals(expected, doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            {
                attributes = new Attributes();
                pendingAttributeName = "name";
                pendingAttributeValue = new StringBuilder("value");
            }
        };

        tag.newAttribute();

        assertEquals(1, tag.attributes.size());
        assertEquals("name", tag.attributes.get("name"));
        assertNull(tag.pendingAttributeName);
        assertEquals(0, tag.pendingAttributeValue.length());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            {
                pendingAttributeName = "name";
                pendingAttributeValue = new StringBuilder("value");
            }
        };

        tag.finaliseTag();

        assertEquals(1, tag.attributes.size());
        assertEquals("name", tag.attributes.get("name"));
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "div";

        String expected = "div";

        assertEquals(expected, tag.name());
    }

    @Test
    public void testTagNameSetter() {
        Token.Tag tag = new Token.Tag();

        String expected = "div";

        tag.name(expected);

        assertEquals(expected, tag.tagName);
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        tag.selfClosing = true;

        boolean expected = true;

        assertEquals(expected, tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.Tag();
        tag.attributes = new Attributes();

        Attributes expected = new Attributes();

        assertEquals(expected, tag.getAttributes());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "container");

        String expected = "<div id=\"container\">";

        assertEquals(expected, startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag("div");

        String expected = "<div>";

        assertEquals(expected, startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");

        String expected = "</div>";

        assertEquals(expected, endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        String expected = "This is a comment";

        assertEquals(expected, comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        String expected = "<!--This is a comment-->";

        assertEquals(expected, comment.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Character data");

        String expected = "Character data";

        assertEquals(expected, character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("Character data");

        String expected = "Character data";

        assertEquals(expected, character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token() {
            {
                type = TokenType.Doctype;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;

        Token.Doctype expected = doctype;

        assertEquals(expected, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token() {
            {
                type = TokenType.StartTag;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;

        Token.StartTag expected = startTag;

        assertEquals(expected, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token() {
            {
                type = TokenType.EndTag;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;

        Token.EndTag expected = endTag;

        assertEquals(expected, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token() {
            {
                type = TokenType.Comment;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;

        Token.Comment expected = comment;

        assertEquals(expected, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token() {
            {
                type = TokenType.Character;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("Character data");
        Token token = character;

        Token.Character expected = character;

        assertEquals(expected, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token() {
            {
                type = TokenType.EOF;
            }
        };

        boolean expected = true;

        assertEquals(expected, token.isEOF());
    }

}