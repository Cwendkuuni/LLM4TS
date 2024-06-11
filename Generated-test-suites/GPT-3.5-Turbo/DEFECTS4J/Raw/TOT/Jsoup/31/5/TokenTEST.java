
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.EOF;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testDoctype_getName() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctype_getPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctype_getSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTag_name() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTag_isSelfClosing() {
        StartTag startTag = new StartTag();
        startTag.selfClosing = true;
        
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTag_getAttributes() {
        StartTag startTag = new StartTag();
        
        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        startTag.attributes = attributes;
        
        assertEquals(attributes, startTag.getAttributes());
    }
    
    @Test
    public void testEndTag_name() {
        EndTag endTag = new EndTag();
        endTag.name("div");
        
        assertEquals("div", endTag.name());
    }
    
    @Test
    public void testComment_getData() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacter_getData() {
        Character character = new Character("This is a character");
        
        assertEquals("This is a character", character.getData());
    }
    
    @Test
    public void testEOF() {
        EOF eof = new EOF();
        
        assertTrue(eof.isEOF());
    }
}
