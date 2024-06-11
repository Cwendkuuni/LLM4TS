import static org.junit.Assert.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype"); // assert initial state

        doctype.reset();
        doctype.getName().isEmpty();
        assertTrue(doctype.pubSysKey == null);
        assertTrue(doctype.publicIdentifier.isEmpty());
        assertTrue(doctype.systemIdentifier.isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        doctype.setPubSysKey("psk:12345");
        doctype.publicIdentifier.append("public id: p1");
        doctype.systemIdentifier.append("system id: s1");
        doctype.isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("psk:12345", doctype.getPubSysKey());
        assertEquals("public id: p1", doctype.getPublicIdentifier());
        assertEquals("system id: s1", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        AbstractTag tag = new StartTag();
        tag.tagName = "tag";
        tag.normalName = "TAG";
        tag.attributes = new Attributes();
        tag.newAttribute();
        tag.attributes.put("attr", "value");
        tag.hasPendingAttributeValue = true;
        tag.pendingAttributeValueS = "secondValue";

        tag.reset();
        tag.tagName = null;
        tag.normalName = null;
        tag.pendingAttributeName = null;
        tag.reset(tag.pendingAttributeValue);
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = null;
    }

    @org.junit.Test
    public void testTagSettersAndGetters() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "TAG";
        startTag.appendTagName("nameSpace:tag");
        startTag.appendAttributeName("attr");
        startTag.appendAttributeValue("value1");
        startTag.newAttribute();
        startTag.appendAttributeValue("value2");
        startTag.setEmptyAttributeValue();

        assertEquals("tag", startTag.name());
        assertEquals("TAG", startTag.normalName());
        assertEquals("nameSpace:tag", startTag.toString());
        assertEquals("value1", startTag.getAttributes().get("attr"));
        assertTrue(startTag.hasPendingAttributeValue);
        assertEquals("value2", startTag.getAttributes().get("attr"));
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes().get("emptyAttr"));
        startTag.setEmptyAttributeValue();
        assertTrue(startTag.getAttributes().get("emptyAttr").isEmpty());
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("comment");

        comment.reset();
        comment.data.isEmpty();
        assertFalse(comment.bogus);
    }

    @org.junit.Test
    public void testCommentGetter() {
        Comment comment = new Comment();
        comment.data.append("comment");

        assertEquals("comment", comment.getData());
        assertEquals("<!--comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "char";

        character.reset();
        assertNull(character.getData());
    }

    @org.junit.Test
    public void testCharacterGetter() {
        Character character = new Character();
        character.data = "char";

        assertEquals("char", character.getData());
    }

    @org.junit.Test
    public void testCDataReset() {
        CData cData = new CData("cdata");

        cData.reset();
        assertNull(cData.getData());
    }

    @org.junit.Test
    public void testCDataGetter() {
        CData cData = new CData("cdata");

        assertEquals("<![CDATA[cdata]]>", cData.toString());
    }

    @org.junit.Test
    public void testEOFReset() {
        EOF eof = new EOF();

        eof.reset();
        assertTrue(eof.isEOF());
    }
}