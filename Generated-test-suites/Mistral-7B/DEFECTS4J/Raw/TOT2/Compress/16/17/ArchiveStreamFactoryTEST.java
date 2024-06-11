import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test with valid archives
        InputStream arInputStream = new FileInputStream("test/resources/valid_ar_file.tar.gz");
        ArchiveInputStream arInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertNotNull(arInput);
        arInput.close();
        arInputStream.close();

        InputStream zipInputStream = new FileInputStream("test/resources/valid_zip_file.zip");
        ArchiveInputStream zipInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertNotNull(zipInput);
        zipInput.close();
        zipInputStream.close();

        InputStream tarInputStream = new FileInputStream("test/resources/valid_tar_file.tar.gz");
        ArchiveInputStream tarInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertNotNull(tarInput);
        tarInput.close();
        tarInputStream.close();

        InputStream jarInputStream = new FileInputStream("test/resources/valid_jar_file.jar");
        ArchiveInputStream jarInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertNotNull(jarInput);
        jarInput.close();
        jarInputStream.close();

        InputStream cpioInputStream = new FileInputStream("test/resources/valid_cpio_file.cpio.bin");
        ArchiveInputStream cpioInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertNotNull(cpioInput);
        cpioInput.close();
        cpioInputStream.close();

        InputStream dumpInputStream = new FileInputStream("test/resources/valid_dump_file.dump");
        ArchiveInputStream dumpInput = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertNotNull(dumpInput);
        dumpInput.close();
        dumpInputStream.close();

        // Test with invalid archives
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, invalidInputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveInputStream with null archive");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            invalidInputStream.close();
        }

        InputStream invalidArchiveNameInputStream = new ByteArrayInputStream("InvalidArchiveName".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("InvalidArchiveName", invalidArchiveNameInputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveInputStream with invalid archive name");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            invalidArchiveNameInputStream.close();
        }

        InputStream invalidFormatInputStream = new ByteArrayInputStream("InvalidFormat".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, invalidFormatInputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveInputStream with invalid format");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            invalidFormatInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test with valid archives
        OutputStream arOutputStream = new FileOutputStream("test/temp/valid_ar_file.tar.gz");
        ArchiveOutputStream arOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        archiverTest(arOutput, "test/resources/valid_ar_file.tar.gz");
        arOutput.close();
        arOutputStream.close();

        OutputStream zipOutputStream = new FileOutputStream("test/temp/valid_zip_file.zip");
        ArchiveOutputStream zipOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        archiverTest(zipOutput, "test/resources/valid_zip_file.zip");
        zipOutput.close();
        zipOutputStream.close();

        OutputStream tarOutputStream = new FileOutputStream("test/temp/valid_tar_file.tar.gz");
        ArchiveOutputStream tarOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        archiverTest(tarOutput, "test/resources/valid_tar_file.tar.gz");
        tarOutput.close();
        tarOutputStream.close();

        OutputStream jarOutputStream = new FileOutputStream("test/temp/valid_jar_file.jar");
        ArchiveOutputStream jarOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        archiverTest(jarOutput, "test/resources/valid_jar_file.jar");
        jarOutput.close();
        jarOutputStream.close();

        OutputStream cpioOutputStream = new FileOutputStream("test/temp/valid_cpio_file.cpio.bin");
        ArchiveOutputStream cpioOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        archiverTest(cpioOutput, "test/resources/valid_cpio_file.cpio.bin");
        cpioOutput.close();
        cpioOutputStream.close();

        OutputStream dumpOutputStream = new FileOutputStream("test/temp/valid_dump_file.dump");
        ArchiveOutputStream dumpOutput = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        archiverTest(dumpOutput, "test/resources/valid_dump_file.dump");
        dumpOutput.close();
        dumpOutputStream.close();

        // Test with empty output stream
        OutputStream emptyOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, emptyOutputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveOutputStream with empty output stream");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            emptyOutputStream.close();
        }

        // Test with null output stream
        OutputStream nullOutputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, nullOutputStream);
            Assert.fail("Expected IllegalArgumentException when creating ArchiveOutputStream with null output stream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test with valid archives
        InputStream arInputStream = new FileInputStream("test/resources/valid_ar_file.tar.gz");
        ArchiveInputStream arInput = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        Assert.assertNotNull(arInput);
        ArchiveEntry entry = arInput.getNextEntry();
        Assert.assertNotNull(entry);
        byte[] data = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = arInput.read(data)) != -1) {
            // do something with the data
        }
        arInput.close();
        arInputStream.close();

        InputStream zipInputStream = new FileInputStream("test/resources/valid_zip_file.zip");
        ArchiveInputStream zipInput = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        Assert.assertNotNull(zipInput);
        zipInput.close();
        zipInputStream.close();

        InputStream tarInputStream = new FileInputStream("test/resources/valid_tar_file.tar.gz");
        ArchiveInputStream tarInput = new ArchiveStreamFactory().createArchiveInputStream(tarInputStream);
        Assert.assertNotNull(tarInput);
        tarInput.close();
        tarInputStream.close();

        InputStream jarInputStream = new FileInputStream("test/resources/valid_jar_file.jar");
        ArchiveInputStream jarInput = new ArchiveStreamFactory().createArchiveInputStream(jarInputStream);
        Assert.assertNotNull(jarInput);
        jarInput.close();
        jarInputStream.close();

        InputStream cpioInputStream = new FileInputStream("test/resources/valid_cpio_file.cpio.bin");
        ArchiveInputStream cpioInput = new ArchiveStreamFactory().createArchiveInputStream(cpioInputStream);
        Assert.assertNotNull(cpioInput);
        cpioInput.close();
        cpioInputStream.close();

        InputStream dumpInputStream = new FileInputStream("test/resources/valid_dump_file.dump");
        ArchiveInputStream dumpInput = new ArchiveStreamFactory().createArchiveInputStream(dumpInputStream);
        Assert.assertNotNull(dumpInput);
        dumpInput.close();
        dumpInputStream.close();

        // Test with invalid archives
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(invalidInputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveInputStream from invalid archive");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            invalidInputStream.close();
        }

        InputStream invalidFormatInputStream = new ByteArrayInputStream("InvalidFormat".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(invalidFormatInputStream);
            Assert.fail("Expected ArchiveException when creating ArchiveInputStream from invalid format");
        } catch (ArchiveException e) {
            // Expected
        } finally {
            invalidFormatInputStream.close();
        }

        InputStream invalidStreamInputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(invalidStreamInputStream);
            Assert.fail("Expected IOException when creating ArchiveInputStream from invalid stream");
        } catch (IOException e) {
            // Expected
        } finally {
            invalidStreamInputStream.close();
        }
    }

    private void archiverTest(ArchiveOutputStream output, String inputFileName) throws Exception {
        InputStream input = new FileInputStream(inputFileName);
        ArchiveEntry entry = new JarArchiveEntry("test/temp/test.txt", new File("test/temp/test.txt").length());
        output.putArchiveEntry(entry);
        IOUtils.copy(input, output);
        output.closeArchiveEntry();
        input.close();
        output.finish();
        Assert.assertEquals(new File(inputFileName).length(), new File("test/temp/test.txt").length());
        Assert.assertEquals(new String(IOUtils.toByteArray(new FileInputStream("test/temp/test.txt"))), new String(IOUtils.toByteArray(new File("test/resources/test.txt"))));
    }
}