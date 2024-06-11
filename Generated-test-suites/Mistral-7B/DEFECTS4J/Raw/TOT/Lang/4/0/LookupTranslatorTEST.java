package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

 private static final String SHORT_LOOKUP[][] = {{"hello", "hi"}, {"world", "earth"}};
 private static final String LONG_LOOKUP[][] = {{"thisisareallylonginputsequencefortestingpurposes", "thisisareallylongoutputsequencefortestingpurposes"}};

 @Test
 public void testShortLookup() {
 LookupTranslator translator = new LookupTranslator(SHORT_LOOKUP);
 String input = "hello world";
 String expected = "hi earth";
 String output = "";
 try {
 output = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException thrown during translation");
 }
 assertEquals(expected, output);
 }

 @Test
 public void testLongLookup() {
 LookupTranslator translator = new LookupTranslator(LONG_LOOKUP);
 String input = "thisisareallylonginputsequencefortestingpurposes";
 String expected = "thisisareallylongoutputsequencefortestingpurposes";
 String output = "";
 try {
 output = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException thrown during translation");
 }
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 LookupTranslator translator = new LookupTranslator(SHORT_LOOKUP);
 String input = null;
 try {
 translator.translate(input, 0, new StringWriter());
 } catch (IOException e) {
 // expected IOException
 } catch (IllegalArgumentException e) {
 fail("IllegalArgumentException not thrown for null input");
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 LookupTranslator translator = new LookupTranslator(SHORT_LOOKUP);
 String input = "helloworld";
 try {
 translator.translate(CharSequenceUtils.subSequence(input, 7, input.length()), 0, new StringWriter());
 } catch (IOException e) {
 // expected IOException
 } catch (IndexOutOfBoundsException e) {
 fail("IndexOutOfBoundsException not thrown for out-of-bounds index");
 }
 }

 @Test
 public void testEmptyInput() {
 LookupTranslator translator = new LookupTranslator(SHORT_LOOKUP);
 String input = "";
 String output = "";
 try {
 output = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException thrown during translation");
 }
 assertEquals(output, "");
 }

 @Test
 public void testEmptyLookupTable() {
 LookupTranslator translator = new LookupTranslator(null);
 String input = "helloworld";
 String output = "";
 try {
 output = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, new StringWriter());
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException thrown during translation");
 }
 assertEquals(output, "");
 }
}