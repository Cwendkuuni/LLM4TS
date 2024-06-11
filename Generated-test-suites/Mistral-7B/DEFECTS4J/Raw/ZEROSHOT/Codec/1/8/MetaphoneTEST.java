package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {

 Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String actual = metaphone.metaphone("");
 String expected = "";
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleCharacter() {
 String actual = metaphone.metaphone("A");
 String expected = "A";
 assertEquals(expected, actual);
 }

 @Test
 public void testMetaphoneWithInitialExceptions() {
 String[] inputs = new String[]{"KNIGHT", "APPLE", "WHO", "KANGAROO", "ACTION", "XYZ"};
 String[] outputs = new String[]{"N1T4", "P0L1", "H00", "A000X", "SH1X", ""};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String output = metaphone.metaphone(input);
 assertEquals(outputs[i], output);
 }
 }

 @Test
 public void testMetaphoneWithVowels() {
 String[] inputs = new String[]{"APPLE", "BANANA", "CAT", "DOG", "EGG", "FOOT", "GRAPE", "HAMMER", "ICE", "JACK",
 "KIWI", "LION", "MOUSE", "NUT", "ORANGE", "POTATO", "QUICK", "RAT", "Umbrella", "VANILLA", "WOLF", "XMAS", "YELLOW", "ZOO"};
 String[] outputs = new String[]{"P1L1", "B1N1N1", "K1T1", "D1G1", "E2G2", "F00T", "G1P1", "H1M1M1", "I2S1", "J1K1",
 "K1W1", "L1O1N1", "M1U1S1", "N1T1", "O1N1G1", "P0T1T1", "K1K1", "R1T1", "U1M1B1L1", "V1N1L1", "W1L1F1", "X1M1M1", "Y1L1L0", "Z1O1"};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String output = metaphone.metaphone(input);
 assertEquals(outputs[i], output);
 }
 }

 @Test
 public void testMetaphoneWithDuplicateLetters() {
 String[] inputs = new String[]{"BABABA", "CATCAT", "DOGDOG", "EGGYEGG"};
 String[] outputs = new String[]{"B2B2B2", "T1T1T1", "G2G2G2", "E2G2E2G2"};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String output = metaphone.metaphone(input);
 assertEquals(outputs[i], output);
 }
 }

 @Test
 public void testMetaphoneWithSpecialCharacters() {
 String[] inputs = new String[]{"APPLE$", "BANANA!", "CAT%", "DOG#", "EGG@", "FOOT^", "GRAPE&", "HAMMER*", "ICE?",
 "JACK!", "KIWI#", "LION%", "MOUSE@", "NUT^", "ORANGE&", "POTATO*", "QUICK$", "RAT!", "Umbrella@", "VANILLA#",
 "WOLF%", "XMAS*", "YELLOW&", "ZOO!"};
 String[] outputs = new String[]{"P1L1", "B1N1N1", "K1T1", "D1G1", "E2G2", "F00T", "G1P1", "H1M1M1", "I2S1", "J1K1",
 "K1W1", "L1O1N1", "M1U1S1", "N1T1", "O1N1G1", "P0T1T1", "K1K1", "R1T1", "U1M1B1L1", "V1N1L1", "W1L1F1", "X1M1M1",
 "Y1L1L0", "Z1O1"};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String output = metaphone.metaphone(input);
 assertEquals(outputs[i], output);
 }
 }

 @Test
 public void testMetaphoneWithNumbers() {
 String[] inputs = new String[]{"APPLE1", "BANANA2", "CAT3", "DOG4", "EGG5", "FOOT6", "GRAPE7", "HAMMER8", "ICE9",
 "JACK10", "KIWI11", "LION12", "MOUSE13", "NUT14", "ORANGE15", "POTATO16", "QUICK17", "RAT18", "Umbrella19",
 "VANILLA20", "WOLF21", "XMAS22", "YELLOW23", "ZOO24"};
 String[] outputs = new String[]{"P1L1", "B1N1N1", "K1T1", "D1G1", "E2G2", "F00T", "G1P1", "H1M1M1", "I2S1", "J1K1",
 "K1W1", "L1O1N1", "M1U1S1", "N1T1", "O1N1G1", "P0T1T1", "K1K1", "R1T1", "U1M1B1L1", "V1N1L1", "W1L1F1", "X1M1M1",
 "Y1L1L0", "Z1O1"};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String output = metaphone.metaphone(input);
 assertEquals(outputs[i], output);
 }
 }

 @Test
 public void testMetaphoneWithEmptyStringAsArgument() {
 String input = "";
 String expected = "";
 String actual = metaphone.metaphone(input);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testMetaphoneWithNullString() {
 String input = null;
 String expected = "";
 String actual = "";
 try {
 actual = metaphone.metaphone(input);
 } catch (EncoderException e) {
 // expected
 }
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testMetaphoneWithNullObject() {
 Object input = null;
 try {
 String output = metaphone.encode(input);
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testEncodingObjects() {
 String input1 = "APPLE";
 String expected1 = "P1L1";
 Object obj1 = input1;
 String actual1 = metaphone.encode(obj1);
 assertEquals(expected1, actual1);

 String input2 = "BANANA";
 String expected2 = "B1N1N1";
 Object obj2 = input2;
 String actual2 = metaphone.encode(obj2);
 assertEquals(expected2, actual2);
 }

 @Test
 public void testIsMetaphoneEqualWithEmptyStrings() {
 String string1 = "";
 String string2 = "";
 boolean actual = metaphone.isMetaphoneEqual(string1, string2);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsMetaphoneEqualWithEquivalentStrings() {
 String string1 = "APPLE";
 String string2 = "APPLE";
 boolean actual = metaphone.isMetaphoneEqual(string1, string2);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsMetaphoneEqualWithDifferentStrings() {
 String string1 = "APPLE";
 String string2 = "BANANA";
 boolean actual = metaphone.isMetaphoneEqual(string1, string2);
 boolean expected = false;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxCodeLen() {
 int actual = metaphone.getMaxCodeLen();
 int expected = 4;
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMaxCodeLen() {
 int newMaxCodeLen = 5;
 metaphone.setMaxCodeLen(newMaxCodeLen);
 int actual = metaphone.getMaxCodeLen();
 int expected = 5;
 assertEquals(expected, actual);
 }

}