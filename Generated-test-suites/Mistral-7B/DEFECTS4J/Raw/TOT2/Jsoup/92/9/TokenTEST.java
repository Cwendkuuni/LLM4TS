import org.junit.Test;
import org.junit.Assert;
import static org.jsoup.parser.TokenType.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testTokenCreation() {
        // Test creation of all token types
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        CData cData = new CData("test");
        EOF eof = new EOF();

        // Assert correct token type
        Assert.assertEquals(Doctype.class, doctype.getClass());
        Assert.assertEquals(StartTag.class, startTag.getClass());
        Assert.assertEquals(EndTag.class, endTag.getClass());
        Assert.assertEquals(Comment.class, comment.getClass());
        Assert.assertEquals(Character.class, character.getClass());
        Assert.assertEquals(CData.class, cData.getClass());
        Assert.assertEquals(EOF.class, eof.getClass());

        // Test reset method
        Doctype doctypeReset = (Doctype) doctype.reset();
        Assert.assertNotSame(doctype, doctypeReset);

        // Test methods specific to Doctype token
        Doctype doctypeWithName = new Doctype();
        doctypeWithName.name("HTML");
        Assert.assertEquals("HTML", doctypeWithName.getName());
        Assert.assertEquals("HTML", doctypeWithName.tokenType());

        Doctype doctypeWithSystemIdentifier = new Doctype();
        doctypeWithSystemIdentifier.systemIdentifier("about:blank");
        Assert.assertEquals("about:blank", doctypeWithSystemIdentifier.getSystemIdentifier());

        Doctype doctypeWithForceQuirks = new Doctype();
        doctypeWithForceQuirks.isForceQuirks(true);
        Assert.assertTrue(doctypeWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testStartTagCreation() {
        // Test creation of StartTag and setting tag name
        StartTag startTag = new StartTag().name("html");
        Assert.assertEquals("html", startTag.name());
        Assert.assertEquals("HTML", startTag.normalName());
        Assert.assertEquals("html", startTag.tagName);
        Assert.assertFalse(startTag.isSelfClosing());

        // Test setting attributes
        StartTag tagWithAttributes = new StartTag().name("img").attributes().put("src", "image.jpg").put("alt", "Image").put("width", "100");
        Assert.assertEquals("img", tagWithAttributes.name());
        Assert.assertEquals("IMG", tagWithAttributes.normalName());
        Assert.assertNotNull(tagWithAttributes.getAttributes());
        Assert.assertEquals("image.jpg", tagWithAttributes.getAttributes().getValue("src"));
        Assert.assertEquals("Image", tagWithAttributes.getAttributes().getValue("alt"));
        Assert.assertEquals("100", tagWithAttributes.getAttributes().getValue("width"));

        // Test setting self-closing tag
        StartTag selfClosingTag = new StartTag().name("img").attributes().put("src", "image.jpg").selfClosing(true);
        Assert.assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEndTagCreation() {
        // Test creation of EndTag and setting tag name
        EndTag endTag = new EndTag().name("html");
        Assert.assertEquals("html", endTag.name());
        Assert.assertEquals("HTML", endTag.normalName());
        Assert.assertEquals("html", endTag.tagName);

        // Test empty tag name
        EndTag emptyEndTag = new EndTag().name("");
        Assert.assertEquals("", emptyEndTag.name());
        Assert.assertEquals(TokenType.EndTag, emptyEndTag.type);
    }

    // Add test cases for other token types and their methods here
}