package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag headTag = Tag.valueOf("HEAD");
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag linkTag = Tag.valueOf("LINK");
        Tag titleTag = Tag.valueOf("TITLE");
        Tag h1Tag = Tag.valueOf("H1");
        Tag ulTag = Tag.valueOf("UL");
        Tag olTag = Tag.valueOf("OL");
        Tag dtTag = Tag.valueOf("DT");
        Tag ddTag = Tag.valueOf("DD");
        Tag liTag = Tag.valueOf("LI");
        Tag tableTag = Tag.valueOf("TABLE");
        Tag trTag = Tag.valueOf("TR");
        Tag thTag = Tag.valueOf("TH");
        Tag tdTag = Tag.valueOf("TD");

        // Test pTag can contain
        assertTrue(pTag.canContain(pTag));
        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(bodyTag));
        assertTrue(pTag.canContain(h1Tag));
        assertTrue(pTag.canContain(ulTag));
        assertTrue(pTag.canContain(olTag));
        assertTrue(pTag.canContain(dtTag));
        assertTrue(pTag.canContain(ddTag));
        assertTrue(pTag.canContain(liTag));
        assertTrue(pTag.canContain(tableTag));
        assertTrue(pTag.canContain(trTag));
        assertTrue(pTag.canContain(thTag));
        assertTrue(pTag.canContain(tdTag));

        // Test pTag cannot contain
        assertFalse(pTag.canContain(pTag)); // cannot contain self
        assertFalse(pTag.canContain(headTag));
        assertFalse(pTag.canContain(scriptTag));
        assertFalse(pTag.canContain(linkTag));
        assertFalse(pTag.canContain(titleTag));
        assertFalse(pTag.canContain(scriptTag)); // script is inline, but has been set to block here for testing
        assertFalse(pTag.canContain(tableTag)); // Table is not an ancestor of p

        // Test block tags cannot contain inline tags
        assertFalse(h1Tag.canContain(spanTag)); // span is inline, but has been set to block here for testing
        assertFalse(ulTag.canContain(spanTag));
        assertFalse(olTag.canContain(spanTag));
        assertFalse(dtTag.canContain(spanTag));
        assertFalse(ddTag.canContain(spanTag));

        // Test inline tags cannot contain block tags
        assertFalse(spanTag.canContain(pTag));
        assertFalse(spanTag.canContain(divTag));

        // Test head tags can only contain specific tags
        assertTrue(headTag.canContain(scriptTag));
        assertTrue(headTag.canContain(noscriptTag));
        assertTrue(headTag.canContain(styleTag));
        assertTrue(headTag.canContain(metaTag));
        assertTrue(headTag.canContain(linkTag));
        assertTrue(headTag.canContain(objectTag));
        assertTrue(headTag.canContain(titleTag));

        // Test dt and dd (in dl) cannot contain each other
        assertFalse(dtTag.canContain(ddTag));
        assertFalse(ddTag.canContain(dtTag));

        // Test table related tags
        Tag theadTag = Tag.valueOf("THEAD");
        Tag tfootTag = Tag.valueOf("TFOOT");
        Tag tbodyTag = Tag.valueOf("TBODY");
        Tag colgroupTag = Tag.valueOf("COLGROUP");
        Tag colTag = Tag.valueOf("COL");
        Tag trTag = Tag.valueOf("TR");
        Tag thTag = Tag.valueOf("TH");
        Tag tdTag = Tag.valueOf("TD");

        assertTrue(tableTag.canContain(theadTag));
        assertTrue(tableTag.canContain(tfootTag));
        assertTrue(tableTag.canContain(tbodyTag));
        assertTrue(tableTag.canContain(colgroupTag));
        assertTrue(theadTag.canContain(trTag));
        assertTrue(theadTag.canContain(thTag));
        assertTrue(theadTag.canContain(tdTag));
        assertTrue(tbodyTag.canContain(trTag));
        assertTrue(tbodyTag.canContain(thTag));
        assertTrue(tbodyTag.canContain(tdTag));
        assertTrue(colgroupTag.canContain(colTag));
        assertTrue(trTag.canContain(thTag));
        assertTrue(trTag.canContain(tdTag));

        // Test li can only contain specific tags
        assertTrue(liTag.canContain(pTag));
        assertTrue(liTag.canContain(ulTag));
        assertTrue(liTag.canContain(olTag));
        assertTrue(liTag.canContain(dtTag));
        assertTrue(liTag.canContain(ddTag));

        // Test other tags
        Tag insTag = Tag.valueOf("INS");
        Tag delTag = Tag.valueOf("DEL");

        assertTrue(bodyTag.canContain(insTag));
        assertTrue(bodyTag.canContain(delTag));

        // Test map and area
        Tag mapTag = Tag.valueOf("MAP");
        Tag areaTag = Tag.valueOf("AREA");

        assertTrue(mapTag.canContain(areaTag));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals(pTag1, pTag2);
        assertEquals(pTag1.hashCode(), pTag2.hashCode());
        assertNotEquals(pTag1, divTag);
        assertNotEquals(pTag1.hashCode(), divTag.hashCode());
    }
}