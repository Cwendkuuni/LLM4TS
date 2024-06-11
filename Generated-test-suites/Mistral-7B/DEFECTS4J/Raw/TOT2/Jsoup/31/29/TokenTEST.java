import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Character;
import org.jsoup.parser.Comment;
import org.jsoup.parser.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        String html = "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' SYSTEM 'http://www.w3.org/TR/html4/loose.dtd'>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isDoctype());
        Doctype doctype = (Doctype) nextToken;
        assertEquals("html", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        nextToken = tokenizer.nextToken();
        assertFalse(nextToken.isDoctype());
    }

    @Test
    public void testStartTag() {
        String html = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isStartTag());
        StartTag startTag = (StartTag) nextToken;
        assertEquals("html", startTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag headTag = (StartTag) nextToken;
        assertEquals("head", headTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isTag());
        StartTag titleTag = (StartTag) nextToken;
        assertEquals("title", titleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endTitleTag = (EndTag) nextToken;
        assertEquals("title", endTitleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag bodyTag = (StartTag) nextToken;
        assertEquals("body", bodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endBodyTag = (EndTag) nextToken;
        assertEquals("body", endBodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEOF());
        EOF eof = (EOF) nextToken;
        assertFalse(nextToken.isDoctype());
        assertFalse(nextToken.isStartTag());
        assertFalse(nextToken.isEndTag());
        assertFalse(nextToken.isComment());
        assertFalse(nextToken.isCharacter());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidStartTag() {
        String html = "<html<head>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isStartTag());
        StartTag startTag = (StartTag) nextToken;
        assertEquals("html", startTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        // Assertion error should be thrown here
        StartTag invalidTag = (StartTag) nextToken;
        assertEquals("head", invalidTag.tagName());
    }

    @Test
    public void testComment() {
        String html = "<!DOCTYPE html>\n<!-- This is a comment -->\n<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isDoctype());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isComment());
        Comment comment = (Comment) nextToken;
        assertEquals("", comment.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag htmlTag = (StartTag) nextToken;
        assertEquals("html", htmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHtmlTag = (EndTag) nextToken;
        assertEquals("html", endHtmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag headTag = (StartTag) nextToken;
        assertEquals("head", headTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHeadTag = (EndTag) nextToken;
        assertEquals("head", endHeadTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag titleTag = (StartTag) nextToken;
        assertEquals("title", titleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endTitleTag = (EndTag) nextToken;
        assertEquals("title", endTitleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag bodyTag = (StartTag) nextToken;
        assertEquals("body", bodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endBodyTag = (EndTag) nextToken;
        assertEquals("body", endBodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEOF());
        EOF eof = (EOF) nextToken;
        assertFalse(nextToken.isDoctype());
        assertFalse(nextToken.isStartTag());
        assertFalse(nextToken.isEndTag());
        assertFalse(nextToken.isComment());
        assertFalse(nextToken.isCharacter());
    }

    @Test
    public void testCharacter() {
        String html = "<html><head><title>&amp; Test</title></head><body><p>&quot;This is a test.&quot;</p></body></html>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isDoctype());

        nextToken = tokenizer.nextToken();
        assertFalse(nextToken.isDoctype());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag htmlTag = (StartTag) nextToken;
        assertEquals("html", htmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHtmlTag = (EndTag) nextToken;
        assertEquals("html", endHtmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag headTag = (StartTag) nextToken;
        assertEquals("head", headTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag titleTag = (StartTag) nextToken;
        assertEquals("title", titleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character1 = (Character) nextToken;
        assertEquals("&amp;", character1.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character2 = (Character) nextToken;
        assertEquals("&quot;", character2.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endTitleTag = (EndTag) nextToken;
        assertEquals("title", endTitleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHeadTag = (EndTag) nextToken;
        assertEquals("head", endHeadTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag bodyTag = (StartTag) nextToken;
        assertEquals("body", bodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endBodyTag = (EndTag) nextToken;
        assertEquals("body", endBodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEOF());
        EOF eof = (EOF) nextToken;
        assertFalse(nextToken.isDoctype());
        assertFalse(nextToken.isStartTag());
        assertFalse(nextToken.isEndTag());
        assertFalse(nextToken.isComment());
        assertFalse(nextToken.isCharacter());
    }

    @Test
    public void testEOF() {
        String html = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        Tokenizer tokenizer = new Tokenizer(html);
        Token nextToken = tokenizer.nextToken();

        assertTrue(nextToken.isDoctype());

        nextToken = tokenizer.nextToken();
        assertFalse(nextToken.isDoctype());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag htmlTag = (StartTag) nextToken;
        assertEquals("html", htmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHtmlTag = (EndTag) nextToken;
        assertEquals("html", endHtmlTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag headTag = (StartTag) nextToken;
        assertEquals("head", headTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endHeadTag = (EndTag) nextToken;
        assertEquals("head", endHeadTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag titleTag = (StartTag) nextToken;
        assertEquals("title", titleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character1 = (Character) nextToken;
        assertEquals("T", character1.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character2 = (Character) nextToken;
        assertEquals("e", character2.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character3 = (Character) nextToken;
        assertEquals("s", character3.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isCharacter());
        Character character4 = (Character) nextToken;
        assertEquals("t", character4.getData());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endTitleTag = (EndTag) nextToken;
        assertEquals("title", endTitleTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isStartTag());
        StartTag bodyTag = (StartTag) nextToken;
        assertEquals("body", bodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEndTag());
        EndTag endBodyTag = (EndTag) nextToken;
        assertEquals("body", endBodyTag.tagName());

        nextToken = tokenizer.nextToken();
        assertTrue(nextToken.isEOF());
        EOF eof = (EOF) nextToken;
        assertFalse(nextToken.isDoctype());
        assertFalse(nextToken.isStartTag());
        assertFalse(nextToken.isEndTag());
        assertFalse(nextToken.isComment());
        assertFalse(nextToken.isCharacter());
    }
}