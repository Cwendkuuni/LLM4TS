import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype createDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "public";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;
        return doctype;
    }

    private Token.Tag createStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myDiv");
        startTag.attributes.put("class", "myClass");
        return startTag;
    }

    private Token.Tag createEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        return endTag;
    }

    private Token.Comment createComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = false;
        return comment;
    }

    private Token.Character createCharacter() {
        Token.Character character = new Token.Character();
        character.data("This is a character");
        return character;
    }

    private Token.CData createCData() {
        Token.CData cdata = new Token.CData("This is a CDATA");
        return cdata;
    }

    private Token.EOF createEOF() {
        return new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = createDoctype();
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = createStartTag();
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = createEndTag();
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = createComment();
        comment.reset();
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = createCharacter();
        character.reset();
        assertNull(character.data);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = createDoctype();
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = createDoctype();
        assertEquals("public", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = createDoctype();
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = createDoctype();
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = createDoctype();
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = createStartTag();
        assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = createStartTag();
        assertEquals("div", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = createStartTag();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = createStartTag();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = createEndTag();
        assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = createComment();
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = createCharacter();
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = createCData();
        assertEquals("<![CDATA[This is a CDATA]]>", cdata.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = createEOF();
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = createDoctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = createStartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = createEndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = createComment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = createCharacter();
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token.CData cdata = createCData();
        assertTrue(cdata.isCData());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = createEOF();
        assertTrue(eof.isEOF());
    }

    

    //Add your additional test cases here for edge cases and exception handling scenarios

    
}