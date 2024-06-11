package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link RandomStringUtils}.
 */
public class RandomStringUtilsTest {

    /**
     * Test random method with default character set.
     */
    @Test
    public void testRandom() {
        String randomString = RandomStringUtils.random(10);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertNotEquals("Random string should not contain duplicate characters.", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }

    /**
     * Test randomAscii method with default character set.
     */
    @Test
    public void testRandomAscii() {
        String randomAsciiString = RandomStringUtils.randomAscii(10);
        assertEquals(10, randomAsciiString.length());
        for (int i = 0; i < randomAsciiString.length(); i++) {
            assertTrue("Random ASCII string should only contain ASCII characters.", Character.isASCII(randomAsciiString.charAt(i)));
            assertNotEquals("Random ASCII string should not contain duplicate characters.", randomAsciiString.charAt(i), randomAsciiString.charAt(i + 1));
        }
    }

    /**
     * Test randomAlphabetic method with default character set.
     */
    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticString = RandomStringUtils.randomAlphabetic(10);
        assertEquals(10, randomAlphabeticString.length());
        for (int i = 0; i < randomAlphabeticString.length(); i++) {
            assertTrue("Random alphabetic string should only contain alphabetic characters.", Character.isAlphabetic(randomAlphabeticString.charAt(i)));
            assertNotEquals("Random alphabetic string should not contain duplicate characters.", randomAlphabeticString.charAt(i), randomAlphabeticString.charAt(i + 1));
        }
    }

    /**
     * Test randomAlphanumeric method with default character set.
     */
    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(10);
        assertEquals(10, randomAlphanumericString.length());
        for (int i = 0; i < randomAlphanumericString.length(); i++) {
            assertTrue("Random alphanumeric string should only contain alphanumeric characters.", Character.isAlphabetic(randomAlphanumericString.charAt(i)) || Character.isDigit(randomAlphanumericString.charAt(i)));
            assertNotEquals("Random alphanumeric string should not contain duplicate characters.", randomAlphanumericString.charAt(i), randomAlphanumericString.charAt(i + 1));
        }
    }

    /**
     * Test randomNumeric method with default character set.
     */
    @Test
    public void testRandomNumeric() {
        String randomNumericString = RandomStringUtils.randomNumeric(10);
        assertEquals(10, randomNumericString.length());
        for (int i = 0; i < randomNumericString.length(); i++) {
            assertTrue("Random numeric string should only contain numeric characters.", Character.isDigit(randomNumericString.charAt(i)));
            assertNotEquals("Random numeric string should not contain duplicate characters.", randomNumericString.charAt(i), randomNumericString.charAt(i + 1));
        }
    }

    /**
     * Test random method with custom character set.
     */
    @Test
    public void testRandomCustom() {
        String customSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String randomString = RandomStringUtils.random(5, customSet.toCharArray());
        assertEquals(5, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assertTrue("Random string should only contain characters from the custom set.", customSet.indexOf(randomString.charAt(i)) != -1);
            assertNotEquals("Random string should not contain duplicate characters.", randomString.charAt(i), randomString.charAt(i + 1));
        }
    }
}