package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class RandomStringUtilsTest {

    @Test
    public void testRandom() {
        String randomStr = RandomStringUtils.random(5);
        Assert.assertThat(randomStr.length(), is(5));
    }

    @Test
    public void testRandomAscii() {
        String randomAsciiStr = RandomStringUtils.randomAscii(5);
        Assert.assertThat(randomAsciiStr.length(), is(5));
        Assert.assertThat(randomAsciiStr.matches("[ -~]+"), is(true));
    }

    @Test
    public void testRandomAlphabetic() {
        String randomAlphabeticStr = RandomStringUtils.randomAlphabetic(5);
        Assert.assertThat(randomAlphabeticStr.length(), is(5));
        Assert.assertThat(randomAlphabeticStr.matches("[A-Za-z]+"), is(true));
    }

    @Test
    public void testRandomAlphanumeric() {
        String randomAlphanumericStr = RandomStringUtils.randomAlphanumeric(5);
        Assert.assertThat(randomAlphanumericStr.length(), is(5));
        Assert.assertThat(randomAlphanumericStr.matches("[A-Za-z0-9]+"), is(true));
    }

    @Test
    public void testRandomNumeric() {
        String randomNumericStr = RandomStringUtils.randomNumeric(5);
        Assert.assertThat(randomNumericStr.length(), is(5));
        Assert.assertThat(randomNumericStr.matches("[0-9]+"), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithInvalidCount() {
        RandomStringUtils.random(-1);
    }

    @Test
    public void testRandomWithEmptyChars() {
        String randomStr = RandomStringUtils.random(5, new char[] {});
        Assert.assertThat(randomStr.length(), is(0));
    }

    @Test
    public void testRandomWithCustomChars() {
        String randomStr = RandomStringUtils.random(5, new char[] {'a', 'b', 'c'});
        Assert.assertThat(randomStr.length(), is(5));
        Assert.assertThat(randomStr.matches("[abc]+"), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomWithEmptyStringChars() {
        RandomStringUtils.random(5, "");
    }

}

