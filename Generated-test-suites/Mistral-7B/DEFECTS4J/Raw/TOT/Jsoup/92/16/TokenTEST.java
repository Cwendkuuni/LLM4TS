import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        
        doctype.getName("HTML");
        assertEquals("HTML", doctype.getName());
        
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        
        startTag.name("div");
        assertEquals("div", startTag.name());
        assertEquals("Div", startTag.normalName());
        
        startTag.selfClosing(true);
        assertTrue(startTag.isSelfClosing());
        
        startTag.newAttribute().name("class").value("test");
        assertEquals("class", startTag.pendingAttributeName());
        assertEquals("test", startTag.getAttributes().getValue("class"));
        
        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName());
        assertNull(startTag.getAttributes().getValue("nonexistent"));
        
        startTag.finaliseTag();
        assertNull(startTag.pendingAttributeName());
        assertNull(startTag.getAttributes().getValue("nonexistent"));
        
        startTag.reset();
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        
        endTag.name("div");
        assertEquals("div", endTag.name());
        
        assertFalse(endTag.isSelfClosing());
        
        endTag.finaliseTag();
        assertEquals("/div", endTag.toString());
        
        endTag.reset();
        
        assertNull(endTag.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        
        comment.data("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());
        
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType());
        
        character.data("!");
        assertEquals("!", character.getData());
        assertEquals("!", character.toString());
        
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCDataToken() {
        Token cData = new Token.CData("<!-- Test CData --><![CDATA[Test CData]]>");
        assertEquals("Character", cData.tokenType());
        
        assertEquals("Test CData", cData.getData());
        assertEquals("<![CDATA[Test CData]]>", cData.toString());
        
        cData.reset();
        assertNull(cData.getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        
        assertNull(eof.getName());
        assertNull(eof.getAttributes());
        assertNull(eof.getData());
        
        assertFalse(eof.isSelfClosing());
        assertFalse(eof.hasAttributes());
        
        assertFalse(eof.hasPendingAttributeName());
        assertFalse(eof.hasPendingAttributeValue());
        assertFalse(eof.hasEmptyAttributeValue());
        
        assertFalse(eof.isForceQuirks());
    }
}