import org.junit.Test;

public class TokenTest {

    // Doctype tests
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();

        // initialize doctype
        doctype.name.append("html");
        doctype.pubSysKey = "pubkey";
        doctype.publicIdentifier.append("pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;

        // reset doctype
        doctype.reset();

        // assert that doctype has been reset to initial state
        assert(doctype.name.toString().equals(""));
        assert(doctype.pubSysKey == null);
        assert(doctype.publicIdentifier.toString().equals(""));
        assert(doctype.systemIdentifier.toString().equals(""));
        assert(doctype.forceQuirks == false);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        // assert that the name is correct
        assert(doctype.getName().equals("html"));
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "pubkey";

        // assert that the pubSysKey is correct
        assert(doctype.getPubSysKey().equals("pubkey"));
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("pubid");

        // assert that the publicIdentifier is correct
        assert(doctype.getPublicIdentifier().equals("pubid"));
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("sysid");

        // assert that the systemIdentifier is correct
        assert(doctype.getSystemIdentifier().equals("sysid"));
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        // assert that the forceQuirks is correct
        assert(doctype.isForceQuirks());
    }

    // Tag tests
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();

        // initialize tag
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("myDiv");
        tag.pendingAttributeValueS = "";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;

        // reset tag
        tag.reset();

        // assert that tag has been reset to initial state
        assert(tag.tagName == null);
        assert(tag.normalName == null);
        assert(tag.pendingAttributeName == null);
        assert(tag.pendingAttributeValue.toString().equals(""));
        assert(tag.pendingAttributeValueS == null);
        assert(tag.hasEmptyAttributeValue == false);
        assert(tag.hasPendingAttributeValue == false);
        assert(tag.selfClosing == false);
        assert(tag.attributes == null);
    }

    // complete the rest of the tests for the Tag class...

}