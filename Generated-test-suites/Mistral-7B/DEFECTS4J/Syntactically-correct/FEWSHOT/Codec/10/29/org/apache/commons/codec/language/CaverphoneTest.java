package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncoding() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("2222222222", caverphone.caverphone(""));

        assertEquals("aa", caverphone.caverphone("a"));
        assertEquals("aa3", caverphone.caverphone("aa"));
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("A3", caverphone.caverphone("A"));

        assertEquals("ab", caverphone.caverphone("ab"));
        assertEquals("a2b", caverphone.caverphone("ab"));

        assertEquals("cat", caverphone.caverphone("cat"));
        assertEquals("cou2f", caverphone.caverphone("cat"));
        assertEquals("cou2f", caverphone.caverphone("cough"));

        assertEquals("dog", caverphone.caverphone("dog"));
        assertEquals("dou2f", caverphone.caverphone("dog"));

        assertEquals("enough", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("enough")); // 2.0 only

        assertEquals("foo", caverphone.caverphone("foo"));
        assertEquals("f", caverphone.caverphone("f"));

        assertEquals("ghost", caverphone.caverphone("ghost"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("2kh3", caverphone.caverphone("ghost"));

        assertEquals("iam", caverphone.caverphone("iam"));
        assertEquals("i2m", caverphone.caverphone("iam"));

        assertEquals("john", caverphone.caverphone("john"));
        assertEquals("jy", caverphone.caverphone("j")); // 2.0 only
        assertEquals("J3", caverphone.caverphone("john"));

        assertEquals("kangaroo", caverphone.caverphone("kangaroo"));
        assertEquals("kang2r2o2o", caverphone.caverphone("kangaroo"));

        assertEquals("lion", caverphone.caverphone("lion"));
        assertEquals("l3", caverphone.caverphone("l")); // 2.0 only
        assertEquals("L3", caverphone.caverphone("lion"));

        assertEquals("mike", caverphone.caverphone("mike"));
        assertEquals("m2", caverphone.caverphone("m"));
        assertEquals("M", caverphone.caverphone("mike"));

        assertEquals("note", caverphone.caverphone("note"));
        assertEquals("n2t", caverphone.caverphone("note"));

        assertEquals("opposite", caverphone.caverphone("opposite"));
        assertEquals("o2p2p2i2t2e", caverphone.caverphone("opposite"));

        assertEquals("quicksand", caverphone.caverphone("quicksand"));
        assertEquals("q2k2s2s2a2n2d", caverphone.caverphone("quicksand"));

        assertEquals("tiger", caverphone.caverphone("tiger"));
        assertEquals("t2g2r2", caverphone.caverphone("tiger"));

        assertEquals("university", caverphone.caverphone("university"));
        assertEquals("u2n1v1e2r1s1i2t1y1", caverphone.caverphone("university"));

        assertEquals("water", caverphone.caverphone("water"));
        assertEquals("w2t2a2t2e2r", caverphone.caverphone("water"));

        assertEquals("xmas", caverphone.caverphone("xmas"));
        assertEquals("xm3s", caverphone.caverphone("xmas"));

        assertEquals("yes", caverphone.caverphone("yes"));
        assertEquals("y3", caverphone.caverphone("y")); // 2.0 only
        assertEquals("Yes", caverphone.caverphone("yes"));
    }

    @Test
    public void testCaverphoneEncodingWithStartOptions() {
        Caverphone caverphone = new Caverphone();

        assertEquals("cou2f", caverphone.caverphone("cough"));
        assertEquals("cou2f", caverphone.caverphone("coug"));
        assertEquals("cou2f", caverphone.caverphone("cough "));
        assertEquals("cou2f", caverphone.caverphone("cough h"));
        assertEquals("cou2f", caverphone.caverphone("cough h "));
        assertEquals("cou2f", caverphone.caverphone("cough ha"));
        assertEquals("cou2f", caverphone.caverphone("cough ha "));
        assertEquals("cou2f", caverphone.caverphone("cough has"));
        assertEquals("cou2f", caverphone.caverphone("cough has "));
        assertEquals("cou2f", caverphone.caverphone("cough hasp"));
        assertEquals("cou2f", caverphone.caverphone("cough hasp "));
        assertEquals("cou2f", caverphone.caverphone("cough hasps"));

        assertEquals("rou2f", caverphone.caverphone("rough"));
        assertEquals("rou2f", caverphone.caverphone("roug"));
        assertEquals("rou2f", caverphone.caverphone("rough "));
        assertEquals("rou2f", caverphone.caverphone("rough h"));
        assertEquals("rou2f", caverphone.caverphone("rough h "));
        assertEquals("rou2f", caverphone.caverphone("rough ha"));
        assertEquals("rou2f", caverphone.caverphone("rough ha "));
        assertEquals("rou2f", caverphone.caverphone("rough has"));
        assertEquals("rou2f", caverphone.caverphone("rough has "));
        assertEquals("rou2f", caverphone.caverphone("rough hasp"));
        assertEquals("rou2f", caverphone.caverphone("rough hasps"));

        assertEquals("tou2f", caverphone.caverphone("tough"));
        assertEquals("tou2f", caverphone.caverphone("toug"));
        assertEquals("tou2f", caverphone.caverphone("tough "));
        assertEquals("tou2f", caverphone.caverphone("tough h"));
        assertEquals("tou2f", caverphone.caverphone("tough h "));
        assertEquals("tou2f", caverphone.caverphone("tough ha"));
        assertEquals("tou2f", caverphone.caverphone("tough ha "));
        assertEquals("tou2f", caverphone.caverphone("tough has"));
        assertEquals("tou2f", caverphone.caverphone("tough has "));
        assertEquals("tou2f", caverphone.caverphone("tough hasp"));
        assertEquals("tou2f", caverphone.caverphone("tough hasps"));

        assertEquals("enou2f", caverphone.caverphone("enough"));
        assertEquals("enou2f", caverphone.caverphone("enough "));
        assertEquals("enou2f", caverphone.caverphone("enough h"));
        assertEquals("enou2f", caverphone.caverphone("enough h "));
        assertEquals("enou2f", caverphone.caverphone("enough ha"));
        assertEquals("enou2f", caverphone.caverphone("enough ha "));
        assertEquals("enou2f", caverphone.caverphone("enough has"));
        assertEquals("enou2f", caverphone.caverphone("enough has "));
        assertEquals("enou2f", caverphone.caverphone("enough hasp"));
        assertEquals("enou2f", caverphone.caverphone("enough hasps"));

        assertEquals("trou2f", caverphone.caverphone("trough")); // 2.0 only, note the spec says ^enough here again, c+p error I assume
        assertEquals("trou2f", caverphone.caverphone("troug")); // 2.0 only, note the spec says ^enough here again, c+p error I assume
        assertEquals("trou2f", caverphone.caverphone("trough ")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough h")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough h ")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough ha")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough ha ")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough has")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough has ")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough hasp")); // 2.0 only
        assertEquals("trou2f", caverphone.caverphone("trough hasps")); // 2.0 only

        assertEquals("cou2f", caverphone.caverphone("coug")); // same as 'cough'
        assertEquals("rou2f", caverphone.caverphone("roug")); // same as 'rough'
        assertEquals("tou2f", caverphone.caverphone("toug")); // same as 'tough'
        assertEquals("enou2f", caverphone.caverphone("enough")); // same as 'enough'
        assertEquals("trou2f", caverphone.caverphone("trough")); // same as 'trough'
    }

    @Test
    public void testCaverphoneEncodingWithSpecialChars() {
        Caverphone caverphone = new Caverphone();

        assertEquals("2q", caverphone.caverphone("cq"));
        assertEquals("si", caverphone.caverphone("ci"));
        assertEquals("se", caverphone.caverphone("ce"));
        assertEquals("sy", caverphone.caverphone("cy"));
        assertEquals("2ch", caverphone.caverphone("tch"));
        assertEquals("k", caverphone.caverphone("c"));
        assertEquals("k", caverphone.caverphone("q"));
        assertEquals("k", caverphone.caverphone("x"));
        assertEquals("f", caverphone.caverphone("v"));
        assertEquals("2g", caverphone.caverphone("dg"));
        assertEquals("sio", caverphone.caverphone("tio"));
        assertEquals("sia", caverphone.caverphone("tia"));
        assertEquals("t", caverphone.caverphone("d"));
        assertEquals("fh", caverphone.caverphone("ph"));
        assertEquals("p", caverphone.caverphone("b"));
        assertEquals("s2", caverphone.caverphone("sh"));
        assertEquals("s", caverphone.caverphone("z"));
        assertEquals("A", caverphone.caverphone("^[aeiou]"));
        assertEquals("3", caverphone.caverphone("[aeiou]"));
        assertEquals("y", caverphone.caverphone("j")); // 2.0 only
        assertEquals("Y3", caverphone.caverphone("^y3")); // 2.0 only
        assertEquals("A", caverphone.caverphone("^y")); // 2.0 only
        assertEquals("3", caverphone.caverphone("y")); // 2.0 only
        assertEquals("3kh3", caverphone.caverphone("3gh3"));
        assertEquals("22", caverphone.caverphone("gh"));
        assertEquals("k", caverphone.caverphone("g"));
        assertEquals("S", caverphone.caverphone("s+"));
        assertEquals("T", caverphone.caverphone("t+"));
        assertEquals("P", caverphone.caverphone("p+"));
        assertEquals("K", caverphone.caverphone("k+"));
        assertEquals("F", caverphone.caverphone("f+"));
        assertEquals("M", caverphone.caverphone("m+"));
        assertEquals("N", caverphone.caverphone("n+"));
        assertEquals("W3", caverphone.caverphone("w3"));
        //assertEquals("Wy", caverphone.caverphone("wy")); // 1.0 only
        assertEquals("Wh3", caverphone.caverphone("wh3"));
        assertEquals("3", caverphone.caverphone("w$")); // 2.0 only
        //assertEquals("Why", caverphone.caverphone("why")); // 1.0 only
        assertEquals("2", caverphone.caverphone("w"));
        assertEquals("A", caverphone.caverphone("^h"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("R3", caverphone.caverphone("r3"));
        assertEquals("3", caverphone.caverphone("r$")); // 2.0 only
        //assertEquals("Ry", caverphone.caverphone("ry")); // 1.0 only
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("L3", caverphone.caverphone("l3"));
        assertEquals("3", caverphone.caverphone("l$")); // 2.0 only
        //assertEquals("Ly", caverphone.caverphone("ly")); // 1.0 only
        assertEquals("2", caverphone.caverphone("l"));
        //assertEquals("j", caverphone.caverphone("y")); // 1.0 only
        //assertEquals("Y3", caverphone.caverphone("y3")); // 1.0 only
        //assertEquals("Y", caverphone.caverphone("y")); // 1.0 only
    }

    @Test
    public void testCaverphoneEncodingWithRemovals() {
        Caverphone caverphone = new Caverphone();

        assertEquals("", caverphone.caverphone("2"));
        assertEquals("A", caverphone.caverphone("3$")); // 2.0 only
        assertEquals("", caverphone.caverphone("3"));

        assertEquals("1111111111", caverphone.caverphone("1111111111"));
    }
}