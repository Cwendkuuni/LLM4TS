package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslate_ValidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&amp; #65 &apos; &#x41; &#X41; &#032; &#x{1F600};";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 int actual = unescaper.translate(input, index, out);

 // Check the number of characters written to the Writer
 assertEquals(13, actual);
 }

 @Test(expected = IOException.class)
 public void testTranslate_InvalidInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 unescaper.translate(input, index, out);
 }

 @Test
 public void testTranslate_HexNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&amp; #x41; &#x{20AC};";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 int actual = unescaper.translate(input, index, out);

 // Check the number of characters written to the Writer
 assertEquals(3, actual);
 }

 @Test
 public void testTranslate_LargeNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&amp; #10FFF0;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 int actual = unescaper.translate(input, index, out);

 // Check the number of characters written to the Writer
 assertEquals(4, actual); // 1 for the '&', 2 for the Unicode characters, and 1 for the ';'
 }

 @Test(expected = NumberFormatException.class)
 public void testTranslate_InvalidNumericEntity() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "&amp; #abcd;";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 unescaper.translate(input, index, out);
 }

 @Test
 public void testTranslate_EmptyInput() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 CharSequence input = "";
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // This writer does nothing, just for the sake of the test
 }
 };
 int index = 0;
 int length = input.length();
 int actual = unescaper.translate(input, index, out);

 // Check the return value is 0
 assertEquals(0, actual);
 }
}