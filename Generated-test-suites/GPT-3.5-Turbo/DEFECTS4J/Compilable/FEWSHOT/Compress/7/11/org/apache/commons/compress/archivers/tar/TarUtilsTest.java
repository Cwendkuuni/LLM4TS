package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {49, 50, 51}; // ASCII values for "123"
        assertEquals(123, TarUtils.parseOctal(buffer, 0, buffer.length));
        
        byte[] buffer2 = {54, 55, 56}; // ASCII values for "678"
        assertEquals(678, TarUtils.parseOctal(buffer2, 0, buffer2.length));
        
        byte[] buffer3 = {48}; // ASCII value for "0"
        assertEquals(0, TarUtils.parseOctal(buffer3, 0, buffer3.length));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidDigit() {
        byte[] buffer = {49, 54, 49}; // ASCII values for "161"
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }
    
    @Test
    public void testParseName() {
        byte[] buffer = {102, 111, 111}; // ASCII values for "foo"
        assertEquals("foo", TarUtils.parseName(buffer, 0, buffer.length));
        
        byte[] buffer2 = {98, 97, 114}; // ASCII values for "bar"
        assertEquals("bar", TarUtils.parseName(buffer2, 0, buffer2.length));
        
        byte[] buffer3 = {98, 97, 122, 0}; // ASCII values for "baz"
        assertEquals("baz", TarUtils.parseName(buffer3, 0, buffer3.length));
    }
    
    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[10];
        assertEquals(8, TarUtils.formatNameBytes(name, buf, 0, name.length()));
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
        assertEquals(0, buf[8]);
        assertEquals(0, buf[9]);
    }
    
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(51, buffer[6]);
        assertEquals(50, buffer[7]);
        assertEquals(49, buffer[8]);
        assertEquals(0, buffer[9]);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidValue() {
        long value = 1000000000000000000L; // Value can't fit in octal number buffer of length 10
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }
    
    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        assertEquals(8, TarUtils.formatOctalBytes(value, buf, 0, buf.length));
        assertEquals(51, buf[6]);
        assertEquals(50, buf[7]);
        assertEquals(49, buf[8]);
        assertEquals(0, buf[9]);
    }
    
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        assertEquals(9, TarUtils.formatLongOctalBytes(value, buf, 0, buf.length));
        assertEquals(51, buf[7]);
        assertEquals(50, buf[8]);
        assertEquals(0, buf[9]);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        assertEquals(9, TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length));
        assertEquals(51, buf[7]);
        assertEquals(50, buf[8]);
        assertEquals(0, buf[9]);
    }
    
    @Test
    public void testComputeCheckSum() {
        byte[] buf = {49, 50, 51}; // ASCII values for "123"
        assertEquals(369, TarUtils.computeCheckSum(buf));
        
        byte[] buf2 = {54, 55, 56}; // ASCII values for "678"
        assertEquals(642, TarUtils.computeCheckSum(buf2));
        
        byte[] buf3 = {48}; // ASCII value for "0"
        assertEquals(48, TarUtils.computeCheckSum(buf3));
    }
}