package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD").setAncestor("HTML");
        Tag body = new Tag("BODY").setAncestor("HTML");
        Tag script = new Tag("SCRIPT").setAncestor("HEAD", "BODY").setContainDataOnly();

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("SCRIPT", script.getName());
    }

    @Test
    public void testIsBlock() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");

        assertTrue(p.isBlock());
        assertTrue(div.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag span = new Tag("SPAN");

        assertTrue(p.canContainBlock());
        assertTrue(div.canContainBlock());
        assertFalse(span.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag script = new Tag("SCRIPT").setContainDataOnly();
        Tag textarea = new Tag("TEXTAREA").setContainDataOnly();

        assertTrue(script.isData());
        assertTrue(textarea.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = new Tag("IMG").setEmpty();
        Tag br = new Tag("BR").setEmpty();

        assertTrue(img.isEmpty());
        assertTrue(br.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag pre = new Tag("PRE").setPreserveWhitespace();

        assertTrue(pre.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag div = new Tag("DIV").addAncestor("BODY");

        assertEquals("BODY", div.getImplicitParent().getName());
    }

    @Test
    public void testIsValidParent() {
        Tag div = new Tag("DIV");
        Tag p = new Tag("P");

        assertTrue(div.isValidParent(p));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag p1 = new Tag("P");
        Tag p2 = new Tag("P");
        Tag div = new Tag("DIV");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, div);
    }

    @Test
    public void testCanContain() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");
        Tag span = new Tag("SPAN");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertFalse(div.canContain(p));
        assertFalse(div.canContain(div));
        assertFalse(span.canContain(p));
        assertFalse(span.canContain(span));

        // test some special cases
        assertEquals(Tag.class, p.canContain(Tag.class));
        assertFalse(p.canContain(null));
    }

    @Test
    public void testValueOf() {
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertEquals("P", p.getName());
        assertEquals("DIV", div.getName());

        // test unknown tag
        Tag unknown = Tag.valueOf("UNKNOWN");
        assertFalse(unknown.isBlock());
        assertFalse(unknown.canContainBlock());
        assertFalse(unknown.canContainInline);
        assertFalse(unknown.isEmpty());
        assertTrue(unknown.preserveWhitespace);
        assertEquals(Arrays.asList(new String[]{"UNKNOWN"}), unknown.ancestors);
    }

    @Test
    public void testToString() {
        Tag p = new Tag("P");
        Tag div = new Tag("DIV");

        assertEquals("P", p.toString());
        assertEquals("DIV", div.toString());
    }

    @Test
    public void testCanContainWithPredefinedTags() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag title = new Tag("TITLE");
        Tag link = new Tag("LINK");
        Tag script = new Tag("SCRIPT");

        // head can only contain a few tags, as defined
        assertFalse(html.canContain(title));
        assertFalse(html.canContain(link));
        assertFalse(html.canContain(script));
        assertFalse(html.canContain(title.setValueOf("H1")));
        assertFalse(html.canContain(link.setValueOf("IMG")));

        // dt and dd (in dl)
        Tag dl = new Tag("DL");
        Tag dt = new Tag("DT");
        Tag dd = new Tag("DD");

        assertFalse(dt.canContain(dd));
        assertFalse(dd.canContain(dt));

        // don't allow children to contain their parent (directly)
        Tag parent = new Tag("DIV");
        Tag child = new Tag("DIV").setAncestor(parent);

        assertFalse(child.canContain(parent));

        // test some more complex cases
        Tag ul = new Tag("UL");
        Tag li = new Tag("LI");
        Tag ol = new Tag("OL");

        assertTrue(ul.canContain(li));
        assertTrue(ul.canContain(ol));
        assertTrue(ol.canContain(li));

        // tables
        Tag table = new Tag("TABLE");
        Tag tr = new Tag("TR");
        Tag th = new Tag("TH");
        Tag td = new Tag("TD");

        assertTrue(table.canContain(tr));
        assertTrue(table.canContain(th));
        assertTrue(table.canContain(td));
        assertTrue(tr.canContain(th));
        assertTrue(tr.canContain(td));

        // form elements
        Tag form = new Tag("FORM");
        Tag input = new Tag("INPUT");
        Tag select = new Tag("SELECT");
        Tag textarea = new Tag("TEXTAREA");
        Tag label = new Tag("LABEL");
        Tag button = new Tag("BUTTON");
        Tag optgroup = new Tag("OPTGROUP");
        Tag option = new Tag("OPTION");
        Tag fieldset = new Tag("FIELDSET");
        Tag legend = new Tag("LEGEND");

        assertTrue(form.canContain(input));
        assertTrue(form.canContain(select));
        assertTrue(form.canContain(textarea));
        assertTrue(form.canContain(label));
        assertTrue(form.canContain(button));
        assertTrue(select.canContain(optgroup));
        assertTrue(select.canContain(option));
        assertTrue(fieldset.canContain(legend));

        // other
        Tag area = new Tag("AREA");
        Tag param = new Tag("PARAM");
        Tag ins = new Tag("INS");
        Tag del = new Tag("DEL");

        assertTrue(ins.canContain(text.setValueOf("P")));
        assertTrue(del.canContain(text.setValueOf("P")));
        assertTrue(area.canContain(input));
        assertTrue(param.canContain(text.setValueOf("P")));

        // test invalid parents
        Tag invalidParent = new Tag("INVALID_PARENT");

        assertFalse(p.canContain(invalidParent));
        assertFalse(div.canContain(invalidParent));
    }

    @Test
    public void testToStringWithAncestors() {
        Tag div = new Tag("DIV").addAncestor("BODY");

        assertEquals("DIV (ancestors: BODY)", div.toString());
    }

    @Test
    public void testToStringWithoutAncestors() {
        Tag div = new Tag("DIV");

        assertEquals("DIV", div.toString());
    }

    private Tag text = new Tag("P");
}