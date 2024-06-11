package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

import java.math.BigInteger;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertEquals(1, newUnix.getVersion());
        assertNull(newUnix.getUID());
        assertNull(newUnix.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(12345);
        assertEquals(12345, newUnix.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(54321);
        assertEquals(54321, newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(10);
        newUnix.setGID(10);

        ZipShort expectedLength = new ZipShort((short) (1 + 1 + 1 + 8 + 8));
        assertEquals(expectedLength, newUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(10);
        newUnix.setGID(10);

        ZipShort expectedLength = new ZipShort((short) (1 + 1 + 1 + 8 + 8));
        assertEquals(expectedLength, newUnix.getCentralDirectoryLength());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] {
                0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };

        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, newUnix.getVersion());
        assertEquals(BigInteger.ZERO, newUnix.getUID());
        assertEquals(BigInteger.ZERO, newUnix.getGID());

        byte[] uidBytes = newUnix.getUID().toByteArray();
        assertEquals(1, uidBytes.length);
        assertEquals(0, uidBytes[0]);

        byte[] gidBytes = newUnix.getGID().toByteArray();
        assertEquals(1, gidBytes.length);
        assertEquals(0, gidBytes[0]);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[] {
                0x78, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00
        };

        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.parseFromLocalFileData(data, 0, data.length);

        // Should not reach this point
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] input = new byte[] {0, 0, 0, 1};
        byte[] expectedOutput = new byte[] {1};
        byte[] output = X7875_NewUnix.trimLeadingZeroesForceMinLength(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthEmptyArray() {
        byte[] input = new byte[] {};
        byte[] expectedOutput = new byte[] {};
        byte[] output = X7875_NewUnix.trimLeadingZeroesForceMinLength(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthNonZeroArray() {
        byte[] input = new byte[] {1, 2, 3, 4};
        byte[] expectedOutput = new byte[] {1, 2, 3, 4};
        byte[] output = X7875_NewUnix.trimLeadingZeroesForceMinLength(input);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthLeadingZeros() {
        byte[] input = new byte[] {0, 0, 0, 0, 1};
        byte[] expectedOutput = new byte[] {1};
        byte[] output = X7875_NewUnix.trimLeadingZeroesForceMinLength(input);
        assertArrayEquals(expectedOutput, output);
    }
}