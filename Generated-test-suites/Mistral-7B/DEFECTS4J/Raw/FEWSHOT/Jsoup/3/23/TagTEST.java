package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

 Tag pTag = Tag.valueOf("p");
 Tag headTag = Tag.valueOf("head");
 Tag bodyTag = Tag.valueOf("body");
 Tag htmlTag = Tag.valueOf("HTML");
 Tag titleTag = Tag.valueOf("title");
 Tag scriptTag = Tag.valueOf("script");
 Tag linkTag = Tag.valueOf("link");
 Tag metaTag = Tag.valueOf("meta");
 Tag imgTag = Tag.valueOf("img");
 Tag formTag = Tag.valueOf("FORM");
 Tag inputTag = Tag.valueOf("INPUT");
 Tag selectTag = Tag.valueOf("SELECT");
 Tag textareaTag = Tag.valueOf("TEXTAREA");
 Tag labelTag = Tag.valueOf("LABEL");
 Tag buttonTag = Tag.valueOf("BUTTON");
 Tag optgroupTag = Tag.valueOf("OPTGROUP");
 Tag optionTag = Tag.valueOf("OPTION");
 Tag fieldsetTag = Tag.valueOf("FIELDSET");
 Tag legendTag = Tag.valueOf("LEGEND");
 Tag tableTag = Tag.valueOf("TABLE");
 Tag captionTag = Tag.valueOf("CAPTION");
 Tag theadTag = Tag.valueOf("THEAD");
 Tag tfootTag = Tag.valueOf("TFOOT");
 Tag tbodyTag = Tag.valueOf("TBODY");
 Tag colgroupTag = Tag.valueOf("COLGROUP");
 Tag colTag = Tag.valueOf("COL");
 Tag trTag = Tag.valueOf("TR");
 Tag thTag = Tag.valueOf("TH");
 Tag tdTag = Tag.valueOf("TD");

 @Test
 public void testCanContain() {
 // Test pTag can contain different tags
 assertTrue(pTag.canContain(pTag));
 assertTrue(pTag.canContain(headTag));
 assertTrue(pTag.canContain(bodyTag));
 assertTrue(pTag.canContain(htmlTag));
 assertFalse(pTag.canContain(pTag)); // cannot contain itself

 // Test headTag can contain different tags
 assertTrue(headTag.canContain(metaTag));
 assertTrue(headTag.canContain(titleTag));
 assertTrue(headTag.canContain(scriptTag));
 assertFalse(headTag.canContain(linkTag)); // cannot contain itself

 // Test bodyTag can contain different tags
 assertTrue(bodyTag.canContain(pTag));
 assertTrue(bodyTag.canContain(formTag));
 assertTrue(bodyTag.canContain(inputTag));
 assertTrue(bodyTag.canContain(selectTag));
 assertTrue(bodyTag.canContain(textareaTag));
 assertTrue(bodyTag.canContain(labelTag));
 assertTrue(bodyTag.canContain(buttonTag));
 assertTrue(bodyTag.canContain(tableTag));
 assertTrue(bodyTag.canContain(trTag));
 assertTrue(bodyTag.canContain(thTag));
 assertTrue(bodyTag.canContain(tdTag));
 assertFalse(bodyTag.canContain(pTag)); // cannot contain itself

 // Test htmlTag can contain different tags
 assertTrue(htmlTag.canContain(headTag));
 assertTrue(htmlTag.canContain(bodyTag));
 assertTrue(htmlTag.canContain(pTag));
 assertFalse(htmlTag.canContain(htmlTag)); // cannot contain itself

 // Test titleTag can contain different tags
 assertFalse(titleTag.canContain(pTag)); // cannot contain pTag
 assertFalse(titleTag.canContain(titleTag)); // cannot contain itself

 // Test scriptTag can contain different tags
 assertFalse(scriptTag.canContain(pTag)); // cannot contain pTag
 assertFalse(scriptTag.canContain(scriptTag)); // cannot contain itself

 // Test linkTag can contain different tags
 assertFalse(linkTag.canContain(pTag)); // cannot contain pTag
 assertFalse(linkTag.canContain(linkTag)); // cannot contain itself

 // Test metaTag can contain different tags
 assertFalse(metaTag.canContain(pTag)); // cannot contain pTag
 assertFalse(metaTag.canContain(metaTag)); // cannot contain itself

 // Test imgTag can contain different tags
 assertFalse(imgTag.canContain(pTag)); // cannot contain pTag
 assertFalse(imgTag.canContain(imgTag)); // cannot contain itself

 // Test formTag can contain different tags
 assertTrue(formTag.canContain(inputTag));
 assertTrue(formTag.canContain(selectTag));
 assertTrue(formTag.canContain(textareaTag));
 assertTrue(formTag.canContain(labelTag));
 assertTrue(formTag.canContain(buttonTag));
 assertTrue(formTag.canContain(fieldsetTag));
 assertTrue(formTag.canContain(legendTag));
 assertFalse(formTag.canContain(formTag)); // cannot contain itself

 // Test inputTag can contain different tags
 assertFalse(inputTag.canContain(pTag)); // cannot contain pTag
 assertFalse(inputTag.canContain(inputTag)); // cannot contain itself

 // Test selectTag can contain different tags
 assertFalse(selectTag.canContain(pTag)); // cannot contain pTag
 assertTrue(selectTag.canContain(optgroupTag));
 assertTrue(selectTag.canContain(optionTag));
 assertFalse(selectTag.canContain(selectTag)); // cannot contain itself

 // Test textareaTag can contain different tags
 assertFalse(textareaTag.canContain(pTag)); // cannot contain pTag
 assertFalse(textareaTag.canContain(textareaTag)); // cannot contain itself

 // Test labelTag can contain different tags
 assertFalse(labelTag.canContain(pTag)); // cannot contain pTag
 assertFalse(labelTag.canContain(labelTag)); // cannot contain itself

 // Test buttonTag can contain different tags
 assertFalse(buttonTag.canContain(pTag)); // cannot contain pTag
 assertFalse(buttonTag.canContain(buttonTag)); // cannot contain itself

 // Test optgroupTag can contain different tags
 assertFalse(optgroupTag.canContain(pTag)); // cannot contain pTag
 assertTrue(optgroupTag.canContain(optionTag));
 assertFalse(optgroupTag.canContain(optgroupTag)); // cannot contain itself

 // Test optionTag can contain different tags
 assertFalse(optionTag.canContain(pTag)); // cannot contain pTag
 assertFalse(optionTag.canContain(optionTag)); // cannot contain itself

 // Test fieldsetTag can contain different tags
 assertTrue(fieldsetTag.canContain(legendTag));
 assertTrue(fieldsetTag.canContain(inputTag));
 assertTrue(fieldsetTag.canContain(selectTag));
 assertTrue(fieldsetTag.canContain(textareaTag));
 assertTrue(fieldsetTag.canContain(labelTag));
 assertTrue(fieldsetTag.canContain(buttonTag));
 assertFalse(fieldsetTag.canContain(fieldsetTag)); // cannot contain itself

 // Test legendTag can contain different tags
 assertFalse(legendTag.canContain(pTag)); // cannot contain pTag
 assertFalse(legendTag.canContain(legendTag)); // cannot contain itself
 }

 @Test
 public void testIsBlockOrInline() {
 // Test isBlock() for different tags
 assertTrue(pTag.isBlock());
 assertTrue(headTag.isBlock());
 assertTrue(bodyTag.isBlock());
 assertTrue(htmlTag.isBlock());
 assertFalse(titleTag.isBlock());
 assertFalse(scriptTag.isBlock());
 assertFalse(linkTag.isBlock());
 assertFalse(metaTag.isBlock());
 assertFalse(imgTag.isBlock());
 assertFalse(formTag.isBlock());
 assertFalse(inputTag.isBlock());
 assertFalse(selectTag.isBlock());
 assertFalse(textareaTag.isBlock());
 assertFalse(labelTag.isBlock());
 assertFalse(buttonTag.isBlock());
 assertFalse(optgroupTag.isBlock());
 assertFalse(optionTag.isBlock());
 assertFalse(fieldsetTag.isBlock());
 assertFalse(legendTag.isBlock());

 // Test isInline() for different tags
 assertFalse(pTag.isInline());
 assertFalse(headTag.isInline());
 assertFalse(bodyTag.isInline());
 assertFalse(htmlTag.isInline());
 assertTrue(titleTag.isInline());
 assertTrue(scriptTag.isInline());
 assertTrue(linkTag.isInline());
 assertTrue(metaTag.isInline());
 assertFalse(imgTag.isInline());
 assertFalse(formTag.isInline());
 assertFalse(inputTag.isInline());
 assertFalse(selectTag.isInline());
 assertFalse(textareaTag.isInline());
 assertTrue(labelTag.isInline());
 assertTrue(buttonTag.isInline());
 assertTrue(optgroupTag.isInline());
 assertTrue(optionTag.isInline());
 assertTrue(fieldsetTag.isInline());
 assertTrue(legendTag.isInline());
 }

 @Test
 public void testIsData() {
 // Test isData() for different tags
 assertFalse(pTag.isData());
 assertFalse(headTag.isData());
 assertFalse(bodyTag.isData());
 assertFalse(htmlTag.isData());
 assertFalse(titleTag.isData());
 assertFalse(scriptTag.isData());
 assertFalse(linkTag.isData());
 assertFalse(metaTag.isData());
 assertTrue(imgTag.isData());
 assertFalse(formTag.isData());
 assertFalse(inputTag.isData());
 assertFalse(selectTag.isData());
 assertFalse(textareaTag.isData());
 assertFalse(labelTag.isData());
 assertFalse(buttonTag.isData());
 assertFalse(optgroupTag.isData());
 assertFalse(optionTag.isData());
 assertFalse(fieldsetTag.isData());
 assertFalse(legendTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty() for different tags
 assertFalse(pTag.isEmpty());
 assertFalse(headTag.isEmpty());
 assertFalse(bodyTag.isEmpty());
 assertFalse(htmlTag.isEmpty());
 assertFalse(titleTag.isEmpty());
 assertFalse(scriptTag.isEmpty());
 assertFalse(linkTag.isEmpty());
 assertFalse(metaTag.isEmpty());
 assertFalse(imgTag.isEmpty());
 assertFalse(formTag.isEmpty());
 assertFalse(inputTag.isEmpty());
 assertFalse(selectTag.isEmpty());
 assertFalse(textareaTag.isEmpty());
 assertFalse(labelTag.isEmpty());
 assertFalse(buttonTag.isEmpty());
 assertFalse(optgroupTag.isEmpty());
 assertFalse(optionTag.isEmpty());
 assertFalse(fieldsetTag.isEmpty());
 assertFalse(legendTag.isEmpty());
 assertFalse(imgTag.isEmpty());
 }

 @Test
 public void testPreserveWhitespace() {
 // Test preserveWhitespace() for different tags
 assertFalse(pTag.preserveWhitespace());
 assertFalse(headTag.preserveWhitespace());
 assertFalse(bodyTag.preserveWhitespace());
 assertFalse(htmlTag.preserveWhitespace());
 assertFalse(titleTag.preserveWhitespace());
 assertFalse(scriptTag.preserveWhitespace());
 assertFalse(linkTag.preserveWhitespace());
 assertFalse(metaTag.preserveWhitespace());
 assertFalse(imgTag.preserveWhitespace());
 assertFalse(formTag.preserveWhitespace());
 assertFalse(inputTag.preserveWhitespace());
 assertFalse(selectTag.preserveWhitespace());
 assertFalse(textareaTag.preserveWhitespace());
 assertFalse(labelTag.preserveWhitespace());
 assertFalse(buttonTag.preserveWhitespace());
 assertFalse(optgroupTag.preserveWhitespace());
 assertFalse(optionTag.preserveWhitespace());
 assertFalse(fieldsetTag.preserveWhitespace());
 assertFalse(legendTag.preserveWhitespace());
 assertTrue(pTag.preserveWhitespace("pre"));
 assertTrue(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace("textarea"));
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testEqualsAndHashcode() {
 // Test equals() and hashcode() for same and different Tag objects
 Tag tag1 = Tag.valueOf("p");
 Tag tag2 = Tag.valueOf("p");
 Tag tag3 = Tag.valueOf("div");

 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2);
 assertEquals(tag1.hashCode(), tag2.hashCode());
 assertNotEquals(tag1, tag3);
 assertNotEquals(tag1.hashCode(), tag3.hashCode());
 }

 @Test
 public void testToString() {
 // Test toString() method for different tags
 assertEquals("p", pTag.toString());
 assertEquals("head", headTag.toString());
 assertEquals("body", bodyTag.toString());
 assertEquals("HTML", htmlTag.toString());
 }
}