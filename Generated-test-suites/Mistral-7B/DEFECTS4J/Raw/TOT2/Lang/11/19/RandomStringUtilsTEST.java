package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

 // Test for random method
 @Test
 public void testRandom() {
 String randomStr = RandomStringUtils.random(10);
 assertEquals(10, randomStr.length());
 }

 // Test for randomAscii method
 @Test
 public void testRandomAscii() {
 String randomAsciiStr = RandomStringUtils.randomAscii(10);
 assertEquals(10, randomAsciiStr.length());
 for (char c : randomAsciiStr.toCharArray()) {
 assert(c >= 32 && c <= 126);
 }
 }

 // Test for randomAlphabetic method
 @Test
 public void testRandomAlphabetic() {
 String randomAlphabeticStr = RandomStringUtils.randomAlphabetic(10);
 assertEquals(10, randomAlphabeticStr.length());
 for (char c : randomAlphabeticStr.toCharArray()) {
 assert(Character.isAlphabetic(c));
 }
 }

 // Test for randomAlphanumeric method
 @Test
 public void testRandomAlphanumeric() {
 String randomAlphanumericStr = RandomStringUtils.randomAlphanumeric(10);
 assertEquals(10, randomAlphanumericStr.length());
 for (char c : randomAlphanumericStr.toCharArray()) {
 assert(Character.isAlphabetic(c) || Character.isDigit(c));
 }
 }

 // Test for randomNumeric method
 @Test
 public void testRandomNumeric() {
 String randomNumericStr = RandomStringUtils.randomNumeric(10);
 assertEquals(10, randomNumericStr.length());
 for (char c : randomNumericStr.toCharArray()) {
 assert(Character.isDigit(c));
 }
 }

 // Test for random method with custom length, start, end, letters and numbers
 @Test
 public void testRandomCustom() {
 char[] chars = {'a', 'e', 'i', 'o', 'u'};
 String randomCustomStr = RandomStringUtils.random(10, 0, 5, true, false, chars, RandomStringUtils.RANDOM);
 assertEquals(10, randomCustomStr.length());
 for (char c : randomCustomStr.toCharArray()) {
 if (chars.contains(c)) {
 assert(true);
 } else {
 assertFalse(true);
 }
 }
 }

 // Test for random method with custom length, start, end, letters and numbers, and custom random
 @Test
 public void testRandomCustomRandom() {
 char[] chars = {'a', 'e', 'i', 'o', 'u'};
 Random customRandom = new Random();
 String randomCustomStr = RandomStringUtils.random(10, 0, 5, true, false, chars, customRandom);
 assertEquals(10, randomCustomStr.length());
 for (char c : randomCustomStr.toCharArray()) {
 if (chars.contains(c)) {
 assert(true);
 } else {
 assertFalse(true);
 }
 }
 }

 // Test for random method with negative count
 @Test(expected = IllegalArgumentException.class)
 public void testRandomNegativeCount() {
 RandomStringUtils.random(-1);
 }

 // Test for random method with empty chars array
 @Test(expected = IllegalArgumentException.class)
 public void testRandomEmptyChars() {
 RandomStringUtils.random(10, (char[]) null);
 }
}