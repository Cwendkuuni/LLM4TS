import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character("");
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeTokenGetName() {
        doctypeToken.name.append("HTML");
        assertEquals("HTML", doctypeToken.getName());
    }

    @Test
    public void testDoctypeTokenGetPublicIdentifier() {
        doctypeToken.publicIdentifier.append("PUBLIC");
        assertEquals("PUBLIC", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeTokenGetSystemIdentifier() {
        doctypeToken.systemIdentifier.append("SYSTEM");
        assertEquals("SYSTEM", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeTokenIsForceQuirks() {
        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagTokenNewAttribute() {
        startTagToken.newAttribute();
        assertNotNull(startTagToken.attributes);
        assertEquals(0, startTagToken.attributes.size());
    }

    @Test
    public void testStartTagTokenFinaliseTag() {
        startTagToken.tagName = "div";
        startTagToken.pendingAttributeName = "id";
        startTagToken.pendingAttributeValue = new StringBuilder("container");
        startTagToken.finaliseTag();
        assertNotNull(startTagToken.attributes);
        assertEquals(1, startTagToken.attributes.size());
        assertEquals("id", startTagToken.attributes.get("id").getKey());
        assertEquals("container", startTagToken.attributes.get("id").getValue());
    }

    @Test
    public void testStartTagTokenName() {
        startTagToken.tagName = "div";
        assertEquals("div", startTagToken.name());
    }

    @Test
    public void testStartTagTokenSelfClosing() {
        startTagToken.selfClosing = true;
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagTokenGetAttributes() {
        startTagToken.attributes.put("class", "content");
        assertNotNull(startTagToken.getAttributes());
        assertEquals(1, startTagToken.getAttributes().size());
        assertEquals("class", startTagToken.getAttributes().get("class").getKey());
        assertEquals("content", startTagToken.getAttributes().get("class").getValue());
    }

    @Test
    public void testStartTagTokenAppendTagName() {
        startTagToken.appendTagName("div");
        assertEquals("div", startTagToken.tagName);
    }

    @Test
    public void testStartTagTokenAppendReadOnlyTag() {
        startTagToken.appendTagName('d');
        startTagToken.appendTagName('i');
        startTagToken.appendTagName('v');
        assertEquals("div", startTagToken.tagName);
    }

    @Test
    public void testStartTagTokenAppendAttributeName() {
        startTagToken.appendAttributeName("id");
        assertEquals("id", startTagToken.pendingAttributeName);
    }

    @Test
    public void testStartTagTokenAppendReadOnlyAttributeName() {
        startTagToken.appendAttributeName("i");
        startTagToken.appendAttributeName("d");
        assertEquals("id", startTagToken.pendingAttributeName);
    }

    @Test
    public void testStartTagTokenAppendAttributeValue() {
        startTagToken.appendAttributeValue("container");
        assertEquals("container", startTagToken.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagTokenAppendReadOnlyAttributeValue() {
        startTagToken.appendAttributeValue("c");
        startTagToken.appendAttributeValue("o");
        startTagToken.appendAttributeValue("n");
        startTagToken.appendAttributeValue("t");
        startTagToken.appendAttributeValue("a");
        startTagToken.appendAttributeValue("i");
        startTagToken.appendAttributeValue("n");
        startTagToken.appendAttributeValue("e");
        startTagToken.appendAttributeValue("r");
        assertEquals("container", startTagToken.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagTokenToString() {
        endTagToken.tagName = "div";
        assertEquals("</div>", endTagToken.toString());
    }

    @Test
    public void testCommentTokenGetData() {
        commentToken.data.append("This is a comment.");
        assertEquals("This is a comment.", commentToken.getData());
    }

    @Test
    public void testCharacterTokenGetData() {
        characterToken = new Token.Character("This is character data.");
        assertEquals("This is character data.", characterToken.getData());
    }

    @Test
    public void testEOFToken() {
        assertTrue(eofToken.isEOF());
    }

}