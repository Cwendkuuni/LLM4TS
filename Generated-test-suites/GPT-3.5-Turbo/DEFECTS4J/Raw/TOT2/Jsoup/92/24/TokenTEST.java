import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.pubSysKey = "testKey";
        doctype.publicIdentifier.append("testPubId");
        doctype.systemIdentifier.append("testSysId");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("testData");

        assertEquals("testData", character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("testCData");

        assertEquals("<![CDATA[testCData]]>", cdata.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();

        assertNotNull(resetEOF);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("testTag");
        tag.appendAttributeName("testAttribute");
        tag.appendAttributeValue("testValue");

        Token.Tag resetTag = tag.reset();

        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("testTagName", new org.jsoup.nodes.Attributes());

        assertEquals("<testTagName >", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("testTagName");

        assertEquals("</testTagName>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");

        assertEquals("testData", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");

        assertEquals("<!--testData-->", comment.toString());
    }
    
}