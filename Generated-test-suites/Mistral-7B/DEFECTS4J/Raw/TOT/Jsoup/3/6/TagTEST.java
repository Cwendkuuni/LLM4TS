import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Test
    public void testValueOf() {
        // Test getting a Tag by name
        Tag tagP = Tag.valueOf("P");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagUnknown = Tag.valueOf("UNKNOWN");

        // Test pre-defined tags
        assertTrue(tagP.canContain(tagDiv));
        assertTrue(tagP.canContain(tagP));
        assertTrue(tagDiv.canContain(tagP));
        assertFalse(tagP.canContain(tagUnknown));
        assertTrue(tagUnknown.equals(tagUnknown));
        assertFalse(tagP.equals(tagUnknown));

        // Test unknown tag
        assertTrue(tagUnknown.canContain(tagP));
        assertTrue(tagUnknown.canContain(tagDiv));
        assertFalse(tagP.canContain(tagUnknown));
        assertFalse(tagDiv.canContain(tagUnknown));

        // Test creating a new tag with an invalid name
        Tag tagEmpty = new Tag("TAG");
        assertFalse(tagEmpty.canContain(tagP));
        assertFalse(tagEmpty.canContain(tagDiv));
        assertFalse(tagP.canContain(tagEmpty));
        assertFalse(tagDiv.canContain(tagEmpty));
    }

    @Test
    public void testCanContain() {
        // Test that a block tag can contain other block tags
        Tag tagP = Tag.valueOf("P");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagForm = Tag.valueOf("FORM");
        Tag tagInput = Tag.valueOf("INPUT");

        assertTrue(tagP.canContain(tagDiv));
        assertTrue(tagDiv.canContain(tagP));
        assertTrue(tagDiv.canContain(tagDiv));
        assertTrue(tagP.canContain(tagForm));
        assertTrue(tagForm.canContain(tagP));
        assertFalse(tagP.canContain(tagInput));
        assertFalse(tagInput.canContain(tagP));

        // Test that an inline tag can contain other inline tags
        Tag tagA = Tag.valueOf("A");
        Tag tagEm = Tag.valueOf("EM");
        Tag tagStrong = Tag.valueOf("STRONG");

        assertTrue(tagA.canContain(tagEm));
        assertTrue(tagEm.canContain(tagA));
        assertTrue(tagEm.canContain(tagEm));
        assertTrue(tagA.canContain(tagStrong));
        assertTrue(tagStrong.canContain(tagA));
        assertFalse(tagEm.canContain(tagA));
        assertFalse(tagA.canContain(tagStrong));

        // Test that a block tag cannot contain other block tags directly
        assertFalse(tagP.canContain(tagP));

        // Test that an inline tag cannot contain other inline tags directly
        assertFalse(tagA.canContain(tagA));

        // Test that a block tag cannot contain an inline tag directly
        assertFalse(tagP.canContain(tagA));

        // Test that an inline tag cannot contain a block tag directly
        assertFalse(tagA.canContain(tagP));

        // Test optional closing tag
        Tag tagB = Tag.valueOf("B");
        Tag tagB2 = Tag.valueOf("B");
        assertTrue(tagB.canContain(tagB2));
        assertTrue(tagB2.canContain(tagB));
        assertFalse(tagB.canContain(tagB));
        assertFalse(tagB2.canContain(tagB));

        // Test empty tag
        Tag tagImg = Tag.valueOf("IMG");
        assertTrue(tagImg.isEmpty());
        assertTrue(tagImg.canContain(tagP));
        assertTrue(tagP.canContain(tagImg));

        // Test data only tag
        Tag tagScript = Tag.valueOf("SCRIPT");
        assertTrue(tagScript.isData());
        assertFalse(tagScript.canContain(tagP));
        assertFalse(tagP.canContain(tagScript));

        // Test head tag
        Tag tagHead = Tag.valueOf("HEAD");
        List<String> allowedChildren = Arrays.asList("BASE", "SCRIPT", "NOSCRIPT", "LINK", "META", "TITLE", "STYLE", "OBJECT");
        for (String tagName : allowedChildren) {
            Tag childTag = Tag.valueOf(tagName);
            assertTrue(tagHead.canContain(childTag));
            assertTrue(childTag.isValidParent(tagHead));
        }

        // Test dt and dd (in dl)
        Tag tagDt = Tag.valueOf("DT");
        Tag tagDd = Tag.valueOf("DD");
        assertFalse(tagDt.canContain(tagDd));
        assertFalse(tagDd.canContain(tagDt));

        // Test frame tag
        Tag tagFrame = Tag.valueOf("FRAME");
        Tag tagFrameSet = Tag.valueOf("FRAMESET");
        assertTrue(tagFrameSet.canContain(tagFrame));

        // Test table tag
        Tag tagTable = Tag.valueOf("TABLE");
        List<String> allowedTableChildren = Arrays.asList("CAPTION", "THEAD", "TBODY", "TFOOT", "TR", "TH", "TD", "COLGROUP", "COL");
        for (String tagName : allowedTableChildren) {
            Tag childTag = Tag.valueOf(tagName);
            assertTrue(tagTable.canContain(childTag));
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("TAG1");
        Tag tag2 = new Tag("TAG1");
        Tag tag3 = new Tag("TAG2");

        assertTrue(tag1.equals(tag2));
        assertTrue(tag1.hashCode() == tag2.hashCode());
        assertFalse(tag1.equals(tag3));
        assertFalse(tag1.hashCode() == tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("P");
        assertEquals("P", tag.toString());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tagP = Tag.valueOf("P");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagForm = Tag.valueOf("FORM");

        List<String> ancestorsP = Arrays.asList("html", "body");
        Tag implicitParentP = tagP.getImplicitParent();
        List<String> ancestorsImplicitParentP = implicitParentP.getAncestors();

        List<String> ancestorsDiv = Arrays.asList("html", "body", "div");
        Tag implicitParentDiv = tagDiv.getImplicitParent();
        List<String> ancestorsImplicitParentDiv = implicitParentDiv.getAncestors();

        List<String> ancestorsForm = Arrays.asList("html", "body", "form");
        Tag implicitParentForm = tagForm.getImplicitParent();
        List<String> ancestorsImplicitParentForm = implicitParentForm.getAncestors();

        assertTrue(Arrays.equals(ancestorsP.toArray(), ancestorsImplicitParentP.toArray()));
        assertTrue(Arrays.equals(ancestorsDiv.toArray(), ancestorsImplicitParentDiv.toArray()));
        assertTrue(Arrays.equals(ancestorsForm.toArray(), ancestorsImplicitParentForm.toArray()));
    }

    @Test
    public void testIsValidParent() {
        Tag tagP = Tag.valueOf("P");
        Tag tagDiv = Tag.valueOf("DIV");
        Tag tagForm = Tag.valueOf("FORM");

        List<String> ancestorsDiv = Arrays.asList("html", "body", "div");
        assertTrue(tagP.isValidParent(tagDiv));
        assertTrue(tagDiv.isValidParent(tagP));

        List<String> ancestorsForm = Arrays.asList("html", "body", "form");
        Tag childTag = Tag.valueOf("INPUT");
        assertTrue(tagForm.isValidParent(childTag));
        assertTrue(childTag.isValidParent(tagForm));

        List<String> ancestorsP = Arrays.asList("html", "body");
        assertFalse(tagDiv.isValidParent(tagP));
        assertFalse(tagP.isValidParent(tagDiv));

        List<String> ancestorsUnknown = Arrays.asList("html", "body", "UNKNOWN");
        assertFalse(tagForm.isValidParent(Tag.valueOf("UNKNOWN")));
        assertFalse(Tag.valueOf("UNKNOWN").isValidParent(tagForm));

        List<String> ancestorsEmpty = Collections.emptyList();
        assertFalse(tagP.isValidParent(new Tag("TAG")));
        assertFalse(new Tag("TAG").isValidParent(tagP));

        // Test empty tag
        List<String> ancestorsImg = Arrays.asList("html", "body");
        assertTrue(tagP.isValidParent(Tag.valueOf("IMG")));
        assertTrue(Tag.valueOf("IMG").isValidParent(tagP));

        // Test data only tag
        Tag tagScript = Tag.valueOf("SCRIPT");
        List<String> ancestorsScript = Arrays.asList("html", "body");
        assertTrue(tagP.isValidParent(tagScript));
        assertTrue(tagScript.isValidParent(tagP));
    }
}