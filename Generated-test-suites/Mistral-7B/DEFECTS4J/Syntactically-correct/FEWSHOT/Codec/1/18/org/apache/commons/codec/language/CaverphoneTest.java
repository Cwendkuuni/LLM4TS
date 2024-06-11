package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEmptyString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    @Test
    public void testCaverphoneSingleLetter() {
        Caverphone caverphone = new Caverphone();
        assertEquals("k", caverphone.caverphone("a"));
        assertEquals("k", caverphone.caverphone("A"));
        assertEquals("k", caverphone.caverphone("e"));
        // edge case for lowercase z
        assertEquals("s", caverphone.caverphone("z"));
    }

    @Test
    public void testCaverphoneMultipleLetters() {
        Caverphone caverphone = new Caverphone();
        assertEquals("tou2fkfkfkfks2", caverphone.caverphone("thequickbrownfoxjumpsoverthelazydog"));
    }

    @Test
    public void testCaverphoneSpecialChars() {
        Caverphone caverphone = new Caverphone();
        assertEquals("tiofkfkfkfks2", caverphone.caverphone("the quick brown fox jumps over the lazy dog"));
        assertEquals("tiofkfkfkfks2", caverphone.caverphone("the quick brown fox jumps over the lazy dog!"));
        assertEquals("tiofkfkfkfks2", caverphone.caverphone("the quick brown fox jumps over the lazy dog!?"));
    }

    @Test
    public void testCaverphoneStartOptions() {
        Caverphone caverphone = new Caverphone();
        assertEquals("cou2fkfkfkfks2", caverphone.caverphone("cough thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("rou2fkfkfkfks2", caverphone.caverphone("rough thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("tou2fkfkfkfks2", caverphone.caverphone("tough thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("enou2fkfkfkfks2", caverphone.caverphone("enough thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("trou2fkfkfkfks2", caverphone.caverphone("trough thequickbrownfoxjumpsoverthelazydog")); // should be trough, not enough
        assertEquals("m2nkfkfkfkfks2", caverphone.caverphone("gn thequickbrownfoxjumpsoverthelazydog"));
    }

    @Test
    public void testCaverphoneReplacements() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2qkfkfkfks2", caverphone.caverphone("cq thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("si2kfkfkfks2", caverphone.caverphone("ci thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("se2kfkfkfks2", caverphone.caverphone("ce thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("sykfkfkfkfks2", caverphone.caverphone("cy thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("2chfkfkfkfks2", caverphone.caverphone("tch thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase c
        assertEquals("kkfkfkfkfks2", caverphone.caverphone("c thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("kkfkfkfkfks2", caverphone.caverphone("q thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("kkfkfkfkfks2", caverphone.caverphone("x thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("ffkfkfkfks2", caverphone.caverphone("v thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("2gh3kfkfkfks2", caverphone.caverphone("dg thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("siofkfkfkfks2", caverphone.caverphone("tio thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("siafkfkfkfks2", caverphone.caverphone("tia thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase d
        assertEquals("tkfkfkfkfks2", caverphone.caverphone("d thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("fhkfkfkfks2", caverphone.caverphone("ph thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("pkfkfkfkfks2", caverphone.caverphone("b thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("s2kfkfkfkfks2", caverphone.caverphone("sh thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase z
        assertEquals("skfkfkfkfks2", caverphone.caverphone("z thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase h
        assertEquals("Akfkfkfkfks2", caverphone.caverphone("h thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase r
        assertEquals("Akfkfkfkfks2", caverphone.caverphone("r thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase l
        assertEquals("Akfkfkfkfks2", caverphone.caverphone("l thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase j
        assertEquals("y2kfkfkfkfks2", caverphone.caverphone("j thequickbrownfoxjumpsoverthelazydog"));
        // edge case for lowercase y (twice)
        assertEquals("Y3kfkfkfkfks2", caverphone.caverphone("y3 thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("Akfkfkfkfks2", caverphone.caverphone("y thequickbrownfoxjumpsoverthelazydog"));
        assertEquals("3kh3kfkfkfks2", caverphone.caverphone("3gh3 thequickbrownfoxjumpsoverthelazydog"));
    }

    @Test
    public void testCaverphoneHandlesRemovals() {
        Caverphone caverphone = new Caverphone();
        assertEquals("kkfkfkfkf", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$"));
        assertEquals("", caverphone.caverphone("3"));
    }

    @Test
    public void testCaverphoneEdgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("A", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("1111111111"));
        assertEquals("111111", caverphone.caverphone("11111111111111"));
        assertEquals("1111", caverphone.caverphone("1111111111111111"));
    }

    @Test(expected = EncoderException.class)
    public void testCaverphoneNonStringInput() {
        Caverphone caverphone = new Caverphone();
        caverphone.encode((Object) 42);
    }

    @Test
    public void testCaverphoneIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertEquals(true, caverphone.isCaverphoneEqual("thequickbrownfoxjumpsoverthelazydog", "thequickbrownfoxjumpsoverthelazydog"));
        assertEquals(false, caverphone.isCaverphoneEqual("thequickbrownfoxjumpsoverthelazydog", "thequickbrownfoxjumpsoverthelazycat"));
    }
}