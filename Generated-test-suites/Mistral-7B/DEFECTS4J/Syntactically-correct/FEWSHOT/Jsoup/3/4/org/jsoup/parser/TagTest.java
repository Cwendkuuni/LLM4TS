package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testCreateBlockTags() {
        Tag html = Tag.valueOf("HTML");
        Tag head = html.getValueOf("HEAD");
        Tag body = html.getValueOf("BODY");
        Tag frameset = html.getValueOf("FRAMESET");

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("FRAMESET", frameset.getName());

        assertTrue(html.isBlock());
        assertTrue(head.isBlock());
        assertTrue(body.isBlock());
        assertTrue(frameset.isBlock());

        assertTrue(html.canContainBlock());
        assertFalse(head.canContainBlock());
        assertTrue(body.canContainBlock());
        assertTrue(frameset.canContainBlock());
    }

    @Test
    public void testCreateInlineTags() {
        Tag font = Tag.valueOf("FONT");
        Tag tt = font.getValueOf("TT");
        Tag i = font.getValueOf("I");
        Tag b = font.getValueOf("B");
        Tag big = font.getValueOf("BIG");
        Tag small = font.getValueOf("SMALL");

        assertEquals("FONT", font.getName());
        assertEquals("TT", tt.getName());
        assertEquals("I", i.getName());
        assertEquals("B", b.getName());
        assertEquals("BIG", big.getName());
        assertEquals("SMALL", small.getName());

        assertFalse(font.isBlock());
        assertFalse(tt.isBlock());
        assertFalse(i.isBlock());
        assertFalse(b.isBlock());
        assertFalse(big.isBlock());
        assertFalse(small.isBlock());

        assertTrue(font.canContainInline());
        assertTrue(tt.canContainInline());
        assertTrue(i.canContainInline());
        assertTrue(b.canContainInline());
        assertTrue(big.canContainInline());
        assertTrue(small.canContainInline());
    }

    @Test
    public void testCreateOtherTags() {
        Tag p = Tag.valueOf("P");
        Tag h1 = Tag.getValueOf("H1");
        Tag ul = Tag.getValueOf("UL");
        Tag dl = Tag.getValueOf("DL");
        Tag li = Tag.getValueOf("LI");
        Tag tr = Tag.getValueOf("TR");
        Tag th = Tag.getValueOf("TH");
        Tag td = Tag.getValueOf("TD");

        assertEquals("P", p.getName());
        assertEquals("H1", h1.getName());
        assertEquals("UL", ul.getName());
        assertEquals("DL", dl.getName());
        assertEquals("LI", li.getName());
        assertEquals("TR", tr.getName());
        assertEquals("TH", th.getName());
        assertEquals("TD", td.getName());

        assertTrue(p.isBlock());
        assertTrue(h1.isBlock());
        assertTrue(ul.isBlock());
        assertTrue(dl.isBlock());
        assertTrue(li.isInline());
        assertTrue(tr.isBlock());
        assertTrue(th.isBlock());
        assertTrue(td.isBlock());

        assertTrue(p.canContainBlock());
        assertTrue(h1.canContainBlock());
        assertTrue(ul.canContainBlock());
        assertTrue(dl.canContainBlock());
        assertFalse(li.canContainBlock());
        assertTrue(tr.canContainBlock());
        assertTrue(th.canContainBlock());
        assertTrue(td.canContainBlock());
    }

    @Test
    public void testCreateFormControlTags() {
        Tag form = Tag.getValueOf("FORM");
        Tag input = form.getValueOf("INPUT");
        Tag select = form.getValueOf("SELECT");
        Tag textarea = form.getValueOf("TEXTAREA");
        Tag label = form.getValueOf("LABEL");
        Tag button = form.getValueOf("BUTTON");

        assertEquals("FORM", form.getName());
        assertEquals("INPUT", input.getName());
        assertEquals("SELECT", select.getName());
        assertEquals("TEXTAREA", textarea.getName());
        assertEquals("LABEL", label.getName());
        assertEquals("BUTTON", button.getName());

        assertTrue(form.isBlock());
        assertFalse(input.isBlock());
        assertTrue(select.isBlock()); // specific list of only includes (optgroup or option) not implemented
        assertTrue(textarea.isBlock());
        assertTrue(label.isInline());
        assertFalse(button.isBlock());

        assertTrue(form.canContainBlock());
        assertTrue(input.canContainInlineOnly());
        assertTrue(select.canContainBlock());
        assertTrue(textarea.canContainDataOnly());
        assertTrue(label.canContainInline());
        assertFalse(button.canContainBlock());
    }

    @Test
    public void testCreateTableTags() {
        Tag table = Tag.getValueOf("TABLE");
        Tag caption = table.getValueOf("CAPTION");
        Tag thead = table.getValueOf("THEAD");
        Tag tfoot = table.getValueOf("TFOOT");
        Tag tbody = table.getValueOf("TBODY");
        Tag colgroup = table.getValueOf("COLGROUP");
        Tag col = colgroup.getValueOf("COL");
        Tag tr = tbody.getValueOf("TR");
        Tag th = tr.getValueOf("TH");
        Tag td = tr.getValueOf("TD");

        assertEquals("TABLE", table.getName());
        assertEquals("CAPTION", caption.getName());
        assertEquals("THEAD", thead.getName());
        assertEquals("TFOOT", tfoot.getName());
        assertEquals("TBODY", tbody.getName());
        assertEquals("COLGROUP", colgroup.getName());
        assertEquals("COL", col.getName());
        assertEquals("TR", tr.getName());
        assertEquals("TH", th.getName());
        assertEquals("TD", td.getName());

        assertTrue(table.isBlock());
        assertTrue(caption.isBlock());
        assertTrue(thead.isBlock());
        assertTrue(tfoot.isBlock());
        assertTrue(tbody.isBlock());
        assertTrue(colgroup.isBlock());
        assertTrue(td.isBlock());
        assertTrue(th.isBlock());
        assertFalse(tr.isBlock());
        assertFalse(col.isBlock());

        assertTrue(table.canContainBlock());
        assertTrue(caption.canContainInline());
        assertTrue(thead.canContainBlock());
        assertTrue(tfoot.canContainBlock());
        assertTrue(tbody.canContainBlock());
        assertTrue(colgroup.canContainBlock());
        assertFalse(col.canContainBlock());
        assertTrue(tr.canContainBlock());
        assertTrue(td.canContainBlock());
        assertTrue(th.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.getValueOf("DIV");
        Tag ul = Tag.getValueOf("UL");
        Tag dt = Tag.getValueOf("DT");
        Tag dd = Tag.getValueOf("DD");

        assertTrue(p.canContain(Tag.valueOf("A")));
        assertTrue(p.canContain(Tag.getValueOf("BR")));
        assertTrue(p.canContain(Tag.getValueOf("IMG")));
        assertTrue(p.canContain(div));
        assertTrue(p.canContain(ul));
        assertFalse(p.canContain(p));
        assertFalse(p.canContain(dt));
        assertFalse(p.canContain(dd));

        assertTrue(div.canContain(Tag.valueOf("B")));
        assertTrue(div.canContain(Tag.getValueOf("BR")));
        assertTrue(div.canContain(Tag.getValueOf("IMG")));
        assertTrue(div.canContain(div));
        assertTrue(div.canContain(p));
        assertTrue(div.canContain(ul));
        assertFalse(div.canContain(dt));
        assertFalse(div.canContain(dd));

        assertTrue(ul.canContain(Tag.valueOf("LI")));
        assertTrue(ul.canContain(Tag.getValueOf("BR")));
        assertTrue(ul.canContain(Tag.getValueOf("IMG")));
        assertFalse(ul.canContain(p));
        assertTrue(ul.canContain(ul));
        assertTrue(ul.canContain(dt));
        assertFalse(ul.canContain(dd));

        assertTrue(dt.canContain(Tag.valueOf("B")));
        assertTrue(dt.canContain(Tag.getValueOf("BR")));
        assertTrue(dt.canContain(Tag.getValueOf("IMG")));
        assertFalse(dt.canContain(p));
        assertFalse(dt.canContain(ul));
        assertTrue(dt.canContain(dd));
        assertTrue(dt.canContain(Tag.getValueOf("B")));

        assertTrue(dd.canContain(Tag.valueOf("B")));
        assertTrue(dd.canContain(Tag.getValueOf("BR")));
        assertTrue(dd.canContain(Tag.getValueOf("IMG")));
        assertTrue(dd.canContain(p));
        assertTrue(dd.canContain(dt));
        assertTrue(dd.canContain(dd));
    }

    @Test
    public void testGetImplicitParent() {
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.getValueOf("BODY");
        Tag p = Tag.getValueOf("P");
        Tag div = Tag.getValueOf("DIV");
        Tag ul = Tag.getValueOf("UL");

        assertNull(head.getImplicitParent());
        assertEquals(head, head.getImplicitParent());
        assertEquals(body, p.getImplicitParent());
        assertEquals(body, div.getImplicitParent());
        assertEquals(ul, ul.getImplicitParent());
        assertEquals(body, p.getImplicitParent());
        assertEquals(p, div.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.getValueOf("BODY");
        Tag p = Tag.getValueOf("P");
        Tag div = Tag.getValueOf("DIV");
        Tag ul = Tag.getValueOf("UL");

        assertTrue(head.isValidParent(Tag.valueOf("HEAD")));
        assertTrue(head.isValidParent(Tag.getValueOf("BODY")));
        assertFalse(head.isValidParent(Tag.getValueOf("P")));
        assertFalse(head.isValidParent(Tag.getValueOf("DIV")));
        assertFalse(head.isValidParent(Tag.getValueOf("UL")));

        assertTrue(body.isValidParent(Tag.valueOf("HEAD")));
        assertTrue(body.isValidParent(Tag.getValueOf("BODY")));
        assertTrue(body.isValidParent(Tag.getValueOf("P")));
        assertTrue(body.isValidParent(Tag.getValueOf("DIV")));
        assertTrue(body.isValidParent(Tag.getValueOf("UL")));

        assertTrue(p.isValidParent(Tag.valueOf("HEAD")));
        assertTrue(p.isValidParent(Tag.getValueOf("BODY")));
        assertFalse(p.isValidParent(Tag.getValueOf("HEAD")));
        assertTrue(p.isValidParent(Tag.getValueOf("P")));
        assertTrue(p.isValidParent(Tag.getValueOf("DIV")));
        assertFalse(p.isValidParent(Tag.getValueOf("UL")));

        assertTrue(div.isValidParent(Tag.getValueOf("HEAD")));
        assertTrue(div.isValidParent(Tag.getValueOf("BODY")));
        assertTrue(div.isValidParent(Tag.getValueOf("HEAD")));
        assertTrue(div.isValidParent(Tag.getValueOf("P")));
        assertTrue(div.isValidParent(Tag.getValueOf("DIV")));
        assertTrue(div.isValidParent(Tag.getValueOf("UL")));

        assertTrue(ul.isValidParent(Tag.getValueOf("HEAD")));
        assertTrue(ul.isValidParent(Tag.getValueOf("BODY")));
        assertFalse(ul.isValidParent(Tag.getValueOf("HEAD")));
        assertTrue(ul.isValidParent(Tag.getValueOf("P")));
        assertTrue(ul.isValidParent(Tag.getValueOf("UL")));
        assertTrue(ul.isValidParent(Tag.getValueOf("LI")));
    }
}