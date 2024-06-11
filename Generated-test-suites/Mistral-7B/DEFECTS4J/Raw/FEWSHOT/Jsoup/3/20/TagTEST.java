package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValueOf() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknown");

        assertEquals("p", tagP.getName());
        assertEquals("div", tagDiv.getName());
        assertNotSame(tagP, tagDiv);
        assertTrue(tagP.equals(tagP));
        assertTrue(tagP.equals(tagUnknown));
    }

    @Test
    public void testTagCanContain() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagImg = Tag.valueOf("img");

        assertTrue(tagP.canContain(tagSpan));
        assertTrue(tagDiv.canContain(tagP));
        assertTrue(tagDiv.canContain(tagDiv));
        assertTrue(tagDiv.canContain(tagSpan));
        assertFalse(tagP.canContain(tagP));
        assertFalse(tagP.canContain(tagDiv));
        assertFalse(tagP.canContain(tagImg));
        assertFalse(tagDiv.canContain(tagImg));
    }

    @Test
    public void testTagIsBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagForm = Tag.valueOf("form");

        assertTrue(tagP.isBlock());
        assertTrue(tagDiv.isBlock());
        assertFalse(tagSpan.isBlock());
        assertFalse(tagForm.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagForm = Tag.valueOf("form");

        assertTrue(tagDiv.canContainBlock());
        assertTrue(tagP.canContainBlock());
        assertFalse(tagSpan.canContainBlock());
        assertFalse(tagForm.canContainBlock());
    }

    @Test
    public void testTagIsData() {
        Tag tagScript = Tag.valueOf("script");
        Tag tagPre = Tag.valueOf("pre");
        Tag tagTextarea = Tag.valueOf("textarea");

        assertTrue(tagScript.isData());
        assertTrue(tagPre.isData());
        assertTrue(tagTextarea.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tagImg = Tag.valueOf("img");
        Tag tagBr = Tag.valueOf("br");
        Tag tagInput = Tag.valueOf("input");

        assertTrue(tagImg.isEmpty());
        assertTrue(tagBr.isEmpty());
        assertFalse(tagInput.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tagPre = Tag.valueOf("pre");
        Tag tagDiv = Tag.valueOf("div");

        assertFalse(tagDiv.preserveWhitespace());
        assertTrue(tagPre.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagForm = Tag.valueOf("form");

        assertEquals(tagDiv.getName(), tagP.getImplicitParent().getName());
        assertNull(tagSpan.getImplicitParent());
        assertEquals(tagDiv.getName(), tagDiv.getImplicitParent().getName());
        assertEquals(tagForm.getName(), tagInput.getImplicitParent().getName());
    }

    @Test
    public void testTagIsValidParent() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagSpan = Tag.valueOf("span");
        Tag tagForm = Tag.valueOf("form");
        Tag tagInput = Tag.valueOf("input");

        assertTrue(tagDiv.isValidParent(tagP));
        assertTrue(tagDiv.isValidParent(tagDiv));
        assertFalse(tagP.isValidParent(tagDiv));
        assertFalse(tagP.isValidParent(tagSpan));
        assertFalse(tagDiv.isValidParent(tagInput));
        assertFalse(tagForm.isValidParent(tagInput));
        assertTrue(tagForm.isValidParent(tagDiv));
        assertTrue(tagForm.isValidParent(tagSpan));
    }

    @Test
    public void testTagEquals() {
        Tag tagP1 = Tag.valueOf("p");
        Tag tagP2 = Tag.valueOf("p");
        Tag tagDiv1 = Tag.valueOf("div");
        Tag tagDiv2 = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknown");

        assertTrue(tagP1.equals(tagP2));
        assertTrue(tagP1.equals(tagUnknown));
        assertNotEquals(tagP1, tagDiv1);
        assertNotEquals(tagDiv1, tagP1);
        assertNotEquals(tagP1, tagUnknown);
    }

    @Test
    public void testTagHashcode() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");
        Tag tagUnknown = Tag.valueOf("unknown");

        int hashcodeP = tagP.hashCode();
        int hashcodeDiv = tagDiv.hashCode();
        int hashcodeUnknown = tagUnknown.hashCode();

        assertEquals(hashcodeP, hashcodeP);
        assertEquals(hashcodeP, hashcodeUnknown);
        assertNotEquals(hashcodeP, hashcodeDiv);
        assertNotEquals(hashcodeDiv, hashcodeP);
        assertNotEquals(hashcodeDiv, hashcodeUnknown);
    }

    @Test
    public void testTagToString() {
        Tag tagP = Tag.valueOf("p");
        Tag tagDiv = Tag.valueOf("div");

        assertEquals("p", tagP.toString());
        assertEquals("div", tagDiv.toString());
    }

    // Test createBlock and createInline methods
    @Test
    public void testCreateBlockAndCreateInline() {
        Tag tagHtml = createBlock("HTML");
        Tag tagHead = createBlock("HEAD");
        Tag tagBody = createBlock("BODY");
        Tag tagFrameSet = createBlock("FRAMESET");
        Tag tagP = createBlock("P");
        Tag tagH1 = createBlock("H1");
        Tag tagUl = createBlock("UL");
        Tag tagOl = createBlock("OL");
        Tag tagPre = createBlock("PRE");
        Tag tagDiv = createBlock("DIV");
        Tag tagBlockquote = createBlock("BLOCKQUOTE");
        Tag tagHr = createBlock("HR");
        Tag tagAddress = createBlock("ADDRESS");
        Tag tagForm = createBlock("FORM");
        Tag tagInput = createInline("INPUT");
        Tag tagSelect = createInline("SELECT");
        Tag tagTextarea = createInline("TEXTAREA");
        Tag tagLabel = createInline("LABEL");
        Tag tagButton = createInline("BUTTON");
        Tag tagOptgroup = createInline("OPTGROUP");
        Tag tagOption = createInline("OPTION");
        Tag tagTable = createBlock("TABLE");
        Tag tagCaption = createBlock("CAPTION");
        Tag tagThead = createBlock("THEAD");
        Tag tagTfoot = createBlock("TFOOT");
        Tag tagTbody = createBlock("TBODY");
        Tag tagColgroup = createBlock("COLGROUP");
        Tag tagCol = createBlock("COL");
        Tag tagTr = createBlock("TR");
        Tag tagTh = createBlock("TH");
        Tag tagTd = createBlock("TD");

        assertEquals("HTML", tagHtml.getName());
        assertEquals("HEAD", tagHead.getName());
        assertEquals("BODY", tagBody.getName());
        assertEquals("FRAMESET", tagFrameSet.getName());
        assertEquals("P", tagP.getName());
        assertEquals("H1", tagH1.getName());
        assertEquals("UL", tagUl.getName());
        assertEquals("OL", tagOl.getName());
        assertEquals("PRE", tagPre.getName());
        assertEquals("DIV", tagDiv.getName());
        assertEquals("BLOCKQUOTE", tagBlockquote.getName());
        assertEquals("HR", tagHr.getName());
        assertEquals("ADDRESS", tagAddress.getName());
        assertEquals("FORM", tagForm.getName());
        assertEquals("INPUT", tagInput.getName());
        assertEquals("SELECT", tagSelect.getName());
        assertEquals("TEXTAREA", tagTextarea.getName());
        assertEquals("LABEL", tagLabel.getName());
        assertEquals("BUTTON", tagButton.getName());
        assertEquals("OPTGROUP", tagOptgroup.getName());
        assertEquals("OPTION", tagOption.getName());
        assertEquals("TABLE", tagTable.getName());
        assertEquals("CAPTION", tagCaption.getName());
        assertEquals("THEAD", tagThead.getName());
        assertEquals("TFOOT", tagTfoot.getName());
        assertEquals("TBODY", tagTbody.getName());
        assertEquals("COLGROUP", tagColgroup.getName());
        assertEquals("COL", tagCol.getName());
        assertEquals("TR", tagTr.getName());
        assertEquals("TH", tagTh.getName());
        assertEquals("TD", tagTd.getName());

        assertEquals(Tag.class, tagHtml.getClass());
        assertEquals(Tag.class, tagHead.getClass());
        assertEquals(Tag.class, tagBody.getClass());
        assertEquals(Tag.class, tagFrameSet.getClass());
        assertEquals(Tag.class, tagP.getClass());
        assertEquals(Tag.class, tagH1.getClass());
        assertEquals(Tag.class, tagUl.getClass());
        assertEquals(Tag.class, tagOl.getClass());
        assertEquals(Tag.class, tagPre.getClass());
        assertEquals(Tag.class, tagDiv.getClass());
        assertEquals(Tag.class, tagBlockquote.getClass());
        assertEquals(Tag.class, tagHr.getClass());
        assertEquals(Tag.class, tagAddress.getClass());
        assertEquals(Tag.class, tagForm.getClass());
        assertEquals(Tag.class, tagInput.getClass());
        assertEquals(Tag.class, tagSelect.getClass());
        assertEquals(Tag.class, tagTextarea.getClass());
        assertEquals(Tag.class, tagLabel.getClass());
        assertEquals(Tag.class, tagButton.getClass());
        assertEquals(Tag.class, tagOptgroup.getClass());
        assertEquals(Tag.class, tagOption.getClass());
        assertEquals(Tag.class, tagTable.getClass());
        assertEquals(Tag.class, tagCaption.getClass());
        assertEquals(Tag.class, tagThead.getClass());
        assertEquals(Tag.class, tagTfoot.getClass());
        assertEquals(Tag.class, tagTbody.getClass());
        assertEquals(Tag.class, tagColgroup.getClass());
        assertEquals(Tag.class, tagCol.getClass());
        assertEquals(Tag.class, tagTr.getClass());
        assertEquals(Tag.class, tagTh.getClass());
        assertEquals(Tag.class, tagTd.getClass());

        // Test ancestor property
        assertEquals(new String[0], tagHtml.ancestors.toArray(new String[0]));
        assertEquals("HTML", tagHead.getAncestor(0).getName());
        assertEquals("BODY", tagP.getAncestor(0).getName());
        assertEquals("HTML", tagHead.getAncestor(1).getName());
        assertNull(tagInput.getAncestor(0));
    }
}