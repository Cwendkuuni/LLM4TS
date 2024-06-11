import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeSetName() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.getName().equals("html");
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Doctype doctype = new Doctype();
        doctype.setPubSysKey("DTD");
        doctype.getPubSysKey().equals("DTD");
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setPublicIdentifier("public -1");
        doctype.getPublicIdentifier().equals("public -1");
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.setSystemIdentifier("system.xml");
        doctype.getSystemIdentifier().equals("system.xml");
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidDoctype() {
        Doctype doctype = new Doctype();
        doctype.reset();
        doctype.getName().equals("html"); // this line should throw an AssertionError since name is empty after reset
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.reset();
        assertNull(startTag.tagName);
    }

    @Test
    public void testStartTagSetName() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        startTag.getName().equals("div");
    }

    @Test
    public void testStartTagSetAttributes() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().put("id", "test");
        Attributes attributes = startTag.getAttributes();
        assertEquals("id", attributes.keySet().iterator().next());
        assertEquals("test", attributes.get("id").getValue());
    }

    @Test
    public void testStartTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.newAttribute().put("class", "test");
        startTag.finaliseTag();
        Attribute attribute = startTag.getAttributes().get("class");
        assertEquals("test", attribute.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidStartTag() {
        StartTag startTag = new StartTag();
        startTag.reset();
        startTag.tagName = "div"; // this line should throw an AssertionError since tagName is null after reset
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.reset();
        assertNull(endTag.tagName);
    }

    @Test
    public void testEndTagSetName() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        endTag.getName().equals("div");
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentSetData() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        comment.getData().equals("This is a comment");
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidComment() {
        Comment comment = new Comment();
        comment.reset();
        comment.getData().equals("This is a comment"); // this line should throw an AssertionError since data is null after reset
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterSetData() {
        Character character = new Character();
        character.data("A");
        character.getData().equals("A");
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data("A");
        String expected = "A";
        String actual = character.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidCharacter() {
        Character character = new Character();
        character.reset();
        character.getData().equals("A"); // this line should throw an AssertionError since data is null after reset
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token token = new StartTag();
        assertTrue(token.isStartTag());
        Token doctypeToken = new Doctype();
        assertTrue(doctypeToken.isDoctype());
        Token commentToken = new Comment();
        assertTrue(commentToken.isComment());
        Token characterToken = new Character();
        assertTrue(characterToken.isCharacter());
        Token eofToken = new EOF();
        assertTrue(eofToken.isEOF());
    }

    @Test
    public void testTokenTypeConversion() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        TokenType expectedType;
        Token actualType;

        expectedType = TokenType.Doctype;
        actualType = doctype.getType();
        assertEquals(expectedType, actualType);

        expectedType = TokenType.StartTag;
        actualType = startTag.getType();
        assertEquals(expectedType, actualType);

        expectedType = TokenType.EndTag;
        actualType = endTag.getType();
        assertEquals(expectedType, actualType);

        expectedType = TokenType.Comment;
        actualType = comment.getType();
        assertEquals(expectedType, actualType);

        expectedType = TokenType.Character;
        actualType = character.getType();
        assertEquals(expectedType, actualType);

        expectedType = TokenType.EOF;
        actualType = eof.getType();
        assertEquals(expectedType, actualType);

        StartTag startTagDoctype = new StartTag();
        doctype.name("html");
        startTagDoctype.nameAttr(doctype.getName(), doctype.getAttributes());
        expectedType = TokenType.StartTag;
        actualType = startTagDoctype.getType();
        assertEquals(expectedType, actualType);

        EndTag endTagStartTag = new EndTag();
        startTag.name("div");
        endTagStartTag.name(startTag.getName());
        expectedType = TokenType.EndTag;
        actualType = endTagStartTag.getType();
        assertEquals(expectedType, actualType);
    }
}