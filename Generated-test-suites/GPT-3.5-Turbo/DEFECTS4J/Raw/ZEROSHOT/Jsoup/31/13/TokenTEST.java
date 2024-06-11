import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    // Test tokenType method
    @Test
    public void testTokenType() {
        Token token = new Token() {};
        String expected = "Token";
        String actual = token.tokenType();
        assertEquals(expected, actual);
    }
    
    // Test getName method of Doctype class
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        String expected = "HTML";
        String actual = doctype.getName();
        assertEquals(expected, actual);
    }
    
    // Test getPublicIdentifier method of Doctype class
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        String expected = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String actual = doctype.getPublicIdentifier();
        assertEquals(expected, actual);
    }
    
    // Test getSystemIdentifier method of Doctype class
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        String expected = "http://www.w3.org/TR/html4/loose.dtd";
        String actual = doctype.getSystemIdentifier();
        assertEquals(expected, actual);
    }
    
    // Test isForceQuirks method of Doctype class
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        boolean expected = true;
        boolean actual = doctype.isForceQuirks();
        assertEquals(expected, actual);
    }
    
    // Test name method of Tag class
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = "div";
        String expected = "div";
        String actual = tag.name();
        assertEquals(expected, actual);
    }
    
    // Test isSelfClosing method of Tag class
    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag() {};
        tag.selfClosing = true;
        boolean expected = true;
        boolean actual = tag.isSelfClosing();
        assertEquals(expected, actual);
    }
    
    // Test getAttributes method of Tag class
    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.Tag() {};
        tag.attributes = new org.jsoup.nodes.Attributes();
        int expected = 0;
        int actual = tag.getAttributes().size();
        assertEquals(expected, actual);
    }
    
    // Test appendTagName method of Tag class
    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendTagName("p");
        String expected = "p";
        String actual = tag.tagName;
        assertEquals(expected, actual);
    }
    
    // Test appendAttributeName method of Tag class
    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeName("class");
        String expected = "class";
        String actual = tag.pendingAttributeName;
        assertEquals(expected, actual);
    }
    
    // Test appendAttributeValue method of Tag class
    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag() {};
        tag.appendAttributeValue("container");
        String expected = "container";
        String actual = tag.pendingAttributeValue.toString();
        assertEquals(expected, actual);
    }
    
    // Test toString method of StartTag class
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("div");
        String expected = "<div>";
        String actual = startTag.toString();
        assertEquals(expected, actual);
    }
    
    // Test toString method of EndTag class
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");
        String expected = "</div>";
        String actual = endTag.toString();
        assertEquals(expected, actual);
    }
    
    // Test getData method of Comment class
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        String expected = "This is a comment";
        String actual = comment.getData();
        assertEquals(expected, actual);
    }
    
    // Test toString method of Comment class
    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        assertEquals(expected, actual);
    }
    
    // Test getData method of Character class
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("A");
        String expected = "A";
        String actual = character.getData();
        assertEquals(expected, actual);
    }
    
    // Test toString method of Character class
    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character("A");
        String expected = "A";
        String actual = character.toString();
        assertEquals(expected, actual);
    }
    
    // Test isDoctype method
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        boolean expected = true;
        boolean actual = token.isDoctype();
        assertEquals(expected, actual);
    }
    
    // Test asDoctype method
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype expected = (Token.Doctype) token;
        Token.Doctype actual = token.asDoctype();
        assertEquals(expected, actual);
    }
    
    // Test isStartTag method
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        boolean expected = true;
        boolean actual = token.isStartTag();
        assertEquals(expected, actual);
    }
    
    // Test asStartTag method
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag expected = (Token.StartTag) token;
        Token.StartTag actual = token.asStartTag();
        assertEquals(expected, actual);
    }
    
    // Test isEndTag method
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        boolean expected = true;
        boolean actual = token.isEndTag();
        assertEquals(expected, actual);
    }
    
    // Test asEndTag method
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag expected = (Token.EndTag) token;
        Token.EndTag actual = token.asEndTag();
        assertEquals(expected, actual);
    }
    
    // Test isComment method
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        boolean expected = true;
        boolean actual = token.isComment();
        assertEquals(expected, actual);
    }
    
    // Test asComment method
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment expected = (Token.Comment) token;
        Token.Comment actual = token.asComment();
        assertEquals(expected, actual);
    }
    
    // Test isCharacter method
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("A");
        boolean expected = true;
        boolean actual = token.isCharacter();
        assertEquals(expected, actual);
    }
    
    // Test asCharacter method
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character("A");
        Token.Character expected = (Token.Character) token;
        Token.Character actual = token.asCharacter();
        assertEquals(expected, actual);
    }
    
    // Test isEOF method
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        boolean expected = true;
        boolean actual = token.isEOF();
        assertEquals(expected, actual);
    }
}