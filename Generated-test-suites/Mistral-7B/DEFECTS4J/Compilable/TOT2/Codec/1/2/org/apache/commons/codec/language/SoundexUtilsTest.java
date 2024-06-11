package org.apache.commons.codec.language;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        String expectedOutput = "ABCDEFGHIJKLMNOPQRSTUWXYZ";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Add edge case tests for empty string, null input, and special characters
}