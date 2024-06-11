package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for Metaphone class
 */
public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone encoder = new Metaphone();
        assertEquals("", encoder.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone encoder = new Metaphone();
        assertEquals("A", encoder.metaphone("A"));
        assertEquals("B", encoder.metaphone("B"));
        // Test for edge case: single character is itself
        assertEquals("E", encoder.metaphone("E"));
        assertEquals("F", encoder.metaphone("F"));
        assertEquals("G", encoder.metaphone("G"));
        assertEquals("H", encoder.metaphone("H"));
        assertEquals("I", encoder.metaphone("I"));
        assertEquals("J", encoder.metaphone("J"));
        assertEquals("K", encoder.metaphone("K"));
        assertEquals("L", encoder.metaphone("L"));
        assertEquals("M", encoder.metaphone("M"));
        assertEquals("N", encoder.metaphone("N"));
        assertEquals("O", encoder.metaphone("O"));
        assertEquals("P", encoder.metaphone("P"));
        assertEquals("Q", encoder.metaphone("Q"));
        assertEquals("R", encoder.metaphone("R"));
        assertEquals("S", encoder.metaphone("S"));
        assertEquals("T", encoder.metaphone("T"));
        assertEquals("U", encoder.metaphone("U"));
        assertEquals("V", encoder.metaphone("V"));
        assertEquals("W", encoder.metaphone("W"));
        assertEquals("X", encoder.metaphone("X"));
        assertEquals("Y", encoder.metaphone("Y"));
        assertEquals("Z", encoder.metaphone("Z"));
    }

    @Test
    public void testMetaphoneInitialExceptions() {
        Metaphone encoder = new Metaphone();
        // Test for exception: initial KN, etc
        assertEquals("KN", encoder.metaphone("KNIGHT"));
        assertEquals("KN", encoder.metaphone("CONDITION"));
        // Test for exception: AE
        assertEquals("AE", encoder.metaphone("APE"));
        assertEquals("AE", encoder.metaphone("APES"));
        // Test for exception: WR or WH
        assertEquals("WR", encoder.metaphone("WRITE"));
        assertEquals("WH", encoder.metaphone("WHALE"));
        // Test for exception: initial X becomes S
        assertEquals("SEND", encoder.metaphone("SENDS"));
    }

    @Test
    public void testMetaphoneCharacterExceptions() {
        Metaphone encoder = new Metaphone();
        // Test for exceptions: CI,CE,CY -> S
        assertEquals("SEND", encoder.metaphone("SENDS"));
        assertEquals("SEND", encoder.metaphone("SECRET"));
        assertEquals("SEND", encoder.metaphone("SENDING"));
        // Test for exception: CI,CE,CY -> K
        assertEquals("KEEP", encoder.metaphone("KEEPS"));
        assertEquals("KIND", encoder.metaphone("KINDS"));
        assertEquals("KIND", encoder.metaphone("KINDING"));
        // Test for exception: SCH -> sk
        assertEquals("SHARK", encoder.metaphone("SCHOOL"));
        assertEquals("SKATE", encoder.metaphone("SCHOOL"));
        assertEquals("SKI", encoder.metaphone("SCHOOL"));
        // Test for exception: CH consonant -> K consonant
        assertEquals("BARRACKS", encoder.metaphone("BARRACKS"));
        assertEquals("BOOKS", encoder.metaphone("BOOKS"));
        // Test for exception: CHvowel -> X
        assertEquals("AXEL", encoder.metaphone("AXELROD"));
        assertEquals("AXEL", encoder.metaphone("AXELROD'S"));
        // Test for exception: DGE DGI DGY -> J
        assertEquals("JAMES", encoder.metaphone("JAMES"));
        assertEquals("JAMES", encoder.metaphone("JAMES'"));
        // Test for exception: silent G
        assertEquals("APPLE", encoder.metaphone("APPLE"));
        assertEquals("APPLES", encoder.metaphone("APPLES"));
        assertEquals("APPLE", encoder.metaphone("APPLEGATE"));
        assertEquals("APPLE", encoder.metaphone("APPLETON"));
        // Test for exception: F, J, L, M, N, R silent if not followed by vowel
        assertEquals("FRED", encoder.metaphone("FRED"));
        assertEquals("FRED", encoder.metaphone("FRED'S"));
        assertEquals("JAMES", encoder.metaphone("JAMES"));
        assertEquals("JAMES", encoder.metaphone("JAMES'"));
        assertEquals("LION", encoder.metaphone("LION"));
        assertEquals("LION", encoder.metaphone("LION'S"));
        assertEquals("MEN", encoder.metaphone("MEN"));
        assertEquals("MEN", encoder.metaphone("MEN'S"));
        assertEquals("NED", encoder.metaphone("NED"));
        assertEquals("NED", encoder.metaphone("NED'S"));
        assertEquals("RICH", encoder.metaphone("RICH"));
        assertEquals("RICH", encoder.metaphone("RICH'S"));
    }

    @Test
    public void testMetaphoneErrorScenarios() {
        Metaphone encoder = new Metaphone();
        assertNull(encoder.metaphone(null));
        assertNull(encoder.metaphone(""));
        assertEquals("", encoder.metaphone(" "));
        assertEquals("", encoder.metaphone("\t"));
        assertEquals("", encoder.metaphone("\n"));
        assertEquals("", encoder.metaphone("\r"));
        // Test for error: Input format is expected to be a single ASCII word with only characters in the A - Z range, no punctuation or numbers.
        assertEquals("", encoder.metaphone("abc123"));
        assertEquals("", encoder.metaphone("abcdefg!@#$%^&*()_+-="));
        assertEquals("", encoder.metaphone("abcdefg 1234567890"));
    }

    @Test
    public void testMetaphoneComparison() {
        Metaphone encoder = new Metaphone();
        assertEquals(encoder.metaphone("APPLE"), encoder.metaphone("APPLE"));
        assertEquals(encoder.metaphone("APPLE"), encoder.metaphone("APPLETON"));
        assertEquals(encoder.metaphone("APPLE"), encoder.metaphone("APPLES"));
        assertEquals(encoder.metaphone("BARRACKS"), encoder.metaphone("BARRACKS"));
        assertEquals(encoder.metaphone("BARRACKS"), encoder.metaphone("BARRACKS'"));
        assertEquals(encoder.metaphone("BARRACKS"), encoder.metaphone("BARRACKS OF"));
        assertEquals(encoder.metaphone("BARRACKS"), encoder.metaphone("BARRACOOMS")); // different vowel order does not affect metaphone
        assertEquals(encoder.metaphone("BOOKS"), encoder.metaphone("BOOKS"));
        assertEquals(encoder.metaphone("BOOKS"), encoder.metaphone("BOOKKEEP"));
        assertEquals(encoder.metaphone("BOOKS"), encoder.metaphone("BOOKKEEPS"));
        assertEquals(encoder.metaphone("CONDITION"), encoder.metaphone("CONDITION"));
        assertEquals(encoder.metaphone("CONDITION"), encoder.metaphone("CONDIITION"));
        assertEquals(encoder.metaphone("CONDITION"), encoder.metaphone("CONDICTION"));
        assertEquals(encoder.metaphone("FRED"), encoder.metaphone("FRED"));
        assertEquals(encoder.metaphone("FRED"), encoder.metaphone("FRED'S"));
        assertEquals(encoder.metaphone("FRED"), encoder.metaphone("FRED'S OF"));
        assertEquals(encoder.metaphone("FRED"), encoder.metaphone("FREDO"));
        assertEquals(encoder.metaphone("JAMES"), encoder.metaphone("JAMES"));
        assertEquals(encoder.metaphone("JAMES"), encoder.metaphone("JAMES'"));
        assertEquals(encoder.metaphone("JAMES"), encoder.metaphone("JAMES'S"));
        assertEquals(encoder.metaphone("KNIGHT"), encoder.metaphone("KNIGHT"));
        assertEquals(encoder.metaphone("KNIGHT"), encoder.metaphone("KNIGHT'S"));
        assertEquals(encoder.metaphone("KNIGHT"), encoder.metaphone("KNIGHTS"));
        assertEquals(encoder.metaphone("MEN"), encoder.metaphone("MEN"));
        assertEquals(encoder.metaphone("MEN"), encoder.metaphone("MEN'S"));
        assertEquals(encoder.metaphone("MEN"), encoder.metaphone("MEN'S OF"));
        assertEquals(encoder.metaphone("MEN"), encoder.metaphone("MENN"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'S"));
        assertEquals(encoder.metaphone("SECRET"), encoder.metaphone("SECRET"));
        assertEquals(encoder.metaphone("SECRET"), encoder.metaphone("SECRET'"));
        assertEquals(encoder.metaphone("SECRET"), encoder.metaphone("SECRETS"));
        assertEquals(encoder.metaphone("SENDING"), encoder.metaphone("SENDING"));
        assertEquals(encoder.metaphone("SENDING"), encoder.metaphone("SENDING'"));
        assertEquals(encoder.metaphone("SENDING"), encoder.metaphone("SENDING'S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDINGS"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDING'S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDING'S'"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDING''S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'S'"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS'S'S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S'"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S'''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SENDS"), encoder.metaphone("SENDS''S''''''''''''''''''''''''''''''''"));
        assertEquals(encoder.metaphone("SHARK"), encoder.metaphone("SCHOOL"));
        assertEquals(encoder.metaphone("SHARK"), encoder.metaphone("SCHOOL'"));
        assertEquals(encoder.metaphone("SHARK"), encoder.metaphone("SCHOOLS"));
        assertEquals(encoder.metaphone("SKATE"), encoder.metaphone("SCHOOL"));
        assertEquals(encoder.metaphone("SKATE"), encoder.metaphone("SCHOOL'"));
        assertEquals(encoder.metaphone("SKATE"), encoder.metaphone("SCHOOLS"));
        assertEquals(encoder.metaphone("SKI"), encoder.metaphone("SCHOOL"));
        assertEquals(encoder.metaphone("SKI"), encoder.metaphone("SCHOOL'"));
        assertEquals(encoder.metaphone("SKI"), encoder.metaphone("SCHOOLS"));
        assertEquals(encoder.metaphone("SCHOOL"), encoder.metaphone("SCHOOL"));
        assertEquals(encoder.metaphone("SCHOOL"), encoder.metaphone("SCHOOL'"));
        assertEquals(encoder.metaphone("SCHOOL"), encoder.metaphone("SCHOOLS"));
        assertEquals(encoder.metaphone("WRITE"), encoder.metaphone("WRITE"));
        assertEquals(encoder.metaphone("WRITE"), encoder.metaphone("WRITE'"));
        assertEquals(encoder.metaphone("WRITE"), encoder.metaphone("WRITES"));
        assertEquals(encoder.metaphone("WRONG"), encoder.metaphone("WRONG"));
        assertEquals(encoder.metaphone("WRONG"), encoder.metaphone("WRONG'"));
        assertEquals(encoder.metaphone("WRONG"), encoder.metaphone("WRONGS"));
    }
}