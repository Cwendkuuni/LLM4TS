import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private static Tag genericTag;
    private static Tag knownTag;

    @BeforeClass
    public static void setUp() {
        genericTag = Tag.valueOf("generic");
        knownTag = Tag.valueOf("p");
    }

    @Test
    public void testGetName() {
        assertEquals("generic", genericTag.getName());
        assertEquals("p", knownTag.getName());
    }

    @Test
    public void testValueOf() {
        assertNotNull(Tag.valueOf("generic"));
        assertNotNull(Tag.valueOf("p"));
    }

    @Test
    public void testIsBlock() {
        assertFalse(genericTag.isBlock());
        assertTrue(knownTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(genericTag.formatAsBlock());
        assertTrue(knownTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(genericTag.canContainBlock());
        assertTrue(knownTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertTrue(genericTag.isInline());
        assertFalse(knownTag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(genericTag.isData());
        assertFalse(knownTag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(genericTag.isEmpty());
        assertFalse(knownTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(genericTag.isSelfClosing());
        assertFalse(knownTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(genericTag.isKnownTag());
        assertTrue(knownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("generic"));
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(genericTag.preserveWhitespace());
        assertFalse(knownTag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        assertFalse(genericTag.isSelfClosing());
        assertNotNull(genericTag.setSelfClosing());
        assertTrue(genericTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        assertTrue(genericTag.equals(genericTag));
        assertFalse(genericTag.equals(knownTag));
        assertFalse(genericTag.equals(null));
        assertFalse(genericTag.equals(new Object()));
        assertFalse(genericTag.equals(new Tag("generic")));
    }

    @Test
    public void testHashCode() {
        assertEquals(genericTag.hashCode(), new Tag("generic").hashCode());
        assertNotEquals(genericTag.hashCode(), knownTag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("generic", genericTag.toString());
        assertEquals("p", knownTag.toString());
    }
}