import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "public -1";
        doctype.publicIdentifier.append("ID");
        doctype.systemIdentifier.append("/path/to/file.html");
        doctype.forceQuirks = true;

        Token expected = doctype;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getPubSysKey(), actual.getPubSysKey());
        Assertions.assertEquals(expected.getPublicIdentifier(), actual.getPublicIdentifier());
        Assertions.assertEquals(expected.getSystemIdentifier(), actual.getSystemIdentifier());
        Assertions.assertEquals(expected.isForceQuirks(), actual.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("html", null);
        startTag.newAttribute();
        startTag.appendAttributeName("lang");
        startTag.appendAttributeValue("en");

        Token expected = startTag;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected.normalName(), actual.normalName());
        Assertions.assertTrue(expected.isSelfClosing()); // assume StartTag is self-closing for this test
        Assertions.assertFalse(expected.hasAttributes()); // assume no attributes for this test
        Assertions.assertNull(expected.getAttributes());
        Assertions.assertEquals("html", expected.name());
        Assertions.assertEquals("html", expected.normalName());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("lang", "en"));

        expected.nameAttr("body", attributes);

        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.nameAttr("html", null);

        Token expected = endTag;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected.normalName(), actual.normalName());
        Assertions.assertFalse(expected.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");

        Token expected = comment;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("&copy; 2022 MyCompany");

        Token expected = character;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
        Assertions.assertEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testEOFToken() {
        EOF eof = new EOF();

        Token expected = eof;
        Token actual = new TokenParser().parse(); // assume we have a TokenParser here

        Assertions.assertEquals(expected.tokenType(), actual.tokenType());
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.type, actual.type);
    }

    @Test
    public void testResetTokens() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        // Test Doctype
        doctype.name.append("html");
        doctype.reset();
        Assertions.assertFalse(doctype.getName().hasLength());

        // Test StartTag
        startTag.nameAttr("html", null);
        startTag.newAttribute();
        startTag.appendAttributeName("lang");
        startTag.appendAttributeValue("en");
        startTag.reset();
        Assertions.assertFalse(startTag.hasAttributes());

        // Test Comment
        comment.data.append("This is a comment");
        comment.reset();
        Assertions.assertFalse(comment.getData().hasLength());

        // Test Character
        character.data("&copy; 2022 MyCompany");
        character.reset();
        Assertions.assertFalse(character.getData().hasLength());

        // Test EOF
        eof.reset();
        Assertions.assertTrue(eof.isEOF());
    }
}