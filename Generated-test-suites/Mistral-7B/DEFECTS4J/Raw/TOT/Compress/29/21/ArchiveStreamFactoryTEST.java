package org.apache.commons.compress.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creation of input streams.
     */
    @Test
    public void testInput() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream in = new ByteArrayInputStream(
                "   !<arch>\n" +
                        "  type 7z0\n" +
                        "name test.7z\n" +
                        "volname Vol\n" +
                        "version 1.0\n" +
                        "created 2022-05-01 11:11:11\n" +
                        "size 100\n" +
                        "\n".getBytes(StandardCharsets.US_ASCII));

        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, in);
        Assert.assertEquals("7z", ais.getFormat().getName());
        Assert.assertNull(ais.getNextEntry());
        ais.close();
    }

    /**
     * Test creation of output streams.
     */
    @Test
    public void testOutput() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        JarArchiveEntry entry = new JarArchiveEntry("test.txt");
        aos.putNextEntry(entry);
        String data = "Hello World!";
        aos.write(data.getBytes(StandardCharsets.UTF_8), 0, data.length());
        aos.closeArchiveEntry();
        aos.finish();
        aos.close();

        byte[] bytes = ((ByteArrayOutputStream) out).toByteArray();
        InputStream test = new ByteArrayInputStream(bytes);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, test);
        JarArchiveEntry entry2 = (JarArchiveEntry) ais.getNextEntry();
        String content = new String(IOUtils.toByteArray(ais), StandardCharsets.UTF_8);
        Assert.assertEquals("Hello World!\n", content);
        Assert.assertEquals("test.txt", entry2.getName());
        ais.close();
    }

    /**
     * Test creation of input streams from existing files.
     */
    @Test
    public void testExistingFiles() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP,
                ArchiveStreamFactory.class.getResourceAsStream("test.zip"));

        JarArchiveEntry entry = (JarArchiveEntry) ais.getNextEntry();
        Assert.assertEquals("ArchiveStreamFactoryTest.class", entry.getName());
        Assert.assertNotNull(ais.getInputStream());
        String content = new String(IOUtils.toByteArray(ais), StandardCharsets.UTF_8);
        Assert.assertFalse(content.isEmpty());

        ais.close();
    }

    /**
     * Test creation of input streams from null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    /**
     * Test creation of input streams with incorrect format.
     */
    @Test(expected = ArchiveException.class)
    public void testIncorrectFormat() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream in = new ByteArrayInputStream("This is not an archive".getBytes(StandardCharsets.UTF_8));
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
    }

    /**
     * Test creation of output streams with null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    /**
     * Test creation of output streams with incorrect format.
     */
    @Test(expected = ArchiveException.class)
    public void testIncorrectOutputFormat() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.GZIP, out);
    }

    /**
     * Test creation of input streams with unknown format.
     */
    @Test(expected = ArchiveException.class)
    public void testUnknownFormat() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream in = new ByteArrayInputStream("This is not a known archive format".getBytes(StandardCharsets.UTF_8));
        factory.createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, in);
    }
}