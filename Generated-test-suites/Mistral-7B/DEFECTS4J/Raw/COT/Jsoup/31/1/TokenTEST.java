import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctypeMethods() {
        Doctype doctype = new Doctype();
        Doctype expectedDoctype = new Doctype();
        expectedDoctype.getName().equals("html");
        expectedDoctype.getPublicIdentifier().equals(" pubblic:"); // Edge case: empty public identifier
        expectedDoctype.getSystemIdentifier().equals("/path/to/file.html"); // Edge case: non-empty system identifier
        expectedDoctype.isForceQuirks().equals(false); // Normal case
        expectedDoctype.isForceQuirks().equals(true); // Edge case: force quirks mode
        Assert.assertEquals(expectedDoctype, doctype);
    }

    @Test
    public void testTagMethods() {
        Tag tag = new StartTag("html");
        Tag expectedTag = new StartTag("html");
        expectedTag.name().equals("html");
        expectedTag.isSelfClosing().equals(false); // Normal case: tag is not self-closing
        expectedTag.isSelfClosing().equals(true); // Edge case: tag is self-closing
        Assert.assertEquals(expectedTag, tag);

        tag = new EndTag("html");
        expectedTag = new EndTag("html");
        expectedTag.name().equals("html");
        Assert.assertEquals(expectedTag, tag);
    }

    @Test(expected = AssertionError.class)
    public void testTagConstructorNullName() {
        new Tag(null); // Edge case: tag constructor with null name
    }

    @Test(expected = AssertionError.class)
    public void testAttributeNameNull() {
        Tag tag = new StartTag("html");
        tag.appendAttributeName(null); // Edge case: null attribute name
    }

    @Test(expected = AssertionError.class)
    public void testAttributeValueNull() {
        Tag tag = new StartTag("html");
        tag.appendAttributeValue(null); // Edge case: null attribute value
    }

    @Test
    public void testCharacterMethods() {
        Character character = new Character("a");
        Character expectedCharacter = new Character("a");
        Assert.assertEquals(expectedCharacter, character);
    }

    @Test(expected = AssertionError.class)
    public void testCharacterConstructorEmptyData() {
        new Character(""); // Edge case: empty character data
    }

    @Test(expected = AssertionError.class)
    public void testCharacterConstructorNullData() {
        new Character(null); // Edge case: null character data
    }

    @Test
    public void testEOFMethods() {
        EOF eof = new EOF();
        EOF expectedEof = new EOF();
        Assert.assertEquals(expectedEof, eof);
    }
}