import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("public id", doctype.getPublicIdentifier());
        assertEquals("system id", doctype.getSystemIdentifier());
        assertEquals(true, doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        assertEquals(doctype, resetDoctype);
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.tagName.append("p");
        Attributes attributes = new Attributes();
        attributes.put("class", "someClass");
        startTag.nameAttr("class", attributes);

        assertEquals("StartTag", startTag.tokenType().toString());
        assertEquals("p", startTag.name());
        assertEquals("p", startTag.normalName());
        assertEquals("someClass", startTag.getAttributes().get("class"));

        StartTag resetStartTag = startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.getAttributes());
        assertEquals(startTag, resetStartTag);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.tagName.append("p");

        assertEquals("EndTag", endTag.tokenType().toString());
        assertEquals("p", endTag.name());
        assertEquals("p", endTag.normalName());

        EndTag resetEndTag = endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName());
        assertEquals(endTag, resetEndTag);
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");

        assertEquals("Comment", comment.tokenType().toString());
        assertEquals("This is a comment", comment.getData());

        Comment resetComment = comment.reset();
        assertNull(comment.getData());
        assertEquals(comment, resetComment);
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("&hellip;");

        assertEquals("Character", character.tokenType().toString());
        assertEquals("&hellip;", character.getData());

        Character resetCharacter = character.reset();
        assertNull(character.getData());
        assertEquals(character, resetCharacter);
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();

        assertEquals("EOF", eof.tokenType().toString());

        EOF resetEOF = eof.reset();
        assertEquals(eof, resetEOF);
    }
}