
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Token.*;

public class TokenTest {
    
    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        
        // Typical use case
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = false;
        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
        
        // Edge case - empty values
        doctype.name.delete(0, doctype.name.length());
        doctype.publicIdentifier.delete(0, doctype.publicIdentifier.length());
        doctype.systemIdentifier.delete(0, doctype.systemIdentifier.length());
        doctype.forceQuirks = true;
        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        
        // Typical use case
        startTag.name("div");
        startTag.selfClosing = false;
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("container");
        Assert.assertEquals("div", startTag.name());
        Assert.assertFalse(startTag.isSelfClosing());
        Assert.assertEquals("class", startTag.getAttributes().get(0).getKey());
        Assert.assertEquals("container", startTag.getAttributes().get(0).getValue());
        
        // Edge case - empty name
        startTag.name("");
        Assert.assertEquals("", startTag.name());
        
        // Error scenario - attribute with null name
        startTag.newAttribute();
        startTag.appendAttributeName(null);
        Assert.assertEquals(0, startTag.getAttributes().size());
    }
    
    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        
        // Typical use case
        endTag.name("div");
        Assert.assertEquals("div", endTag.name());
        
        // Edge case - empty name
        endTag.name("");
        Assert.assertEquals("", endTag.name());
    }
    
    @Test
    public void testComment() {
        Comment comment = new Comment();
        
        // Typical use case
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
        
        // Edge case - empty comment
        comment.data.delete(0, comment.data.length());
        Assert.assertEquals("", comment.getData());
    }
    
    @Test
    public void testCharacter() {
        Character character = new Character("A");
        
        // Typical use case
        Assert.assertEquals("A", character.getData());
        
        // Edge case - empty character
        character = new Character("");
        Assert.assertEquals("", character.getData());
    }
    
    @Test
    public void testEOF() {
        EOF eof = new EOF();
        Assert.assertTrue(eof.isEOF());
    }
}
