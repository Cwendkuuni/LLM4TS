import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks() == false;

        doctype.setName("html");
        doctype.setPublicIdentifier("public id");
        doctype.setSystemIdentifier("/system/id");
        doctype.setForceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        doctype.getName().equals("");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks() == false;

        Assert.assertEquals(doctype, resetDoctype);
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName().equals(null);
        tag.normalName().equals(null);
        tag.pendingAttributeName().equals(null);
        tag.pendingAttributeValue().length() == 0;
        tag.pendingAttributeValueS().equals(null);
        tag.hasEmptyAttributeValue() == false;
        tag.hasPendingAttributeValue() == false;
        tag.selfClosing() == false;
        tag.attributes() == null;

        tag.setName("div");
        tag.setNormalName("div");
        tag.setPendingAttributeName("class");
        tag.setPendingAttributeValue("class1 class2");
        tag.setPendingAttributeValueS("class3");
        tag.setHasEmptyAttributeValue(true);
        tag.setHasPendingAttributeValue(true);
        tag.setSelfClosing(true);
        tag.setAttributes(new Attributes());

        Tag resetTag = tag.reset();

        tag.tagName().equals(null);
        tag.normalName().equals(null);
        tag.pendingAttributeName().equals(null);
        tag.pendingAttributeValue().length() == 0;
        tag.pendingAttributeValueS().equals(null);
        tag.hasEmptyAttributeValue() == false;
        tag.hasPendingAttributeValue() == false;
        tag.selfClosing() == false;
        tag.attributes() == null;

        Assert.assertEquals(tag, resetTag);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("");
        comment.bogus() == false;

        comment.setData("This is a comment");
        comment.setBogus(true);

        Comment resetComment = comment.reset();

        comment.getData().equals("");
        comment.isBogus() == false;

        Assert.assertEquals(comment, resetComment);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("");

        character.setData("A");

        Character resetCharacter = character.reset();

        character.getData().equals("");

        Assert.assertEquals(character, resetCharacter);
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeTypeMismatch() {
        Token token = new Doctype();
        Token expectedType = new StartTag();

        Assert.assertEquals(TokenType.Doctype, token.type());
        token = (Doctype) token.reset();
        Assert.assertEquals(TokenType.StartTag, token.type());
    }

    @Test
    public void testTagTypeMismatchReset() {
        Tag tag = new StartTag();
        Token expectedType = new EndTag();

        Assert.assertEquals(TokenType.StartTag, tag.type());
        tag = (Tag) tag.reset();
        Assert.assertEquals(TokenType.EndTag, tag.type());
        tag = (Tag) tag.reset();
        Assert.assertEquals(TokenType.StartTag, tag.type());
    }

    @Test
    public void testTagTypeMismatchNew() {
        Tag tag = new StartTag();
        Token expectedType = new EndTag();

        Assert.assertEquals(TokenType.StartTag, tag.type());
        tag = new EndTag();
        Assert.assertEquals(TokenType.EndTag, tag.type());
    }
}