package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.ArjArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{}));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{'a'}));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{'a', 'r'}));
            assertNotNull(in);
            assertInstanceOf(ArArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[]{'a', 'r', 'j'}));
            assertNotNull(in);
            assertInstanceOf(ArjArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[]{'c', 'p', 'i', 'o'}));
            assertNotNull(in);
            assertInstanceOf(CpioArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[]{'d', 'u', 'm', 'p'}));
            assertNotNull(in);
            assertInstanceOf(DumpArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[]{'j', 'a', 'r'}));
            assertNotNull(in);
            assertInstanceOf(JarArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[]{'t', 'a', 'r'}));
            assertNotNull(in);
            assertInstanceOf(TarArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
            assertNull(in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[]{'z', 'i', 'p'}));
            assertNotNull(in);
            assertInstanceOf(ZipArchiveInputStream.class, in);

            in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, null);
            assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0])));

            // Test unknown archive format
            in = new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[]{'u', 'n', 'k', 'n', 'o', 'w', 'n'}));
            assertNull(in);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{}));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[]{'a', 'r'}));
            assertNotNull(out);
            assertInstanceOf(ArArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[]{'a', 'r', 'j'}));
            assertNotNull(out);
            assertInstanceOf(ArjArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[]{'c', 'p', 'i', 'o'}));
            assertNotNull(out);
            assertInstanceOf(CpioArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[]{'d', 'u', 'm', 'p'}));
            assertNotNull(out);
            assertInstanceOf(DumpArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[]{'j', 'a', 'r'}));
            assertNotNull(out);
            assertInstanceOf(JarArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[]{'t', 'a', 'r'}));
            assertNotNull(out);
            assertInstanceOf(TarArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[]{'z', 'i', 'p'}));
            assertNotNull(out);
            assertInstanceOf(ZipArchiveOutputStream.class, out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, null);
            assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0])));

            // Test unknown archive format
            out = new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayInputStream(new byte[]{'u', 'n', 'k', 'n', 'o', 'w', 'n'}));
            assertNull(out);

            out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream(1024));
            assertNotNull(out);
            assertInstanceOf(ZipArchiveOutputStream.class, out);

            // Test creating an archive with a valid entry
            ArchiveOutputStream zipOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream(1024));
            zipOut.putNextEntry(new JarEntry("test.txt"));
            zipOut.write("Hello world!".getBytes());
            zipOut.closeArchiveEntry();
            zipOut.close();

            // Test reading the archive
            ArchiveInputStream zipIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipOut.toByteArray()));
            JarEntry entry = (JarEntry)zipIn.getNextEntry();
            assertNotNull(entry);
            assertNotNull(entry.getName());
            assertEquals("test.txt", entry.getName());
            byte[] data = new byte[entry.getSize()];
            IOUtils.readFully(zipIn, data);
            assertEquals("Hello world!", new String(data));
            zipIn.close();

        } catch (IOException | ArchiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAutoDetectArchiveFormat() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[]{'z', 'i', 'p'});
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAutoDetectInvalidArchiveFormat() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[]{'u', 'n', 'k', 'n', 'o', 'w', 'n'});
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            assertNull(archiveInputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}