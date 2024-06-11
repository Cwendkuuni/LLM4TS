package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

 private static final Tag HTML = Tag.valueOf("html");
 private static final Tag HEAD = Tag.valueOf("head");
 private static final Tag BODY = Tag.valueOf("body");
 private static final Tag P = Tag.valueOf("p");
 private static final Tag DIV = Tag.valueOf("div");
 private static final Tag SCRIPT = Tag.valueOf("script");
 private static final Tag NOSCRIPT = Tag.valueOf("noscript");
 private static final Tag STYLE = Tag.valueOf("style");
 private static final Tag META = Tag.valueOf("meta");
 private static final Tag LINK = Tag.valueOf("link");
 private static final Tag IMG = Tag.valueOf("img");
 private static final Tag BR = Tag.valueOf("br");
 private static final Tag H1 = Tag.valueOf("h1");
 private static final Tag H2 = Tag.valueOf("h2");
 private static final Tag H3 = Tag.valueOf("h3");
 private static final Tag H4 = Tag.valueOf("h4");
 private static final Tag H5 = Tag.valueOf("h5");
 private static final Tag H6 = Tag.valueOf("h6");
 private static final Tag UL = Tag.valueOf("ul");
 private static final Tag OL = Tag.valueOf("ol");
 private static final Tag HR = Tag.valueOf("hr");
 private static final Tag ADDRESS = Tag.valueOf("address");
 private static final Tag FIGURE = Tag.valueOf("figure");
 private static final Tag FIGCAPTION = Tag.valueOf("figcaption");
 private static final Tag FORM = Tag.valueOf("form");
 private static final Tag FIELDSET = Tag.valueOf("fieldset");
 private static final Tag INS = Tag.valueOf("ins");
 private static final Tag DEL = Tag.valueOf("del");
 private static final Tag DL = Tag.valueOf("dl");
 private static final Tag DT = Tag.valueOf("dt");
 private static final Tag DD = Tag.valueOf("dd");
 private static final Tag LI = Tag.valueOf("li");
 private static final Tag TABLE = Tag.valueOf("table");
 private static final Tag CAPTION = Tag.valueOf("caption");
 private static final Tag THEAD = Tag.valueOf("thead");
 private static final Tag TFOOT = Tag.valueOf("tfoot");
 private static final Tag TBODY = Tag.valueOf("tbody");
 private static final Tag COLGROUP = Tag.valueOf("colgroup");
 private static final Tag COL = Tag.valueOf("col");
 private static final Tag TR = Tag.valueOf("tr");
 private static final Tag TH = Tag.valueOf("th");
 private static final Tag TD = Tag.valueOf("td");
 private static final Tag VIDEO = Tag.valueOf("video");
 private static final Tag AUDIO = Tag.valueOf("audio");
 private static final Tag CANVAS = Tag.valueOf("canvas");
 private static final Tag DETAILS = Tag.valueOf("details");
 private static final Tag MENU = Tag.valueOf("menu");
 private static final Tag PLAINTEXT = Tag.valueOf("plaintext");

 @Test
 public void testTagValues() {
 // Test getting a tag by value
 Tag html = Tag.valueOf("html");
 Tag p = Tag.valueOf("p");
 Tag unknown = Tag.valueOf("unknown");

 // Test known tags
 assertTrue(html.isKnownTag());
 assertTrue(p.isKnownTag());
 // Test unknown tag
 assertFalse(unknown.isKnownTag());
 }

 @Test
 public void testIsBlock() {
 // Test known block tags
 assertTrue(HTML.isBlock());
 assertTrue(HEAD.isBlock());
 assertTrue(BODY.isBlock());
 // Test known inline tags
 assertFalse(P.isBlock());
 assertFalse(DIV.isBlock());
 // Test empty tags
 assertTrue(SCRIPT.isEmpty());
 assertTrue(IMG.isEmpty());
 assertTrue(BR.isEmpty());
 // Test format as block tags
 assertTrue(PRE.formatAsBlock());
 // Test other tags
 assertFalse(NOSCRIPT.formatAsBlock());
 assertFalse(STYLE.formatAsBlock());
 }

 @Test
 public void testCanContainBlock() {
 // Test known block tags that can contain block tags
 assertTrue(HTML.canContainBlock());
 assertTrue(HEAD.canContainBlock());
 assertTrue(BODY.canContainBlock());
 // Test known inline tags that cannot contain block tags
 assertFalse(P.canContainBlock());
 assertFalse(DIV.canContainBlock());
 // Test empty tags that cannot contain block tags
 assertFalse(SCRIPT.canContainBlock());
 assertFalse(IMG.canContainBlock());
 assertFalse(BR.canContainBlock());
 // Test format as block tags that can contain block tags
 assertTrue(PRE.canContainBlock());
 // Test other tags
 assertFalse(NOSCRIPT.canContainBlock());
 assertFalse(STYLE.canContainBlock());
 }

 @Test
 public void testCanContainInline() {
 // Test known block tags that cannot contain inline elements
 assertFalse(HTML.canContainInline());
 assertFalse(HEAD.canContainInline());
 assertFalse(BODY.canContainInline());
 // Test known inline tags that can contain inline elements
 assertTrue(P.canContainInline());
 assertTrue(DIV.canContainInline());
 // Test empty tags that cannot contain inline elements
 assertFalse(SCRIPT.canContainInline());
 assertFalse(IMG.canContainInline());
 assertFalse(BR.canContainInline());
 // Test format as block tags that can contain inline elements
 assertTrue(PRE.canContainInline());
 // Test other tags
 assertFalse(NOSCRIPT.canContainInline());
 assertFalse(STYLE.canContainInline());
 }

 @Test
 public void testIsData() {
 // Test known block tags that are data tags
 assertFalse(HTML.isData());
 assertFalse(HEAD.isData());
 assertFalse(BODY.isData());
 // Test known inline tags that are data tags
 assertFalse(P.isData());
 assertFalse(DIV.isData());
 // Test empty tags that are data tags
 assertFalse(SCRIPT.isData());
 assertFalse(IMG.isData());
 assertFalse(BR.isData());
 // Test format as block tags that are data tags
 assertFalse(PRE.isData());
 // Test other tags that are data tags
 assertTrue(META.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test known block tags that are empty
 assertFalse(HTML.isEmpty());
 assertFalse(HEAD.isEmpty());
 assertFalse(BODY.isEmpty());
 // Test known inline tags that are empty
 assertFalse(P.isEmpty());
 assertFalse(DIV.isEmpty());
 // Test empty tags
 assertTrue(SCRIPT.isEmpty());
 assertTrue(IMG.isEmpty());
 assertTrue(BR.isEmpty());
 // Test format as block tags that are empty
 assertFalse(PRE.isEmpty());
 // Test other tags that are empty
 assertTrue(NOSCRIPT.isEmpty());
 assertTrue(META.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 // Test known block tags that are self-closing
 assertFalse(HTML.isSelfClosing());
 assertFalse(HEAD.isSelfClosing());
 assertFalse(BODY.isSelfClosing());
 // Test known inline tags that are self-closing
 assertFalse(P.isSelfClosing());
 assertFalse(DIV.isSelfClosing());
 // Test empty tags that are self-closing
 assertFalse(SCRIPT.isSelfClosing());
 assertFalse(IMG.isSelfClosing());
 assertFalse(BR.isSelfClosing());
 // Test format as block tags that are self-closing
 assertFalse(PRE.isSelfClosing());
 // Test other tags that are self-closing
 assertFalse(NOSCRIPT.isSelfClosing());
 assertFalse(META.isSelfClosing());
 // Test tags that should be self-closing
 assertTrue(IMG.setSelfClosing().isSelfClosing());
 assertTrue(BR.setSelfClosing().isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // Test known HTML tags
 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("head"));
 assertTrue(Tag.isKnownTag("body"));
 // Test other HTML tags
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testPreserveWhitespace() {
 // Test known tags that preserve whitespace
 assertTrue(PRE.preserveWhitespace());
 assertTrue(PLAINTEXT.preserveWhitespace());
 // Test known tags that do not preserve whitespace
 assertFalse(P.preserveWhitespace());
 assertFalse(DIV.preserveWhitespace());
 }
}