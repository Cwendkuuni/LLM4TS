package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("");
        assertEquals("", actual);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("A");
        assertEquals("A", actual);
    }

    @Test
    public void testMetaphoneSimpleWords() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("apple");
        String expected = "APEL";
        assertEquals(expected, actual);

        actual = metaphone.metaphone("banana");
        expected = "BANA";
        assertEquals(expected, actual);

        actual = metaphone.metaphone("cat");
        expected = "KAT";
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("knife");
        String expected = "NIF";
        assertEquals(expected, actual);

        actual = metaphone.metaphone("aeon");
        expected = "AEON";
        assertEquals(expected, actual);

        actual = metaphone.metaphone("whisky");
        expected = "WISK";
        assertEquals(expected, actual);

        actual = metaphone.metaphone("xylophone");
        expected = "FONX";
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNonASCIIString() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("Mëtäphõne");
    }

    @Test
    public void testMetaphoneIdenticalStrings() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("apple");
        String expected = metaphone.metaphone("apple");
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        String actual = metaphone.metaphone("testtesttest");
        assertEquals("T2ST", actual.substring(0, 4));
    }
}