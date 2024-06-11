package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag headTag = Tag.valueOf("head");
        Tag h1Tag = Tag.valueOf("h1");
        Tag inputTag = Tag.valueOf("input");
        Tag formTag = Tag.valueOf("form");
        Tag tableTag = Tag.valueOf("table");

        // p can contain div
        assertTrue(pTag.canContain(divTag));

        // div can contain p
        assertTrue(divTag.canContain(pTag));

        // p cannot contain p
        assertFalse(pTag.canContain(pTag));

        // head cannot contain p
        assertFalse(headTag.canContain(pTag));

        // h1 can contain p
        assertTrue(h1Tag.canContain(pTag));

        // input can be contained by form
        assertTrue(formTag.canContain(inputTag));

        // form cannot contain form
        assertFalse(formTag.canContain(formTag));

        // table cannot contain p directly
        assertFalse(tableTag.canContain(pTag));

        // table can contain tr, which can contain td, which can contain p
        Tag trTag = Tag.valueOf("tr");
        Tag tdTag = Tag.valueOf("td");
        assertTrue(tableTag.canContain(trTag));
        assertTrue(trTag.canContain(tdTag));
        assertTrue(tdTag.canContain(pTag));
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        Tag imgTag = Tag.valueOf("img");

        // script is data only
        assertTrue(scriptTag.isData());

        // img is not data only
        assertFalse(imgTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag brTag = Tag.valueOf("br");

        // img is not empty
        assertFalse(imgTag.isEmpty());

        // br is empty
        assertTrue(brTag.isEmpty());
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");

        // p is block
        assertTrue(pTag.isBlock());

        // span is inline
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");

        // p is block
        assertFalse(pTag.isInline());

        // span is inline
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag headTag = Tag.valueOf("head");
        Tag bodyTag = Tag.valueOf("body");

        // p is a valid parent for div
        assertTrue(pTag.isValidParent(divTag));

        // div is not a valid parent for p
        assertFalse(divTag.isValidParent(pTag));

        // head is not a valid parent for p
        assertFalse(headTag.isValidParent(pTag));

        // body is a valid parent for p
        assertTrue(bodyTag.isValidParent(pTag));
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        // toString returns the tag name
        assertEquals("p", pTag.toString());
        assertEquals("div", divTag.toString());
    }
}