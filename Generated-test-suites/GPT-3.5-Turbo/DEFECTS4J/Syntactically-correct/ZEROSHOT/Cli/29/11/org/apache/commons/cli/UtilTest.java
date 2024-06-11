package org.apache.commons.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @BeforeEach
    void setUp() {
        // Setup before each test
    }

    @Test
    void stripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";

        assertEquals("test", Util.stripLeadingHyphens(str1));
        assertEquals("test", Util.stripLeadingHyphens(str2));
        assertEquals("test", Util.stripLeadingHyphens(str3));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    void stripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "test";
        String str3 = "\"test";

        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str2));
        assertEquals("\"test", Util.stripLeadingAndTrailingQuotes(str3));
    }
}