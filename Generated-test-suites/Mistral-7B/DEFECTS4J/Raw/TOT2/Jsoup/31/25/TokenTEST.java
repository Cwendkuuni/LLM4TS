import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DoctypeTest {
    @Test
    public void testDoctypeCreation() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeInvalidName() {
        Doctype doctype = new Doctype();
        doctype.name("1html");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeInvalidSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("#");
    }
}

public class TagTest {
    @Test
    public void testTagCreation() {
        Tag tag = new Tag();
        assertEquals("Tag", tag.tokenType());
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagName() {
        Tag tag = new Tag();
        tag.name("html");
        assertEquals("html", tag.name());
    }

    @Test
    public void testSelfClosingTag() {
        Tag tag = new Tag();
        tag.name("img");
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagCreationWithAttributes() {
        Tag tag = new Tag();
        tag.name("html");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("container");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("main");
        assertEquals(2, tag.getAttributes().size());
        assertEquals("class", tag.getAttributes().keyAt(0));
        assertEquals("container", tag.getAttributes().valueAt(0));
        assertEquals("id", tag.getAttributes().keyAt(1));
        assertEquals("main", tag.getAttributes().valueAt(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagCreationWithNullTagName() {
        Tag tag = new Tag();
        tag.tagName = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagCreationWithInvalidTagName() {
        Tag tag = new Tag();
        tag.tagName = "1html";
    }
}