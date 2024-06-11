package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

public class NumericEntityUnescaperTest {

 /**
 * Test translate method with valid numeric entities.
 */
 @Test
 public void testValidNumericEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 // Test entities without semicolon
 String input1 = "&#x41;Hello World&";
 String expected1 = "\u0041Hello World";
 String output1 =CharSequenceUtils.toString(unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input1.length();
 }

 @Override
 public char charAt(int index) {
 return input1.charAt(index);
 }
 }, 0, new StringBuilder()).toString());
 assertEquals(expected1, output1);

 // Test entities with semicolon
 String input2 = "&#[x41]";
 String expected2 = "\u0041";
 String output2 =CharSequenceUtils.toString(unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input2.length();
 }

 @Override
 public char charAt(int index) {
 return input2.charAt(index);
 }
 }, 0, new StringBuilder()).toString());
 assertEquals(expected2, output2);

 // Test entities with semicolon and uppercase X
 String input3 = "&#[X41]";
 String expected3 = "\u0041";
 String output3 =CharSequenceUtils.toString(unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input3.length();
 }

 @Override
 public char charAt(int index) {
 return input3.charAt(index);
 }
 }, 0, new StringBuilder()).toString());
 assertEquals(expected3, output3);

 // Test entities with multiple entities
 String input4 = "&#[x41]&#[x65]&#[x6C]&#[x6C]&#[x6F];Hello World&";
 String expected4 = "\u0041\u0065\u006C\u006C\u006FHello World";
 String output4 =CharSequenceUtils.toString(unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input4.length();
 }

 @Override
 public char charAt(int index) {
 return input4.charAt(index);
 }
 }, 0, new StringBuilder()).toString());
 assertEquals(expected4, output4);
 }

 /**
 * Test translate method with invalid input.
 */
 @Test(expected = NumberFormatException.class)
 public void testInvalidEntities() {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 String input1 = "&#[xABC]"; // Invalid hexadecimal value
 String input2 = "&#[123456789012345]"; // Too long for int
 String input3 = "&#[X1234ABC]"; // Mixed hex and decimal
 String input4 = "&#[xDEADBEEF]"; // Out of bounds unicode character
 String input5 = "&#[x0000];"; // Zero width character

 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input1.length();
 }

 @Override
 public char charAt(int index) {
 return input1.charAt(index);
 }
 }, 0, new StringBuilder());

 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input2.length();
 }

 @Override
 public char charAt(int index) {
 return input2.charAt(index);
 }
 }, 0, new StringBuilder());

 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input3.length();
 }

 @Override
 public char charAt(int index) {
 return input3.charAt(index);
 }
 }, 0, new StringBuilder());

 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input4.length();
 }

 @Override
 public char charAt(int index) {
 return input4.charAt(index);
 }
 }, 0, new StringBuilder());

 unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input5.length();
 }

 @Override
 public char charAt(int index) {
 return input5.charAt(index);
 }
 }, 0, new StringBuilder());
 }
}