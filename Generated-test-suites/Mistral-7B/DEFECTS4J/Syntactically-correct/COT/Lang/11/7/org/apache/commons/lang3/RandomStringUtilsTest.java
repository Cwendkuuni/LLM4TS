package org.apache.commons.lang3.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for RandomStringUtils class.
 */
public class RandomStringUtilsTest {

    /**
     * Test for random method with default options.
     */
    @Test
    public void testRandomDefault() {
        String randomString = RandomStringUtils.random(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
    }

    /**
     * Test for randomAscii method with default options.
     */
    @Test
    public void testRandomAsciiDefault() {
        String randomString = RandomStringUtils.randomAscii(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(randomString.charAt(i) >= 32 && randomString.charAt(i) <= 126);
        }
    }

    /**
     * Test for randomAlphabetic method with default options.
     */
    @Test
    public void testRandomAlphabeticDefault() {
        String randomString = RandomStringUtils.randomAlphabetic(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isAlphabetic(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomAlphanumeric method with default options.
     */
    @Test
    public void testRandomAlphanumericDefault() {
        String randomString = RandomStringUtils.randomAlphanumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for randomNumeric method with default options.
     */
    @Test
    public void testRandomNumericDefault() {
        String randomString = RandomStringUtils.randomNumeric(10);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for random method with specified options.
     */
    @Test
    public void testRandomSpecified() {
        String randomString = RandomStringUtils.random(10, true, true);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isAlphabetic(randomString.charAt(i)) || Character.isDigit(randomString.charAt(i)));
        }
    }

    /**
     * Test for random method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNegativeCount() {
        RandomStringUtils.random(-1);
    }

    /**
     * Test for randomAlphabetic method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomAlphabeticEmptySet() {
        RandomStringUtils.randomAlphabetic(0);
    }

    /**
     * Test for randomNumeric method with empty character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNumericEmptySet() {
        RandomStringUtils.randomNumeric(0);
    }

    /**
     * Test for random method with null character set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomNullSet() {
        RandomStringUtils.random(0, null);
    }

    /**
     * Test for random method with specified start and end options.
     */
    @Test
    public void testRandomStartEnd() {
        String randomString = RandomStringUtils.random(10, 'a', 'z');
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(randomString.charAt(i) >= 'a' && randomString.charAt(i) <= 'z');
        }
    }

    /**
     * Test for random method with specified start and end options out of range.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRandomStartEndOutOfRange() {
        String randomString = RandomStringUtils.random(10, 'z', 'a');
    }

    /**
     * Test for random method with specified character set containing private high surrogates.
     */
    @Test
    public void testRandomPrivateHighSurrogates() {
        String randomString = RandomStringUtils.random(10, 'x0340'.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(randomString.charAt(i) >= 55296 || randomString.charAt(i) <= 57343);
        }
    }

    /**
     * Test for random method with specified character set containing high and low surrogates.
     */
    @Test
    public void testRandomHighAndLowSurrogates() {
        String randomString = RandomStringUtils.random(10, 'x0340'.toCharArray(), 'x0359'.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            char ch = randomString.charAt(i);
            if (Character.isHighSurrogate(ch)) {
                assert(i < randomString.length() - 1);
                char next = randomString.charAt(i + 1);
                assert(Character.isLowSurrogate(next));
                i++;
            } else if (Character.isLowSurrogate(ch)) {
                assert (i > 0);
                char previous = randomString.charAt(i - 1);
                assert(Character.isHighSurrogate(previous));
            } else {
                assert(Character.isDefined(ch));
            }
        }
    }

    /**
     * Test for random method with specified character set containing high surrogates only.
     */
    @Test
    public void testRandomHighSurrogatesOnly() {
        String randomString = RandomStringUtils.random(10, 'x0340'.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isHighSurrogate(randomString.charAt(i)));
        }
    }

    /**
     * Test for random method with specified character set containing low surrogates only.
     */
    @Test
    public void testRandomLowSurrogatesOnly() {
        String randomString = RandomStringUtils.random(10, 'x0360'.toCharArray(), RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isLowSurrogate(randomString.charAt(i)));
        }
    }

    /**
     * Test for random method with specified character set containing illegal surrogates.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRandomIllegalSurrogates() {
        String randomString = RandomStringUtils.random(10, 'x0300'.toCharArray(), RANDOM);
    }

    /**
     * Test for random method with specified character set containing non-printable characters.
     */
    @Test
    public void testRandomNonPrintable() {
        String randomString = RandomStringUtils.random(10, new char[]{'\u0001', '\u007F'}, RANDOM);
        assertNotNull(randomString);
        assertEquals(10, randomString.length());
        for (int i = 0; i < randomString.length(); i++) {
            assert(Character.isDefined(randomString.charAt(i)) && Character.isDefined(Character.forDigit(randomString.charAt(i), 0x10)));
        }
    }
}