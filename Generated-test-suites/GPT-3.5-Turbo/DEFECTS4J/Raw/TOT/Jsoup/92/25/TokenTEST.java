import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.Tag tag;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cdata;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        tag = new Token.Tag();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctype_reset() {
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.0//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/REC-html40/strict.dtd");
        doctype.systemIdentifier.append("file://path/to/dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertSame(null, doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testTag_reset() {
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("container");
        tag.pendingAttributeValueS = "container";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;

        tag.reset();

        assertSame(null, tag.tagName);
        assertSame(null, tag.normalName);
        assertSame(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertSame(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.selfClosing);
        assertSame(null, tag.attributes);
    }

    @Test
    public void testStartTag_reset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = "container";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = null;

        startTag.reset();

        assertSame(null, startTag.tagName);
        assertSame(null, startTag.normalName);
        assertSame(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertSame(null, startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertSame(null, startTag.attributes);
    }

    @Test
    public void testEndTag_reset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "class";
        endTag.pendingAttributeValue.append("container");
        endTag.pendingAttributeValueS = "container";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = null;

        endTag.reset();

        assertSame(null, endTag.tagName);
        assertSame(null, endTag.normalName);
        assertSame(null, endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertSame(null, endTag.pendingAttributeValueS);
        assertEquals(false, endTag.hasEmptyAttributeValue);
        assertEquals(false, endTag.hasPendingAttributeValue);
        assertEquals(false, endTag.selfClosing);
        assertSame(null, endTag.attributes);
    }

    @Test
    public void testComment_reset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacter_reset() {
        character.data = "Hello, World!";

        character.reset();

        assertSame(null, character.data);
    }

    @Test
    public void testCData_reset() {
        cdata.data = "CDATA";

        cdata.reset();

        assertSame(null, cdata.data);
    }

    @Test
    public void testEOF_reset() {
        eof.reset();

        assertSame(eof, eof.reset());
    }

}