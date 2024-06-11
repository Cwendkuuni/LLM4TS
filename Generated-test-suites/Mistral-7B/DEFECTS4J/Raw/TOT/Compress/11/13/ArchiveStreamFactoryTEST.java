import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(getSampleZipData().getBytes(StandardCharsets.UTF_8));
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("test1.xml", archiveEntry.getName());

        // read data
        byte[] data = new byte[archiveEntry.getSize()];
        archiveInputStream.read(data, 0, data.length);
        Assert.assertArrayEquals(getSampleFileContent1().getBytes(StandardCharsets.UTF_8), data);

        archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);

        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownFormat() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(getSampleZipData().getBytes(StandardCharsets.UTF_8));

        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);

        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test1.txt"));
        archiveOutputStream.write(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        byte[] archiveData = outputStream.toByteArray();
        OutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(archiveData));

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("test1.txt", archiveEntry.getName());

        // read data
        byte[] data = new byte[archiveEntry.getSize()];
        archiveInputStream.read(data, 0, data.length);
        Assert.assertArrayEquals(testData, data);

        archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);

        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(getSampleZipData().getBytes(StandardCharsets.UTF_8));
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(archiveEntry);
        Assert.assertEquals("test1.xml", archiveEntry.getName());

        // read data
        byte[] data = new byte[archiveEntry.getSize()];
        archiveInputStream.read(data, 0, data.length);
        Assert.assertArrayEquals(getSampleFileContent1().getBytes(StandardCharsets.UTF_8), data);

        archiveEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(archiveEntry);

        archiveInputStream.close();
    }

    private JarArchiveEntry getSampleFileEntry() {
        return new JarArchiveEntry("test1.xml");
    }

    private JarArchiveEntry getSampleFileEntryWithData() throws IOException {
        return new JarArchiveEntry("test1.xml").setData(getSampleFileContent1().getBytes(StandardCharsets.UTF_8));
    }

    private String getSampleZipData() {
        return "PK\n\n\032\015\000\012test1.xml\000\012\000\006test1.xml\n\000\002\012test1.xml\n\000\001\000\000test1.xml\000\000\000test1.xml\n\000\002test1.xml\000\012\000\006test1.xml\014test1.xml\n\000\001test1.xml\n\000\000\000test1.xml\n\000\004test1.xml\nHello World\000\000\000test1.xml\n\000\002test1.xml\000\012\000\006test1.xml\014test1.xml\n\000\001test1.xml\n\000\000\000test1.xml\n\000\005test1.xml\nHello World\n\000\000\000test1.xml\n\000\002test1.xml\000\012\000\006test1.xml\014test1.xml\n\000\001test1.xml\n\000\000\000test1.xml\n\000\004test1.xml\nHello World\n\000\000\000test1.xml\n\000\002test1.xml\000\012\000\006test1.xml\014test1.xml\n\000\001test1.xml\n\000\000\000test1.xml\n\000\005test1.xml\nHello World\n";
    }

    private String getSampleFileContent1() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n  <element1>Hello</element1>\n  <element2>World</element2>\n</root>";
    }
}