import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assert token.tokenType().equals("Doctype");
    }
    
    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assert doctype.name.length() == 0;
        assert doctype.pubSysKey == null;
        assert doctype.publicIdentifier.length() == 0;
        assert doctype.systemIdentifier.length() == 0;
        assert !doctype.forceQuirks;
    }
    
    @Test
    public void testStartTagNormalization() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", null);

        assert startTag.normalName.equals("div");
    }

    @Test
    public void testEmptyAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue("");
        tag.setEmptyAttributeValue();

        assert tag.hasEmptyAttributeValue;
    }

}