import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testNameGetterAndSetter() {
        doctype.setName("doctype");
        assertEquals("doctype", doctype.getName());
    }

    @Test
    public void testPubSysKeyGetterAndSetter() {
        doctype.setPubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());
    }

    @Test
    public void testPublicIdentifierGetterAndSetter() {
        doctype.setPublicIdentifier("publicidentifier");
        assertEquals("publicidentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testSystemIdentifierGetterAndSetter() {
        doctype.setSystemIdentifier("systemidentifier");
        assertEquals("systemidentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testForceQuirksGetterAndSetter() {
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testReset() {
        doctype.setName("doctype");
        doctype.setPubSysKey("pubsyskey");
        doctype.setPublicIdentifier("publicidentifier");
        doctype.setSystemIdentifier("systemidentifier");
        doctype.setForceQuirks(true);

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagNameAndAttributes() {
        startTag.nameAttr("tagname", new Attributes());
        assertEquals("tagname", startTag.name());
        assertEquals(0, startTag.getAttributes().size());
    }

    @Test
    public void testEndTagName() {
        endTag.name("tagname");
        assertEquals("tagname", endTag.name());
    }

    @Test
    public void testCommentData() {
        comment.setData("commentdata");
        assertEquals("commentdata", comment.getData());
    }

    @Test
    public void testCharacterData() {
        character.data("characterdata");
        assertEquals("characterdata", character.getData());
    }

}