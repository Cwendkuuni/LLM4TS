package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarHeader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromName() throws Exception {
        assertInstanceOf(ArArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
        assertInstanceOf(JarArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
        assertInstanceOf(TarArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
        assertInstanceOf(ZipArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
        assertInstanceOf(CpioArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
        assertInstanceOf(DumpArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        TarHeader tarHeader = new TarHeader("test.txt", (short) 0, (short) 0, 0, 0, 0, "".getBytes());
        ByteArrayOutputStream tarHeaderOut = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(tarHeaderOut);
        tarOut.putNextEntry(new TarEntry("test.txt"));
        tarOut.close();
        byte[] tarHeaderBytes = tarHeaderOut.toByteArray();
        tarHeaderOut.close();

        InputStream tarInputStream = new ByteArrayInputStream(tarHeaderBytes);

        assertInstanceOf(ArArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(in));
        assertInstanceOf(JarArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(in));
        assertInstanceOf(TarArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(tarInputStream));
        assertInstanceOf(ZipArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(in));
        assertInstanceOf(CpioArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(in));
        assertInstanceOf(DumpArchiveInputStream.class, ArchiveStreamFactory.createArchiveInputStream(in));

        tarInputStream.close();
        in.close();
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiverName() throws Exception {
        ArchiveStreamFactory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateArchiveOutputStreamFromName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        archiveOutputStream.putNextEntry(new TarEntry("test.txt"));
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        byte[] expectedBytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x41, (byte) 0x54, (byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x43, (byte) 0x52, (byte) 0x53, (byte) 0x53, (byte) 0x42, (byte) 0x79, (byte) 0x5c, (byte) 0x43, (byte) 0x79, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6f, (byte) 0x63, (byte) 0x68, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x54, (byte) 0x45, (byte) 0x4e, (byte) 0x44, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x53, (byte) 0x79, (byte) 0x73, (byte) 0x71, (byte) 0x66, (byte) 0x65, (byte) 0x72, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        assertArrayEquals(expectedBytes, ((ByteArrayOutputStream) out).toByteArray());
        out.close();

        assertInstanceOf(ArArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream()));
        assertInstanceOf(JarArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream()));
        assertInstanceOf(TarArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream()));
        assertInstanceOf(ZipArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream()));
        assertInstanceOf(CpioArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream()));
        assertInstanceOf(DumpArchiveOutputStream.class, ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream()));
    }

    @Test
    public void testNullInput() {
        assertNull(ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, null));
        assertNull(ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null));
    }

    @Test
    public void testNullOutputStream() {
        try {
            ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromBytes() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveOutputStream.putArchiveEntry(new TarEntry("test.txt", "test content".getBytes()));
        archiveOutputStream.writeTo(inputStream, "test content".length());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        byte[] expectedBytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x41, (byte) 0x54, (byte) 0x52, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6f, (byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x43, (byte) 0x52, (byte) 0x53, (byte) 0x53, (byte) 0x42, (byte) 0x79, (byte) 0x5c, (byte) 0x43, (byte) 0x79, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6f, (byte) 0x63, (byte) 0x68, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x54, (byte) 0x45, (byte) 0x4e, (byte) 0x44, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x41, (byte) 0x75, (byte) 0x74, (byte) 0x65, (byte) 0x52, (byte) 0x65, (byte) 0x63, (byte) 0x74, (byte) 0x31, (byte) 0x32, (byte) 0x20, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x75, (byte) 0x54, (byte) 0x20, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x65, (byte) 0x6e, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x65, (byte) 0x78, (byte) 0x69, (byte) 0x73, (byte) 0x74, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        assertArrayEquals(expectedBytes, ((ByteArrayOutputStream) outputStream).toByteArray());
        outputStream.close();
    }
}