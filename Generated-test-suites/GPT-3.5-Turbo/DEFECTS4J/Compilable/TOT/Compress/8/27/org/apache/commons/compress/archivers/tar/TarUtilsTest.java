package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {
    
    // test the parseOctal method
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = {49};
        TarUtils.parseOctal(buffer, 0, 1);
    }
    
    @Test
    public void testParseOctal_AllNULs() {
        byte[] buffer = {0};
        long result = TarUtils.parseOctal(buffer, 0, 1);
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_TrailingSpaceMissing() {
        byte[] buffer = {49, 50};
        TarUtils.parseOctal(buffer, 0, 2);
    }
    
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = {49, 50, 32};
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(12, result);
    }
    
    // test the parseName method
    
    @Test
    public void testParseName_TrailingNull() {
        byte[] buffer = {116, 101, 115, 116, 0, 97, 98, 99};
        String result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("test", result);
    }
    
    @Test
    public void testParseName_BufferLengthReached() {
        byte[] buffer = {116, 101, 115, 116, 0, 97, 98, 99};
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("test", result);
    }
    
    // test the formatNameBytes method
    
    @Test
    public void testFormatNameBytes_BufferIsLonger() {
        String name = "test";
        byte[] buf = new byte[10];
        int result = TarUtils.formatNameBytes(name, buf, 0, 10);
        assertEquals(10, result);
        assertArrayEquals(new byte[] {116, 101, 115, 116, 0, 0, 0, 0, 0, 0}, buf);
    }
    
    @Test
    public void testFormatNameBytes_NameIsLonger() {
        String name = "test";
        byte[] buf = new byte[2];
        int result = TarUtils.formatNameBytes(name, buf, 0, 2);
        assertEquals(2, result);
        assertArrayEquals(new byte[] {116, 101}, buf);
    }
    
    // test the formatUnsignedOctalString method
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueDoesNotFitInBuffer() {
        byte[] buffer = new byte[1];
        TarUtils.formatUnsignedOctalString(1234, buffer, 0, 1);
    }
    
    @Test
    public void testFormatUnsignedOctalString_ValueIsZero() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(0, buffer, 0, 5);
        assertArrayEquals(new byte[] {48, 48, 48, 48, 48}, buffer);
    }
    
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(1234, buffer, 0, 5);
        assertArrayEquals(new byte[] {48, 60, 64, 0, 0}, buffer);
    }
    
    // test the formatOctalBytes method
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueDoesNotFitInBuffer() {
        byte[] buffer = new byte[2];
        TarUtils.formatOctalBytes(123, buffer, 0, 2);
    }
    
    @Test
    public void testFormatOctalBytes_ValidInput() {
        byte[] buffer = new byte[5];
        int result = TarUtils.formatOctalBytes(123, buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new byte[] {49, 50, 32, 0, 0}, buffer);
    }
    
    // test the formatLongOctalBytes method
    
    @Test
    public void testFormatLongOctalBytes_ValidInput() {
        byte[] buffer = new byte[5];
        int result = TarUtils.formatLongOctalBytes(123, buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new byte[] {49, 50, 32, 0, 0}, buffer);
    }
    
    // test the formatCheckSumOctalBytes method
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_ValueDoesNotFitInBuffer() {
        byte[] buffer = new byte[3];
        TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 3);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        byte[] buffer = new byte[5];
        int result = TarUtils.formatCheckSumOctalBytes(123, buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new byte[] {49, 50, 0, 32, 0}, buffer);
    }
    
    // test the computeCheckSum method
    
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = {49, 50, 51};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(150, result);
    }
}