package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Random;

public class RandomStringUtilsTest {

 @Test
 public void testRandom() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();

 // Test empty string
 assertEquals("", randomStringUtils.random(0));

 // Test random string of length 1
 String randomString1 = randomStringUtils.random(1);
 assertNotNull(randomString1);
 assertEquals(randomString1.length(), 1);

 // Test random string of length 10
 String randomString10 = randomStringUtils.random(10);
 assertNotNull(randomString10);
 assertEquals(randomString10.length(), 10);

 // Test random alphabetic string of length 10
 String randomAlphabetic10 = randomStringUtils.randomAlphabetic(10);
 assertNotNull(randomAlphabetic10);
 assertEquals(randomAlphabetic10.length(), 10);

 // Test random alphanumeric string of length 10
 String randomAlphanumeric10 = randomStringUtils.randomAlphanumeric(10);
 assertNotNull(randomAlphanumeric10);
 assertEquals(randomAlphanumeric10.length(), 10);

 // Test random numeric string of length 10
 String randomNumeric10 = randomStringUtils.randomNumeric(10);
 assertNotNull(randomNumeric10);
 assertEquals(randomNumeric10.length(), 10);
 }

 @Test
 public void testRandomAscii() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();

 // Test empty string
 assertEquals("", randomStringUtils.randomAscii(0));

 // Test random string of length 1
 String randomAscii1 = randomStringUtils.randomAscii(1);
 assertNotNull(randomAscii1);
 assertEquals(randomAscii1.length(), 1);

 // Test random string of length 10
 String randomAscii10 = randomStringUtils.randomAscii(10);
 assertNotNull(randomAscii10);
 assertEquals(randomAscii10.length(), 10);
 }

 @Test
 public void testRandomAlphabetic() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();

 // Test empty string
 assertEquals("", randomStringUtils.randomAlphabetic(0));

 // Test random string of length 1
 String randomAlphabetic1 = randomStringUtils.randomAlphabetic(1);
 assertNotNull(randomAlphabetic1);
 assertEquals(randomAlphabetic1.length(), 1);

 // Test random string of length 10
 String randomAlphabetic10 = randomStringUtils.randomAlphabetic(10);
 assertNotNull(randomAlphabetic10);
 assertEquals(randomAlphabetic10.length(), 10);
 }

 @Test
 public void testRandomAlphanumeric() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();

 // Test empty string
 assertEquals("", randomStringUtils.randomAlphanumeric(0));

 // Test random string of length 1
 String randomAlphanumeric1 = randomStringUtils.randomAlphanumeric(1);
 assertNotNull(randomAlphanumeric1);
 assertEquals(randomAlphanumeric1.length(), 1);

 // Test random string of length 10
 String randomAlphanumeric10 = randomStringUtils.randomAlphanumeric(10);
 assertNotNull(randomAlphanumeric10);
 assertEquals(randomAlphanumeric10.length(), 10);
 }

 @Test
 public void testRandomNumeric() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();

 // Test empty string
 assertEquals("", randomStringUtils.randomNumeric(0));

 // Test random string of length 1
 String randomNumeric1 = randomStringUtils.randomNumeric(1);
 assertNotNull(randomNumeric1);
 assertEquals(randomNumeric1.length(), 1);

 // Test random string of length 10
 String randomNumeric10 = randomStringUtils.randomNumeric(10);
 assertNotNull(randomNumeric10);
 assertEquals(randomNumeric10.length(), 10);
 }

 @Test
 public void testRandomCustom() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();
 Random random = new Random();

 // Test empty string
 assertEquals("", randomStringUtils.random(0, random));

 // Test random string of length 1
 String randomCustom1 = randomStringUtils.random(1, random);
 assertNotNull(randomCustom1);
 assertEquals(randomCustom1.length(), 1);

 // Test random string of length 10 with custom start and end
 String randomCustom10 = randomStringUtils.random(10, 1, 3, random);
 assertNotNull(randomCustom10);
 assertEquals(randomCustom10.length(), 10);

 // Test random string of length 10 with custom set of characters
 String randomCustomChars10 = randomStringUtils.random("abc", random);
 assertNotNull(randomCustomChars10);
 assertEquals(randomCustomChars10.length(), 10);
 }

 @Test
 public void testRandomCustomWithArray() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();
 Random random = new Random();

 // Test empty string
 assertEquals("", randomStringUtils.random(0, random, new char[0]));

 // Test random string of length 1
 String randomCustomArray1 = randomStringUtils.random(1, random, new char[]{'a'});
 assertNotNull(randomCustomArray1);
 assertEquals(randomCustomArray1.length(), 1);

 // Test random string of length 10 with custom set of characters
 String randomCustomArray10 = randomStringUtils.random(10, random, new char[]{'a', 'b', 'c'});
 assertNotNull(randomCustomArray10);
 assertEquals(randomCustomArray10.length(), 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRandomNegativeLength() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();
 randomStringUtils.random(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRandomEmptyCustomSet() {
 RandomStringUtils randomStringUtils = new RandomStringUtils();
 Random random = new Random();
 randomStringUtils.random(10, random, new char[0]);
 }
}