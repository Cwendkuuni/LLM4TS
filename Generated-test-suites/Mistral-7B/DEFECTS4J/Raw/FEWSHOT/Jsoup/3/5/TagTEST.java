package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

 Tag pTag = Tag.valueOf("p");
 Tag divTag = Tag.valueOf("div");
 Tag h1Tag = Tag.valueOf("h1");
 Tag ulTag = Tag.valueOf("ul");
 Tag liTag = Tag.valueOf("li");
 Tag tableTag = Tag.valueOf("table");
 Tag trTag = Tag.valueOf("tr");
 Tag thTag = Tag.valueOf("th");
 Tag tdTag = Tag.valueOf("td");
 Tag formTag = Tag.valueOf("form");
 Tag inputTag = Tag.valueOf("input");
 Tag selectTag = Tag.valueOf("select");
 Tag textareaTag = Tag.valueOf("textarea");
 Tag labelTag = Tag.valueOf("label");
 Tag buttonTag = Tag.valueOf("button");
 Tag optgroupTag = Tag.valueOf("optgroup");
 Tag optionTag = Tag.valueOf("option");
 Tag fieldsetTag = Tag.valueOf("fieldset");
 Tag legendTag = Tag.valueOf("legend");
 Tag areaTag = Tag.valueOf("area");
 Tag paramTag = Tag.valueOf("param");

 @Test
 public void testCanContain() {
 // p can contain div
 assertTrue(pTag.canContain(divTag));

 // div can contain p
 assertTrue(divTag.canContain(pTag));

 // p cannot contain p
 assertFalse(pTag.canContain(pTag));

 // h1 can contain p
 assertTrue(h1Tag.canContain(pTag));

 // p cannot contain h1
 assertFalse(pTag.canContain(h1Tag));

 // ul can contain li
 assertTrue(ulTag.canContain(liTag));

 // li can contain p
 assertTrue(liTag.canContain(pTag));

 // table can contain tr
 assertTrue(tableTag.canContain(trTag));

 // tr can contain td
 assertTrue(trTag.canContain(tdTag));

 // td can contain p
 assertTrue(tdTag.canContain(pTag));

 // form can contain input
 assertTrue(formTag.canContain(inputTag));

 // input can contain p
 assertFalse(inputTag.canContain(pTag));

 // select can contain option
 assertTrue(selectTag.canContain(optionTag));

 // option cannot contain select
 assertFalse(optionTag.canContain(selectTag));

 // label can contain input
 assertTrue(labelTag.canContain(inputTag));

 // button can contain input
 assertTrue(buttonTag.canContain(inputTag));

 // optgroup can contain option
 assertTrue(optgroupTag.canContain(optionTag));

 // fieldset can contain legend
 assertTrue(fieldsetTag.canContain(legendTag));

 // area cannot contain param
 assertFalse(areaTag.canContain(paramTag));

 // param cannot contain area
 assertFalse(paramTag.canContain(areaTag));
 }

 @Test
 public void testIsBlock() {
 // p is block
 assertTrue(pTag.isBlock());

 // div is block
 assertTrue(divTag.isBlock());

 // h1 is block
 assertTrue(h1Tag.isBlock());

 // ul is inline
 assertFalse(ulTag.isBlock());

 // li is inline
 assertFalse(liTag.isBlock());

 // table is block
 assertTrue(tableTag.isBlock());

 // tr is block
 assertTrue(trTag.isBlock());

 // th is inline
 assertFalse(thTag.isBlock());

 // td is inline
 assertFalse(tdTag.isBlock());

 // form is block
 assertTrue(formTag.isBlock());

 // input is inline
 assertFalse(inputTag.isBlock());

 // select is inline
 assertFalse(selectTag.isBlock());

 // textarea is block
 assertTrue(textareaTag.isBlock());

 // label is inline
 assertFalse(labelTag.isBlock());

 // button is block
 assertTrue(buttonTag.isBlock());

 // optgroup is inline
 assertFalse(optgroupTag.isBlock());

 // option is inline
 assertFalse(optionTag.isBlock());

 // fieldset is block
 assertTrue(fieldsetTag.isBlock());

 // legend is inline
 assertFalse(legendTag.isBlock());
 }

 @Test
 public void testIsData() {
 // p is not data
 assertFalse(pTag.isData());

 // script is data
 assertTrue(Tag.valueOf("script").isData());

 // textarea is data
 assertTrue(textareaTag.isData());
 }

 @Test
 public void testIsEmpty() {
 // img is empty
 assertTrue(Tag.valueOf("img").isEmpty());

// p is not empty
assertFalse(pTag.isEmpty());

 // input is empty when type="hidden"
 Tag hiddenInputTag = new Tag("input").setAttribute("type", "hidden");
 assertTrue(hiddenInputTag.isEmpty());

 // input is not empty when type="text"
 Tag textInputTag = new Tag("input").setAttribute("type", "text");
 assertFalse(textInputTag.isEmpty());
}

 @Test
 public void testIsValidParent() {
 // p can be parent of input
 assertTrue(pTag.isValidParent(inputTag));

 // input cannot be parent of p
 assertFalse(inputTag.isValidParent(pTag));

 // label can be parent of input
 assertTrue(labelTag.isValidParent(inputTag));

 // input cannot be parent of label
 assertFalse(inputTag.isValidParent(labelTag));
 }

 @Test
 public void testEqualsAndHashcode() {
 Tag tag1 = new Tag("p");
 Tag tag2 = new Tag("p");
 Tag tag3 = new Tag("div");

 assertEquals(tag1, tag1);
 assertEquals(tag1, tag2); // tag names are case insensitive
 assertNotEquals(tag1, tag3);

 // hashcodes are equal for equal objects
 int hashcode1 = tag1.hashCode();
 int hashcode2 = tag2.hashCode();
 assertEquals(hashcode1, hashcode2);

 // hashcodes are different for non-equal objects
 int hashcode3 = tag3.hashCode();
 assertNotEquals(hashcode1, hashcode3);
 }
}