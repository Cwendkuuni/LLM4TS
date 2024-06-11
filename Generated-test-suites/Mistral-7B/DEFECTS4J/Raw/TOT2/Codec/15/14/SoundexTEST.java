package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class
 */
public class SoundexTest {

 private final Soundex soundex = new Soundex();

 /**
 * Test method for {@link Soundex#encode(String)}
 */
 @Test
 public void testEncodeString() {
 String input = "Apple";
 String expected = "K2311";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 /**
 * Test method for {@link Soundex#encode(String)} with empty String
 */
 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String output = soundex.encode(input);
 assertEquals(input, output);
 }

 /**
 * Test method for {@link Soundex#encode(String)} with null String
 */
 @Test(expected = EncoderException.class)
 public void testEncodeNullString() {
 String input = null;
 soundex.encode(input);
 }

 /**
 * Test method for {@link Soundex#difference(String, String)}
 */
 @Test
 public void testDifference() {
 String input1 = "Apple";
 String input2 = "Orange";
 int expected = 0;
 int output = soundex.difference(input1, input2);
 assertEquals(expected, output);
 }

 /**
 * Test method for {@link Soundex#difference(String, String)} with same strings
 */
 @Test
 public void testDifferenceSameStrings() {
 String input1 = "Apple";
 String input2 = "Apple";
 int expected = 4;
 int output = soundex.difference(input1, input2);
 assertEquals(expected, output);
 }

 /**
 * Test method for {@link Soundex#difference(String, String)} with edge cases
 */
 @Test
 public void testDifferenceEdgeCases() {
 String input1 = "Apple";
 String input2 = "Application";
 int expected = 3;
 int output = soundex.difference(input1, input2);
 assertEquals(expected, output);
 }

 /**
 * Test method for {@link Soundex#difference(String, String)} with exceptions
 */
 @Test(expected = EncoderException.class)
 public void testDifferenceExceptions() {
 String input1 = null;
 String input2 = "Orange";
 soundex.difference(input1, input2);
 }
}