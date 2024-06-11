package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test case 1
        String sequence1 = "hello world";
        int expected1 = 11;
        int actual1 = Utf8.encodedLength(sequence1);
        assertEquals(expected1, actual1);

        // Test case 2
        String sequence2 = "";
        int expected2 = 0;
        int actual2 = Utf8.encodedLength(sequence2);
        assertEquals(expected2, actual2);

        // Test case 3
        String sequence3 = "äöü";
        int expected3 = 6;
        int actual3 = Utf8.encodedLength(sequence3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test case 1
        String sequence1 = "hello world";
        int start1 = 0;
        int expected1 = 11;
        int actual1 = Utf8.encodedLengthGeneral(sequence1, start1);
        assertEquals(expected1, actual1);

        // Test case 2
        String sequence2 = "";
        int start2 = 0;
        int expected2 = 0;
        int actual2 = Utf8.encodedLengthGeneral(sequence2, start2);
        assertEquals(expected2, actual2);

        // Test case 3
        String sequence3 = "äöü";
        int start3 = 0;
        int expected3 = 6;
        int actual3 = Utf8.encodedLengthGeneral(sequence3, start3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testIsWellFormed() {
        // Test case 1
        byte[] bytes1 = { 0x61, 0x62, 0x63 };
        boolean expected1 = true;
        boolean actual1 = Utf8.isWellFormed(bytes1);
        assertEquals(expected1, actual1);

        // Test case 2
        byte[] bytes2 = {};
        boolean expected2 = true;
        boolean actual2 = Utf8.isWellFormed(bytes2);
        assertEquals(expected2, actual2);

        // Test case 3
        byte[] bytes3 = { (byte)0xC3, (byte)0xA4, (byte)0xC3, (byte)0xB6, (byte)0xC3, (byte)0xBC };
        boolean expected3 = true;
        boolean actual3 = Utf8.isWellFormed(bytes3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test case 1
        byte[] bytes1 = { 0x61, 0x62, 0x63 };
        int off1 = 0;
        int len1 = 3;
        boolean expected1 = true;
        boolean actual1 = Utf8.isWellFormed(bytes1, off1, len1);
        assertEquals(expected1, actual1);

        // Test case 2
        byte[] bytes2 = {};
        int off2 = 0;
        int len2 = 0;
        boolean expected2 = true;
        boolean actual2 = Utf8.isWellFormed(bytes2, off2, len2);
        assertEquals(expected2, actual2);

        // Test case 3
        byte[] bytes3 = { (byte)0xC3, (byte)0xA4, (byte)0xC3, (byte)0xB6, (byte)0xC3, (byte)0xBC };
        int off3 = 0;
        int len3 = 6;
        boolean expected3 = true;
        boolean actual3 = Utf8.isWellFormed(bytes3, off3, len3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test case 1
        byte[] bytes1 = { (byte)0x80 };
        int off1 = 0;
        int end1 = 1;
        boolean expected1 = false;
        boolean actual1 = Utf8.isWellFormedSlowPath(bytes1, off1, end1);
        assertEquals(expected1, actual1);

        // Test case 2
        byte[] bytes2 = { (byte)0xC3, (byte)0x80, (byte)0x80 };
        int off2 = 0;
        int end2 = 3;
        boolean expected2 = false;
        boolean actual2 = Utf8.isWellFormedSlowPath(bytes2, off2, end2);
        assertEquals(expected2, actual2);

        // Test case 3
        byte[] bytes3 = { (byte)0xE0, (byte)0x80, (byte)0x80, (byte)0x80 };
        int off3 = 0;
        int end3 = 4;
        boolean expected3 = false;
        boolean actual3 = Utf8.isWellFormedSlowPath(bytes3, off3, end3);
        assertEquals(expected3, actual3);
    }
}