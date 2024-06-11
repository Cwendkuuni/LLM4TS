import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetMethods() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.name(name);
        doctype.getName().equals(name);
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeSetForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.isForceQuirks();
        doctype.isForceQuirks(true);
        doctype.isForceQuirks();
        doctype.reset();
        doctype.isForceQuirks();
        doctype.isForceQuirks(false);
        doctype.isForceQuirks();
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeNullName() {
        Doctype doctype = new Doctype();
        doctype.getName().equals(null);
        doctype.name(null);
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = null;
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        String name = "div";
        tag.name(name);
        tag.name().equals(name);
        tag.reset();
        tag.name().equals(null);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        String attributeName = "class";
        tag.newAttribute();
        tag.attributes().size() == 0;
        tag.appendAttributeName("class");
        tag.newAttribute();
        tag.attributes().size() == 1;
        tag.reset();
        tag.attributes().size() == 0;
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        tag.finaliseTag();
        tag.attributes().size() == 0;
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.isSelfClosing(false);
        tag.isSelfClosing();
        tag.isSelfClosing(true);
        tag.isSelfClosing();
        tag.reset();
        tag.isSelfClosing();
        tag.isSelfClosing(false);
        tag.isSelfClosing();
    }

    @Test(expected = AssertionError.class)
    public void testTagNullName() {
        StartTag tag = new StartTag();
        tag.name(null);
    }

    @Test
    public void testStartTagToString() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.toString().startsWith("<div>");
        tag.reset();
        tag.toString().startsWith("<div>");
    }

    @Test
    public void testEndTagToString() {
        EndTag tag = new EndTag();
        tag.name("div");
        tag.toString().startsWith("</div>");
        tag.reset();
        tag.toString().startsWith("</div>");
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.length() > 0;
        comment.reset();
        comment.data.length() == 0;
    }

    @Test
    public void testCommentgetData() {
        Comment comment = new Comment();
        String data = "This is a comment";
        comment.data(data);
        comment.getData().equals(data);
        comment.reset();
        comment.getData().equals("");
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        String data = "This is a comment";
        comment.data(data);
        comment.toString().startsWith("<!--This is a comment-->");
        comment.reset();
        comment.toString().startsWith("<!---->");
    }

    @Test
    public void testCharacterData() {
        Character character = new Character();
        String data = "A character";
        character.data(data);
        character.getData().equals(data);
        character.reset();
        character.getData().equals(null);
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        String data = "A character";
        character.data(data);
        character.toString().equals(data);
        character.reset();
        character.toString().equals(null);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        // EOF cannot be instantiated or tested further
    }
}