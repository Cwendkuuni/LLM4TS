package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone encoder = new Metaphone();

        assertEquals("BAT", encoder.metaphone("bat"));
        assertEquals("BATH", encoder.metaphone("bath"));
        assertEquals("CAR", encoder.metaphone("car"));
        assertEquals("CAT", encoder.metaphone("cat"));
        assertEquals("CHAINS", encoder.metaphone("chains"));
        assertEquals("CODE", encoder.metaphone("code"));
        assertEquals("DOG", encoder.metaphone("dog"));
        assertEquals("FOOT", encoder.metaphone("foot"));
        assertEquals("GHOST", encoder.metaphone("ghost"));
        assertEquals("HAVE", encoder.metaphone("have"));
        assertEquals("HELL", encoder.metaphone("hell"));
        assertEquals("HORSE", encoder.metaphone("horse"));
        assertEquals("ICE", encoder.metaphone("ice"));
        assertEquals("JUMP", encoder.metaphone("jump"));
        assertEquals("KAYAK", encoder.metaphone("kayak"));
        assertEquals("LEMON", encoder.metaphone("lemon"));
        assertEquals("MOTOR", encoder.metaphone("motor"));
        assertEquals("NATION", encoder.metaphone("nation"));
        assertEquals("NOTICE", encoder.metaphone("notice"));
        assertEquals("OCEAN", encoder.metaphone("ocean"));
        assertEquals("PIRATE", encoder.metaphone("pirate"));
        assertEquals("QUICK", encoder.metaphone("quick"));
        assertEquals("RABBIT", encoder.metaphone("rabbit"));
        assertEquals("SILENCE", encoder.metaphone("silence"));
        assertEquals("TOAST", encoder.metaphone("toast"));
        assertEquals("TOMATO", encoder.metaphone("tomato"));
        assertEquals("TOWN", encoder.metaphone("town"));
        assertEquals("TURKEY", encoder.metaphone("turkey"));
        assertEquals("UNIVERSITY", encoder.metaphone("university"));
        assertEquals("VANISH", encoder.metaphone("vanish"));
        assertEquals("WATER", encoder.metaphone("water"));
        assertEquals("WHISKEY", encoder.metaphone("whiskey"));
        assertEquals("YES", encoder.metaphone("yes"));
        assertEquals("ZOO", encoder.metaphone("zoo"));

        // Test empty string
        assertNull(encoder.metaphone(""));

        // Test null input
        assertNull(encoder.metaphone(null));

        // Test long string
        String longString = StringUtils.repeat("apple", 1000);
        assertEquals(encoder.metaphone(longString), encoder.metaphone("apple"));

        // Test string with numbers
        assertEquals("APPLE123", encoder.metaphone("apple123"));

        // Test string with special characters
        assertEquals("APPLE!", encoder.metaphone("apple!"));
        assertEquals("APPLE$", encoder.metaphone("apple$"));

        // Test string with multiple words
        assertEquals("APPLE APPLE", encoder.metaphone("apple apple"));
    }

    @Test(expected = EncoderException.class)
    public void testInvalidInput() {
        Metaphone encoder = new Metaphone();
        encoder.metaphone(null);
    }
}