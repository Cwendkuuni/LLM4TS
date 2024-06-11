package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

 Caverphone caverphoneObj = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 try {
 caverphoneObj.caverphone(null);
 } catch (EncoderException e) {
 assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
 }
 }

 @Test
 public void testCaverphoneEmptyString() {
 assertEquals("1111111111", caverphoneObj.caverphone(""));
 }

 @Test
 public void testCaverphoneLowercase() {
 assertEquals("kfkhtkftkfkf", caverphoneObj.caverphone("The quick brown fox jumps over the lazy dog"));
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 assertEquals("kfkhtkftkfkf", caverphoneObj.caverphone("The quick brown fox jumps over the lazy dog"));
 assertEquals("cou2fkftkfkf", caverphoneObj.caverphone("The quick brown fox coughs over the lazy dog"));
 assertEquals("rou2fkftkfkf", caverphoneObj.caverphone("The quick brown fox roughs over the lazy dog"));
 assertEquals("tou2fkftkfkf", caverphoneObj.caverphone("The quick brown fox toughs over the lazy dog"));
 assertEquals("enou2fkftkfkf", caverphoneObj.caverphone("The quick brown fox enoughs over the lazy dog"));
 assertEquals("trou2fkftkfkf", caverphoneObj.caverphone("The quick brown fox troughs over the lazy dog")); // Note: Spec error in test case
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 assertEquals("kfkhtkftkfkf", caverphoneObj.caverphone("The quick brown fox jumps over the lazy dog"));
 assertEquals("2qtkftkfkf", caverphoneObj.caverphone("The quick brown fox qicks over the lazy dog"));
 assertEquals("siikftkfkf", caverphoneObj.caverphone("The quick brown fox cis over the lazy dog"));
 assertEquals("seikftkfkf", caverphoneObj.caverphone("The quick brown fox ces over the lazy dog"));
 assertEquals("syikftkfkf", caverphoneObj.caverphone("The quick brown fox cys over the lazy dog"));
 assertEquals("2chtkftkfkf", caverphoneObj.caverphone("The quick brown fox tchicks over the lazy dog"));
 assertEquals("kkkfkf", caverphoneObj.caverphone("The quick brown fox kicks over the lazy dog"));
 assertEquals("kkkfkf", caverphoneObj.caverphone("The quick brown fox kicks over the lazy dog"));
 assertEquals("kkkfkf", caverphoneObj.caverphone("The quick brown fox kicks over the lazy dog"));
 assertEquals("ffkfkf", caverphoneObj.caverphone("The quick brown fox voxes over the lazy dog"));
 assertEquals("2g2g2g", caverphoneObj.caverphone("The quick brown fox dgogs over the lazy dog"));
 assertEquals("sioikfkf", caverphoneObj.caverphone("The quick brown fox tio over the lazy dog"));
 assertEquals("siaikfkf", caverphoneObj.caverphone("The quick brown fox tia over the lazy dog"));
 assertEquals("ttttttt", caverphoneObj.caverphone("The quick brown fox d over the lazy dog"));
 assertEquals("fhphkfkf", caverphoneObj.caverphone("The quick brown fox phoxes over the lazy dog"));
 assertEquals("ppp", caverphoneObj.caverphone("The quick brown fox b over the lazy dog"));
 assertEquals("s2s2s2", caverphoneObj.caverphone("The quick brown fox sh over the lazy dog"));
 assertEquals("ssssss", caverphoneObj.caverphone("The quick brown fox z over the lazy dog"));
 assertEquals("AA", caverphoneObj.caverphone("The quick brown fox ")); // Handles empty end
 assertEquals("3", caverphoneObj.caverphone("The quick brown fox y")); // 2.0 only
 assertEquals("A", caverphoneObj.caverphone("The quick brown fox y3")); // 2.0 only
 assertEquals("A", caverphoneObj.caverphone("The quick brown fox y")); // 2.0 only
 assertEquals("3", caverphoneObj.caverphone("The quick brown fox y")); // 2.0 only
 assertEquals("3kh3", caverphoneObj.caverphone("The quick brown fox 3gh3"));
 assertEquals("22", caverphoneObj.caverphone("The quick brown fox gh"));
 assertEquals("k", caverphoneObj.caverphone("The quick brown fox g"));
 assertEquals("S", caverphoneObj.caverphone("The quick brown fox s+"));
 assertEquals("T", caverphoneObj.caverphone("The quick brown fox t+"));
 assertEquals("P", caverphoneObj.caverphone("The quick brown fox p+"));
 assertEquals("K", caverphoneObj.caverphone("The quick brown fox k+"));
 assertEquals("F", caverphoneObj.caverphone("The quick brown fox f+"));
 assertEquals("M", caverphoneObj.caverphone("The quick brown fox m+"));
 assertEquals("N", caverphoneObj.caverphone("The quick brown fox n+"));
 assertEquals("W3", caverphoneObj.caverphone("The quick brown fox w3"));
 // Workaround for unreachable code warning
 // assertEquals("Wy", caverphoneObj.caverphone("The quick brown fox wy")); // 1.0 only
 assertEquals("Wh3", caverphoneObj.caverphone("The quick brown fox wh3"));
 assertEquals("3", caverphoneObj.caverphone("The quick brown fox w")); // 2.0 only
 // Workaround for unreachable code warning
 // assertEquals("Why", caverphoneObj.caverphone("The quick brown fox why")); // 1.0 only
 assertEquals("2", caverphoneObj.caverphone("The quick brown fox w"));
 assertEquals("A", caverphoneObj.caverphone("The quick brown fox h"));
 assertEquals("2", caverphoneObj.caverphone("The quick brown fox h"));
 assertEquals("R3", caverphoneObj.caverphone("The quick brown fox r3"));
 assertEquals("3", caverphoneObj.caverphone("The quick brown fox r")); // 2.0 only
 // Workaround for unreachable code warning
 // assertEquals("Ry", caverphoneObj.caverphone("The quick brown fox ry")); // 1.0 only
 assertEquals("2", caverphoneObj.caverphone("The quick brown fox r"));
 assertEquals("L3", caverphoneObj.caverphone("The quick brown fox l3"));
 assertEquals("3", caverphoneObj.caverphone("The quick brown fox l")); // 2.0 only
 // Workaround for unreachable code warning
 // assertEquals("Ly", caverphoneObj.caverphone("The quick brown fox ly")); // 1.0 only
 assertEquals("2", caverphoneObj.caverphone("The quick brown fox l"));
 // Workaround for unreachable code warning
 // assertEquals("y", caverphoneObj.caverphone("The quick brown fox j")); // 1.0 only
 // Workaround for unreachable code warning
 // assertEquals("Y3", caverphoneObj.caverphone("The quick brown fox y3")); // 1.0 only
 // Workaround for unreachable code warning
 // assertEquals("Y", caverphoneObj.caverphone("The quick brown fox y")); // 1.0 only
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 assertEquals("kfkf", caverphoneObj.caverphone("The quick brown fox jumps over the lazy dog"));
 assertEquals("A", caverphoneObj.caverphone("1111111111")); // 1.0 truncates to 6
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneThrowsEncoderExceptionForNonString() {
 caverphoneObj.encode(new Object() {});
 }

 @Test
 public void testCaverphoneEqualStrings() {
 assertTrue(caverphoneObj.isCaverphoneEqual("The quick brown fox jumps over the lazy dog", "The quick brown fox jumps over the lazy dog"));
 }

 @Test
 public void testCaverphoneUnequalStrings() {
 assertFalse(caverphoneObj.isCaverphoneEqual("The quick brown fox jumps over the lazy dog", "The quick brown fox jumps over the cat"));
 }
}