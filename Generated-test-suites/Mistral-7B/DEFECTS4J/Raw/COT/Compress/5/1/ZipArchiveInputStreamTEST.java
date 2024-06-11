package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ZipArchiveInputStream.class})
public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry_NormalCase() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_EndOfCentralDirectoryReached() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any()).thenReturn(new byte[0]);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_UnexpectedSignature() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any())
                .thenReturn(new byte[]{0x04, 0x05, 0x4b, 0x51, 0x00, 0x00, 0x00, 0x00});
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextZipEntry_ErrorDuringReading() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any())
                .thenThrow(new IOException("Test error during reading"));
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextEntry_NormalCase() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextEntry();
        assertNotNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextEntry_EndOfCentralDirectoryReached() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any()).thenReturn(new byte[0]);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextEntry_UnexpectedSignature() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any())
                .thenReturn(new byte[]{0x04, 0x05, 0x4b, 0x51, 0x00, 0x00, 0x00, 0x00});
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testGetNextEntry_ErrorDuringReading() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "readFully", (Object[]) any())
                .thenThrow(new IOException("Test error during reading"));
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextEntry();
        assertNull(zipArchiveEntry);
    }

    @Test
    public void testRead_DataIsAvailable() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[5];
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, read);
    }

    @Test
    public void testRead_EndOfStreamReached() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[15];
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    @Test
    public void testRead_ErrorDuringReading() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        PowerMockito.when(inputStream, "read").thenThrow(new IOException("Test error during reading"));
        byte[] buffer = new byte[10];
        int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        verify(inputStream).close();
    }

    @Test(expected = IOException.class)
    public void testClose_StreamIsAlreadyClosed() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        zipArchiveInputStream.close();
    }

    @Test
    public void testSkip() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.skip(10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_NegativeValue() throws Exception {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.skip(-1);
    }
}