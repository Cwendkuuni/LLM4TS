import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        String expectedName = "html";
        doctype.name(expectedName);
        String actualName = doctype.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.name(name);
        String expectedName = "html";
        doctype.reset();
        String actualName = doctype.getName();
        assertEquals(expectedName, actualName);
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeNameNull() {
        Doctype doctype = new Doctype();
        doctype.name(null);
    }

    @Test
    public void testDoctypeWithPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.pubSysKey = "public";
        doctype.publicIdentifier.append("identifier");
        String expectedPublicIdentifier = "identifier";
        String actualPublicIdentifier = doctype.getPublicIdentifier();
        assertEquals(expectedPublicIdentifier, actualPublicIdentifier);
    }

    @Test
    public void testDoctypeResetWithPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.pubSysKey = "public";
        doctype.publicIdentifier.append("identifier");
        String expectedPublicIdentifier = "identifier";
        doctype.reset();
        String actualPublicIdentifier = doctype.getPublicIdentifier();
        assertEquals(expectedPublicIdentifier, actualPublicIdentifier);
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        String expectedTagName = "div";
        tag.name(expectedTagName);
        String actualTagName = tag.name();
        assertEquals(expectedTagName, actualTagName);
    }

    @Test
    public void testTagReset() {
        Tag tag = new Tag();
        String expectedTagName = "div";
        tag.name(expectedTagName);
        tag.reset();
        String actualTagName = tag.name();
        assertEquals(expectedTagName, actualTagName);
    }

    @Test(expected = AssertionError.class)
    public void testTagNameNull() {
        Tag tag = new Tag();
        tag.name(null);
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        String expectedTagName = "p";
        startTag.name(expectedTagName);
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("class", "red");
        startTag.attributes = expectedAttributes;
        String actualTagName = startTag.name();
        Attributes actualAttributes = startTag.getAttributes();
        assertEquals(expectedTagName, actualTagName);
        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        String expectedTagName = "p";
        startTag.name(expectedTagName);
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("class", "red");
        startTag.attributes = expectedAttributes;
        startTag.reset();
        String actualTagName = startTag.name();
        Attributes actualAttributes = startTag.getAttributes();
        assertEquals(expectedTagName, actualTagName);
        assertNull(actualAttributes);
    }

    @Test(expected = AssertionError.class)
    public void testStartTagNameNull() {
        StartTag startTag = new StartTag();
        startTag.name(null);
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        String expectedTagName = "p";
        endTag.name(expectedTagName);
        String actualTagName = endTag.name();
        assertEquals(expectedTagName, actualTagName);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        String expectedTagName = "p";
        endTag.name(expectedTagName);
        endTag.reset();
        String actualTagName = endTag.name();
        assertEquals(expectedTagName, actualTagName);
    }

    @Test(expected = AssertionError.class)
    public void testEndTagNameNull() {
        EndTag endTag = new EndTag();
        endTag.name(null);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        String expectedData = "<script src=\"script.js\"></script>";
        comment.data(expectedData);
        String actualData = comment.getData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        String expectedData = "<script src=\"script.js\"></script>";
        comment.data(expectedData);
        comment.reset();
        String actualData = comment.getData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = AssertionError.class)
    public void testCommentDataNull() {
        Comment comment = new Comment();
        comment.data(null);
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        String expectedData = "!";
        character.data(expectedData);
        String actualData = character.getData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        String expectedData = "!";
        character.data(expectedData);
        character.reset();
        String actualData = character.getData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = AssertionError.class)
    public void testCharacterDataNull() {
        Character character = new Character();
        character.data(null);
    }

    @Test
    public void testCData() {
        String expectedData = "<![CDATA[hello world]]>";
        CData cData = new CData(expectedData);
        String actualData = cData.getData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testCDataReset() {
        String expectedData = "<![CDATA[hello world]]>";
        CData cData = new CData(expectedData);
        cData.reset();
        String actualData = cData.getData();
        assertEquals(expectedData, actualData);
    }

    @Test(expected = AssertionError.class)
    public void testCDataDataNull() {
        CData cData = new CData(null);
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
        eof.reset();
        assertFalse(eof.isEOF());
    }

    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("Hello World");
        Token.reset(sb);
        assertEquals("", sb.toString());
    }

}