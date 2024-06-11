package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Test empty string
        assertEquals("", metaphone.metaphone(""));

        // Test single character
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("B", metaphone.metaphone("B"));

        // Test initial exceptions
        assertEquals("KNAPP", metaphone.metaphone("Knapp"));
        assertEquals("AEGIS", metaphone.metaphone("Aegis"));
        assertEquals("WHALE", metaphone.metaphone("Whale"));
        assertEquals("SIX", metaphone.metaphone("Xix"));

        // Test simple words
        assertEquals("A000", metaphone.metaphone("Aardvark"));
        assertEquals("A000", metaphone.metaphone("Aardwark"));
        assertEquals("A000", metaphone.metaphone("Aardwolf"));
        assertEquals("B251", metaphone.metaphone("Barack"));
        assertEquals("C261", metaphone.metaphone("Caramel"));
        assertEquals("D223", metaphone.metaphone("Dolphin"));
        assertEquals("E000", metaphone.metaphone("Elephant"));
        assertEquals("G263", metaphone.metaphone("Gorilla"));
        assertEquals("I010", metaphone.metaphone("Iguana"));
        assertEquals("K321", metaphone.metaphone("Kangaroo"));
        assertEquals("P261", metaphone.metaphone("Potato"));
        assertEquals("S321", metaphon.metaphone("Snake"));
        assertEquals("T231", metaphone.metaphone("Tomato"));

        // Test complex words
        assertEquals("C462", metaphone.metaphone("Caramelized"));
        assertEquals("K263", metaphone.metaphone("Kangaroos"));
        assertEquals("K261", metaphone.metaphone("Kangaroo's"));
        assertEquals("P251", metaphone.metaphone("Potatoes"));
        assertEquals("S262", metaphone.metaphone("Snakeskin"));
        assertEquals("T261", metaphone.metaphone("Tomatoes"));

        // Test edge cases
        assertEquals("A000", metaphone.metaphone(""));
        assertEquals("A000", metaphone.metaphone(" "));
        assertEquals("A000", metaphone.metaphone("A"));
        assertEquals("A000", metaphone.metaphone("A "));
        assertEquals("A000", metaphone.metaphone("A "));
        assertEquals("A000", metaphone.metaphone("Aa"));
        assertEquals("A000", metaphone.metaphone("Aaa"));
        assertEquals("A000", metaphone.metaphone("Aaaa"));
        assertEquals("A000", metaphone.metaphone("Aaaaa"));
        assertEquals("A000", metaphone.metaphone("Aaaaaaaaa"));
        assertEquals("A010", metaphone.metaphone("Aardvark"));
        assertEquals("A010", metaphone.metaphone("Aardwolf"));
        assertEquals("A010", metaphone.metaphone("Aardwolfs"));
        assertEquals("B251", metaphone.metaphone("Barack"));
        assertEquals("B251", metaphone.metaphone("Barack Obama"));
        assertEquals("B251", metaphone.metaphone("Barack Hussein Obama"));
        assertEquals("C261", metaphone.metaphone("Caramel"));
        assertEquals("C261", metaphone.metaphone("Caramelized"));
        assertEquals("C261", metaphone.metaphone("Caramelized Custard"));
        assertEquals("D223", metaphone.metaphone("Dolphin"));
        assertEquals("D223", metaphone.metaphone("Dolphins"));
        assertEquals("E000", metaphone.metaphone("Elephant"));
        assertEquals("E000", metaphone.metaphone("Elephants"));
        assertEquals("G263", metaphone.metaphone("Gorilla"));
        assertEquals("G263", metaphone.metaphone("Gorillas"));
        assertEquals("I010", metaphone.metaphone("Iguana"));
        assertEquals("I010", metaphone.metaphone("Iguanas"));
        assertEquals("K321", metaphone.metaphone("Kangaroo"));
        assertEquals("K321", metaphone.metaphone("Kangaroos"));
        assertEquals("K321", metaphone.metaphone("Kangaroos and Wallabies"));
        assertEquals("P261", metaphone.metaphone("Potato"));
        assertEquals("P261", metaphone.metaphone("Potatoes"));
        assertEquals("P261", metaphone.metaphone("Potatoes and Carrots"));
        assertEquals("S321", metaphone.metaphone("Snake"));
        assertEquals("S321", metaphone.metaphone("Snakes"));
        assertEquals("S321", metaphone.metaphone("Snakes and Ladders"));
        assertEquals("T231", metaphone.metaphone("Tomato"));
        assertEquals("T231", metaphone.metaphone("Tomatoes"));
        assertEquals("T231", metaphone.metaphone("Tomatoes and Cucumbers"));

        // Test incorrect input
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("", metaphone.metaphone("1234"));
        assertEquals("", metaphone.metaphone("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("", metaphone.metaphone("AbCdEfGhIjKlMnOpQrStUvWxYz"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();

        // Test equality
        assertEquals(true, metaphone.isMetaphoneEqual("Aardvark", "Aardvark"));
        assertEquals(true, metaphone.isMetaphoneEqual("Aardvark", "Aardwark"));
        assertEquals(true, metaphone.isMetaphoneEqual("Aardvark", "Aardwolf"));
        assertEquals(true, metaphone.isMetaphoneEqual("Barack", "Barack Obama"));
        assertEquals(true, metaphone.isMetaphoneEqual("Caramelized", "Caramelized Custard"));
        assertEquals(true, metaphone.isMetaphoneEqual("Dolphins", "Dolphin"));
        assertEquals(true, metaphone.isMetaphoneEqual("Elephants", "Elephant"));
        assertEquals(true, metaphone.isMetaphoneEqual("Gorillas", "Gorilla"));
        assertEquals(true, metaphone.isMetaphoneEqual("Iguanas", "Iguana"));
        assertEquals(true, metaphone.isMetaphoneEqual("Kangaroos", "Kangaroo"));
        assertEquals(true, metaphone.isMetaphoneEqual("Potatoes", "Potato"));
        assertEquals(true, metaphone.isMetaphoneEqual("Snakes", "Snake"));
        assertEquals(true, metaphone.isMetaphoneEqual("Tomatoes", "Tomato"));

        // Test inequality
        assertEquals(false, metaphone.isMetaphoneEqual("Aardvark", "Aardwolf"));
        assertEquals(false, metaphone.isMetaphoneEqual("Barack", "Barack Obama"));
        assertEquals(false, metaphone.isMetaphoneEqual("Caramel", "Caramelized"));
        assertEquals(false, metaphone.isMetaphoneEqual("Dolphin", "Dolphins"));
        assertEquals(false, metaphone.isMetaphoneEqual("Elephant", "Elephants"));
        assertEquals(false, metaphone.isMetaphoneEqual("Gorilla", "Gorillas"));
        assertEquals(false, metaphone.isMetaphoneEqual("Iguana", "Iguanas"));
        assertEquals(false, metaphone.isMetaphoneEqual("Kangaroo", "Kangaroos"));
        assertEquals(false, metaphone.isMetaphoneEqual("Potato", "Potatoes"));
        assertEquals(false, metaphone.isMetaphoneEqual("Snake", "Snakes"));
        assertEquals(false, metaphone.isMetaphoneEqual("Tomato", "Tomatoes"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();

        assertEquals("A000", metaphone.encode("Aardvark"));
        assertEquals("A000", metaphone.encode("Aardwark"));
        assertEquals("A000", metaphone.encode("Aardwolf"));
        assertEquals("B251", metaphone.encode("Barack"));
        assertEquals("C261", metaphone.encode("Caramel"));
        assertEquals("D223", metaphone.encode("Dolphin"));
        assertEquals("E000", metaphone.encode("Elephant"));
        assertEquals("G263", metaphone.encode("Gorilla"));
        assertEquals("I010", metaphone.encode("Iguana"));
        assertEquals("K321", metaphone.encode("Kangaroo"));
        assertEquals("P261", metaphone.encode("Potato"));
        assertEquals("S321", metaphone.encode("Snake"));
        assertEquals("T231", metaphone.encode("Tomato"));

        assertEquals("C462", metaphone.encode("Caramelized"));
        assertEquals("K263", metaphone.encode("Kangaroos"));
        assertEquals("K261", metaphone.encode("Kangaroo's"));
        assertEquals("P251", metaphone.encode("Potatoes"));
        assertEquals("S262", metaphone.encode("Snakeskin"));
        assertEquals("T261", metaphone.encode("Tomatoes"));

        assertEquals("", metaphone.encode(null));
    }
}