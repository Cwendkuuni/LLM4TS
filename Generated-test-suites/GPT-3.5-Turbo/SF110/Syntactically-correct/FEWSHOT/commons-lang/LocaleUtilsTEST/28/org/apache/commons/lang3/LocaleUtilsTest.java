

package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_ContainsHash() {
        try {
            LocaleUtils.toLocale("en#US");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en#US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_TooShort() {
        try {
            LocaleUtils.toLocale("e");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_FirstCharNotUnderscore() {
        try {
            LocaleUtils.toLocale("en_US");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_TwoOccurrences() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }


    @Test
    public void testToLocale_ThreeOccurrences() {
        Locale result = LocaleUtils.toLocale("en__US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_NotAllLowercase() {
        try {
            LocaleUtils.toLocale("EN_us");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: EN_us", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        try {
            LocaleUtils.toLocale("eng_US");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: eng_US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        try {
            LocaleUtils.toLocale("en_USA");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_USA", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_SecondOccurrenceEmpty() {
        try {
            LocaleUtils.toLocale("en__");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en__", e.getMessage());
        }
    }

    @Test
    public void testToLocale_SecondOccurrenceNotAllUppercase() {
        try {
            LocaleUtils.toLocale("en_uS");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_uS", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        Locale result = LocaleUtils.toLocale("_US");
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }

    // Add more test cases for localeLookupList() method testing different scenarios

}

