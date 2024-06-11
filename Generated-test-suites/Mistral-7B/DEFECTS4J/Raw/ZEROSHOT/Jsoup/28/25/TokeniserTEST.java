import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    private static final String TEST_HTML = "<html><head><title>Test</title></head><body><script type=\"text/javascript\">function test() { alert('Hello World!'); }</script></body></html>";

    @Before
    public void setUp() {
        reader = new CharacterReader(new ByteArrayInputStream(TEST_HTML.getBytes()));
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadCharacters() {
        // Test reading characters
        Character char1 = tokeniser.read();
        Assert.assertEquals("Expected character 'T' but was '&', position: 0", 'T', char1.character);
        Character char2 = tokeniser.read();
        Assert.assertEquals("Expected character 'h' but was '<', position: 1", 'h', char2.character);
        Character char3 = tokeniser.read();
        Assert.assertEquals("Expected character 't' but was 'm', position: 2", 't', char3.character);
        Character char4 = tokeniser.read();
        Assert.assertEquals("Expected character 'h' but was 'l', position: 3", 'l', char4.character);
        Character char5 = tokeniser.read();
        Assert.assertEquals("Expected character 'e' but was '/', position: 4", 'e', char5.character);

        // Test reading end of file
        Character char6 = tokeniser.read();
        Assert.assertNull("Expected null but was character, position: 5", char6);
    }

    @Test
    public void testReadStartTag() {
        // Test reading start tag
        StartTag startTag1 = tokeniser.read();
        Assert.assertEquals("Expected tag 'html' but was 'T', position: 0", "html", startTag1.tagName);
        Assert.assertFalse("Expected start tag to not be self-closing", startTag1.isSelfClosing());

        StartTag startTag2 = tokeniser.read();
        Assert.assertEquals("Expected tag 'head' but was 'h', position: 7", "head", startTag2.tagName);
        Assert.assertFalse("Expected start tag to not be self-closing", startTag2.isSelfClosing());

        StartTag startTag3 = tokeniser.read();
        Assert.assertEquals("Expected tag 'title' but was 't', position: 16", "title", startTag3.tagName);
        Assert.assertFalse("Expected start tag to not be self-closing", startTag3.isSelfClosing());
    }

    @Test
    public void testReadScriptTag() {
        // Test reading script tag
        StartTag startTag1 = tokeniser.read();
        Assert.assertEquals("Expected tag 'script' but was 'S', position: 22", "script", startTag1.tagName);
        Assert.assertTrue("Expected start tag to be self-closing", startTag1.isSelfClosing());
    }

    @Test
    public void testReadEndTag() {
        // Test reading end tag
        EndTag endTag1 = tokeniser.read();
        Assert.assertEquals("Expected tag 'html' but was '<', position: 45", "html", endTag1.tagName);
        Assert.assertNull("Expected no attributes but was attribute list", endTag1.attributes);

        EndTag endTag2 = tokeniser.read();
        Assert.assertEquals("Expected tag 'head' but was '/', position: 49", "head", endTag2.tagName);
        Assert.assertNull("Expected no attributes but was attribute list", endTag2.attributes);

        EndTag endTag3 = tokeniser.read();
        Assert.assertEquals("Expected tag 'body' but was '/', position: 56", "body", endTag3.tagName);
        Assert.assertNull("Expected no attributes but was attribute list", endTag3.attributes);
    }

    @Test
    public void testReadComment() {
        // Test reading comment
        tokeniser.read(); // Read <--!
        tokeniser.read(); // Read comment marker -->
        Comment comment = (Comment) tokeniser.read();
        Assert.assertEquals("Expected comment 'Test' but was 'T', position: 12", "Test", comment.data);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadInvalidCharacterReference() {
        // Test reading invalid character reference
        tokeniser.read(); // Read '&'
        tokeniser.read(); // Read 'A'
        tokeniser.read(); // Read 'M'
        tokeniser.read(); // Read 'P'
        tokeniser.read(); // Read ';'
        tokeniser.read(); // Read ';' // Unexpected ';'
        tokeniser.read(); // Read 'S'
    }

    @Test
    public void testReadCharacterReference() {
        // Test reading character reference
        Character character1 = tokeniser.read();
        Assert.assertEquals("Expected character '&' but was '&', position: 28", '&', character1.character);

        Character character2 = tokeniser.read();
        Assert.assertEquals("Expected character '#' but was 'n', position: 29", '#', character2.character);

        Character character3 = tokeniser.read();
        Assert.assertEquals("Expected character 'a' but was 'a', position: 29", 'a', character3.character);

        Character character4 = tokeniser.read();
        Assert.assertEquals("Expected character 'm' but was 'p', position: 30", 'p', character4.character);

        Character character5 = tokeniser.read();
        Assert.assertEquals("Expected character ';' but was ';', position: 30", ';', character5.character);

        Assert.assertEquals("Expected character '&' but was 'T', position: 33", 'T', tokeniser.read().character);
        Assert.assertEquals("Expected character 'h' but was 'i', position: 34", 'i', tokeniser.read().character);
        Assert.assertEquals("Expected character 't' but was 'l', position: 35", 'l', tokeniser.read().character);
        Assert.assertEquals("Expected character 'e' but was ';', position: 36", ';', tokeniser.read().character);

        Character character6 = tokeniser.read();
        Assert.assertEquals("Expected character '&' but was '/', position: 37", '/', character6.character);
        Assert.assertEquals("Expected character 'q' but was 'o', position: 38", 'o', tokeniser.read().character);
        Assert.assertEquals("Expected character 't' but was 't', position: 39", 't', tokeniser.read().character);
        Assert.assertEquals("Expected character ';' but was ';', position: 40", ';', tokeniser.read().character);
    }

    @Test
    public void testReadEntityReference() {
        // Test reading entity reference
        Character character1 = tokeniser.read();
        Assert.assertEquals("Expected character '&' but was '&', position: 43", '&', character1.character);

        Character character2 = tokeniser.read();
        Assert.assertEquals("Expected character '#' but was 'E', position: 44", '#', character2.character);

        Character character3 = tokeniser.read();
        Assert.assertEquals("Expected character 'n' but was 'd', position: 44", 'd', character3.character);

        Character character4 = tokeniser.read();
        Assert.assertEquals("Expected character 'q' but was 'o', position: 45", 'o', character4.character);

        Character character5 = tokeniser.read();
        Assert.assertEquals("Expected character ';' but was ';', position: 45", ';', character5.character);

        Assert.assertEquals("Expected character '<' but was 'T', position: 46", '<', tokeniser.read().character);
        Assert.assertEquals("Expected character 'h' but was 'i', position: 47", 'i', tokeniser.read().character);
        Assert.assertEquals("Expected character 't' but was 'l', position: 48", 'l', tokeniser.read().character);
        Assert.assertEquals("Expected character 'e' but was ';', position: 49", ';', tokeniser.read().character);
    }

    @Test(expected = AssertionError.class)
    public void testErrorHandling() {
        // Test error handling
        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '>'

        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read '>' // Missing '>' before '/'

        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '>'
        tokeniser.read(); // Read '<' // Unexpected '<'
    }

    @Test(expected = AssertionError.class)
    public void testEOFErrorHandling() {
        // Test EOF error handling
        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 'h'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read 'm'
        tokeniser.read(); // Read 'l'

        // Read past end of file
        tokeniser.read(); // Expected character 'l' but was EOF, position: 60
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test checking appropriate end tag token
        Assert.assertFalse("Expected false but was true, position: 0", tokeniser.isAppropriateEndTagToken());

        tokeniser.emitTagPending();
        Assert.assertTrue("Expected true but was false, position: 13", tokeniser.isAppropriateEndTagToken());

        tokeniser.emitEndTag("html");
        Assert.assertFalse("Expected false but was true, position: 45", tokeniser.isAppropriateEndTagToken());

        tokeniser.emitTagPending();
        Assert.assertTrue("Expected true but was false, position: 50", tokeniser.isAppropriateEndTagToken());

        tokeniser.emitEndTag("head");
        Assert.assertFalse("Expected false but was true, position: 56", tokeniser.isAppropriateEndTagToken());

        tokeniser.emitTagPending();
        Assert.assertTrue("Expected true but was false, position: 59", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testErrorReporting() {
        // Test error reporting
        List<ParseError> errors = new ArrayList<>();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '>'

        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read '>' // Missing '>' before '/'

        tokeniser.read(); // Read '<'
        tokeniser.read(); // Read '/'
        tokeniser.read(); // Read 's'
        tokeniser.read(); // Read 'c'
        tokeniser.read(); // Read 'r'
        tokeniser.read(); // Read 'i'
        tokeniser.read(); // Read 'p'
        tokeniser.read(); // Read 't'
        tokeniser.read(); // Read '>'
        tokeniser.read(); // Read '<' // Unexpected '<'

        Assert.assertEquals("Expected 3 errors but was 0", 3, errors.size());

        Assert.assertEquals("Expected error message 'Unexpected character '<', position: 61' but was different",
                "Unexpected character '<', position: 61", errors.get(0).getMessage());

        Assert.assertEquals("Expected error message 'Unexpected character '/', position: 63' but was different",
                "Unexpected character '/', position: 63", errors.get(1).getMessage());

        Assert.assertEquals("Expected error message 'Unexpected character '<', position: 65' but was different",
                "Unexpected character '<', position: 65", errors.get(2).getMessage());
    }
}