package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Metaphone class
 */
public class MetaphoneTest {

    @Test
    public void testMetaphoneInstance() {
        Metaphone metaphone = new Metaphone();
        assertInstanceOf(Metaphone.class, metaphone);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testBasicMetaphone() {
        Metaphone metaphone = new Metaphone();
        assertEquals("BA", metaphone.metaphone("Banana"));
        assertEquals("KN", metaphone.metaphone("Knife"));
        assertEquals("KN", metaphone.metaphone("Cknife"));
        assertEquals("S0", metaphone.metaphone("Thetha"));
    }

    @Test
    public void testMetaphoneWithInitialExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KN", metaphone.metaphone("Knife"));
        assertEquals("KN", metaphone.metaphone("Cknife"));
        assertEquals("KN", metaphone.metaphone("Cunknife"));
        assertEquals("KN", metaphone.metaphone("CKnife"));
        assertEquals("KN", metaphone.metaphone("KCnife"));
        assertEquals("KN", metaphone.metaphone("KnCife"));
        assertEquals("KN", metaphone.metaphone("KnifeX"));
        assertEquals("AE", metaphone.metaphone("Apple"));
        assertEquals("AE", metaphone.metaphone("Apele"));
        assertEquals("AE", metaphone.metaphone("Apeal"));
    }

    @Test
    public void testMetaphoneWithMiddleExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SH", metaphone.metaphone("Bath"));
        assertEquals("SH", metaphone.metaphone("Baths"));
        assertEquals("J", metaphone.metaphone("Bathing"));
        assertEquals("J", metaphone.metaphone("Bathings"));
        assertEquals("J", metaphone.metaphone("Bathingship"));
        assertEquals("J", metaphone.metaphone("Bathrobes"));
    }

    @Test
    public void testMetaphoneWithInitialAndMiddleExceptions() {
        Metaphone metaphone = new Metaphone();
        assertEquals("J", metaphone.metaphone("BathingApple"));
        assertEquals("J", metaphone.metaphone("BathingApples"));
        assertEquals("J", metaphone.metaphone("BathingAppleship"));
        assertEquals("J", metaphone.metaphone("BathingAppleships"));
        assertEquals("J", metaphone.metaphone("BathingAppleBath"));
        assertEquals("J", metaphone.metaphone("BathingAppleBaths"));
    }

    @Test
    public void testMetaphoneWithEdgeCases() {
        Metaphone metaphone = new Metaphone();
        assertEquals("S", metaphone.metaphone("School"));
        assertEquals("SX", metaphone.metaphone("Schools"));
        assertEquals("TCH", metaphone.metaphone("Theatre"));
        assertEquals("TH", metaphone.metaphone("Theatres"));
        assertEquals("S", metaphone.metaphone("Schoold"));
        assertEquals("S", metaphone.metaphone("Schooling"));
        assertEquals("S", metaphone.metaphone("Schoolingg"));
        assertEquals("X", metaphone.metaphone("Schoolinggg"));
        assertEquals("S", metaphone.metaphone("Schoolsgh"));
        assertEquals("SX", metaphone.metaphone("Schoolsghs"));
        assertEquals("S", metaphone.metaphone("Schoolsghg"));
        assertEquals("SX", metaphone.metaphone("Schoolsghgs"));
    }

    @Test
    public void testMetaphoneWithExceptions() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.metaphone(null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            metaphone.metaphone("");
            fail("Expected EncoderException for empty string input");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("Banana", "Banana"));
        assertTrue(metaphone.isMetaphoneEqual("Knife", "Cknife"));
        assertTrue(metaphone.isMetaphoneEqual("Thetha", "Thetha"));
        assertFalse(metaphone.isMetaphoneEqual("Banana", "Apple"));
        assertFalse(metaphone.isMetaphoneEqual("Knife", "Sword"));
        assertFalse(metaphone.isMetaphoneEqual("Thetha", "Echo"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}