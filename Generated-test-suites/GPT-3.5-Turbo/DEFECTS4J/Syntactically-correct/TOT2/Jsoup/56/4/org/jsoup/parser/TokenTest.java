import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assert token.tokenType().equals("Doctype");
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.name.append("Test Name");
        doctype.publicIdentifier.append("Test Public Identifier");
        doctype.systemIdentifier.append("Test System Identifier");

        doctype.reset();

        assert !doctype.forceQuirks;
        assert doctype.name.toString().isEmpty();
        assert doctype.publicIdentifier.toString().isEmpty();
        assert doctype.systemIdentifier.toString().isEmpty();
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "Test Name";
        tag.attributes = new Attributes();
        tag.pendingAttributeName = "Test Attribute";

        tag.reset();

        assert tag.tagName == null;
        assert tag.normalName == null;
        assert tag.pendingAttributeName == null;
        assert tag.pendingAttributeValue.toString().isEmpty();
        assert tag.pendingAttributeValueS == null;
        assert !tag.hasEmptyAttributeValue;
        assert !tag.hasPendingAttributeValue;
        assert !tag.selfClosing;
        assert tag.attributes == null;
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes.put("id", "testId");

        assert startTag.toString().equals("<div id=\"testId\">");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        assert endTag.toString().equals("</div>");
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Data");
        comment.bogus = true;

        comment.reset();

        assert comment.data.toString().isEmpty();
        assert !comment.bogus;
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Test Data";

        character.reset();

        assert character.data == null;
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        // No assertion since reset method does not have any specific behavior
    }

    // Add more test cases for other methods if needed

}