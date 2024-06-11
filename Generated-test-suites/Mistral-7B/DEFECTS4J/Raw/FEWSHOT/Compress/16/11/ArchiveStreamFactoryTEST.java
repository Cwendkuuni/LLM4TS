package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarHeader;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipHeader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test known archive types
        InputStream knownArchiveStream = new ByteArrayInputStream(
                "PK\u0005\u0002\u0014\u0000\u000a" + // Zip signature
                        "0\u0000\u0014\u0000\u0000" + // Central directory signature
                        "local header signature" + // Local header signature
                        "name length\u0000\u0001\u0000\u0002" + // Name
                        "method length\u0000\u0002\u0014\u0000" + // Method
                        "compression method\u0000\u0008\u0000" + // Compression method
                        "modified time\u0000\u0000\u0000\u0000\u0000\u0000" + // Modified time
                        "crc32\u0000\u0000\u0000\u0000\u0000\u0000" + // CRC32
                        "size\u0000\u0001\u0000\u0002" + // Size
                        "extra field length\u0000\u0000\u0000" + // Extra field length
                        "comment length\u0000\u0000\u0000" + // Comment length
                        "\u0000\u0001\u0000\u0002" + // Data length
                        "data" + // Data
                        "\u0000"); // End of central directory record signature

        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, knownArchiveStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        TarHeader tarHeader = new TarHeader();
        tarHeader.setMagic(TarArchiveInputStream.MAGIC);
        tarHeader.setVersionMajor(1);
        tarHeader.setVersionMinor(10);
        tarHeader.setUserID("user");
        tarHeader.setGroupID("group");
        tarHeader.setSize(1024);
        tarHeader.setModTime(System.currentTimeMillis());
        tarHeader.setChecksum(0x12345678);
        tarHeader.setName("test.txt");

        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        tarArchiveOutputStream.putNextEntry(tarHeader);
        tarArchiveOutputStream.write("test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        InputStream tarInputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);

        ZipHeader zipHeader = new ZipHeader();
        zipHeader.setMagic(ZipArchiveInputStream.SIG);
        zipHeader.setVersionMadeBy(1);
        zipHeader.setVersionNeeded(1);
        zipHeader.setFlags(0);
        zipHeader.setCompressionMethod(0);
        zipHeader.setModTime(System.currentTimeMillis());
        zipHeader.setCRC32(0x12345678);
        zipHeader.setCompressedSize(1024);
        zipHeader.setSize(1024);
        zipHeader.setName("test.txt");

        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipArchiveOutputStream.write("test content".getBytes());
        zipArchiveOutputStream.closeArchiveEntry();
        zipArchiveOutputStream.finish();

        InputStream zipInputStream = new ByteArrayInputStream(zipOutputStream.toByteArray());
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);

        // Test unknown archive type
        InputStream unknownArchiveStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, unknownArchiveStream);
            assertNull(unknownArchiveInputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test known archive types
        OutputStream knownArchiveStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, knownArchiveStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream.putNextEntry(new ZipEntry("test.txt"));
        archiveOutputStream.write("test content".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        TarHeader tarHeader = new TarHeader();
        tarHeader.setMagic(TarArchiveOutputStream.MAGIC);
        tarHeader.setVersionMajor(1);
        tarHeader.setVersionMinor(10);
        tarHeader.setUserID("user");
        tarHeader.setGroupID("group");
        tarHeader.setSize(1024);
        tarHeader.setModTime(System.currentTimeMillis());
        tarHeader.setChecksum(0x12345678);
        tarHeader.setName("test.txt");

        OutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        tarArchiveOutputStream.putNextEntry(tarHeader);
        tarArchiveOutputStream.write("test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        OutputStream tarArchiveOutputStream2 = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        tarArchiveOutputStream2.putNextEntry(tarHeader);
        tarArchiveOutputStream2.write("test content".getBytes());
        tarArchiveOutputStream2.closeArchiveEntry();
        tarArchiveOutputStream2.finish();

        assertArrayEquals(tarOutputStream.toByteArray(), tarArchiveOutputStream2.toByteArray());

        // Test unknown archive type
        OutputStream unknownArchiveStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, unknownArchiveStream);
            assertNull(unknownArchiveOutputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }
}