import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test creating a ZIP archive input stream from a known input stream
        InputStream expectedZipInputStream = new ByteArrayInputStream(TEST_ZIP_SIGNATURE.getBytes());
        ArchiveInputStream actualZipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, expectedZipInputStream);
        Assert.assertSame(ZipArchiveInputStream.class, actualZipInputStream.getClass());

        // Test creating a non-existent archive input stream
        InputStream nonExistentInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualNonExistentInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", nonExistentInputStream);
        Assert.assertNull(actualNonExistentInputStream);

        // Test creating a JAR archive input stream from a known input stream
        InputStream expectedJarInputStream = new ByteArrayInputStream(TEST_JAR_SIGNATURE.getBytes());
        ArchiveInputStream actualJarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, expectedJarInputStream);
        Assert.assertSame(JarArchiveInputStream.class, actualJarInputStream.getClass());

        // Test creating an AR archive input stream from a known input stream
        InputStream expectedArInputStream = new ByteArrayInputStream(TEST_AR_SIGNATURE.getBytes());
        ArchiveInputStream actualArInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, expectedArInputStream);
        Assert.assertSame(ArArchiveInputStream.class, actualArInputStream.getClass());

        // Test creating a CPIO archive input stream from a known input stream
        InputStream expectedCpioInputStream = new ByteArrayInputStream(TEST_CPIO_SIGNATURE.getBytes());
        ArchiveInputStream actualCpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, expectedCpioInputStream);
        Assert.assertSame(CpioArchiveInputStream.class, actualCpioInputStream.getClass());

        // Test creating a TAR archive input stream from a known input stream
        InputStream expectedTarInputStream = new ByteArrayInputStream(TEST_TAR_SIGNATURE.getBytes());
        ArchiveInputStream actualTarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, expectedTarInputStream);
        Assert.assertSame(TarArchiveInputStream.class, actualTarInputStream.getClass());

        // Test creating a DUMP archive input stream from a known input stream
        InputStream expectedDumpInputStream = new ByteArrayInputStream(TEST_DUMP_SIGNATURE.getBytes());
        ArchiveInputStream actualDumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, expectedDumpInputStream);
        Assert.assertSame(DumpArchiveInputStream.class, actualDumpInputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test creating a ZIP archive output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actualZipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        Assert.assertSame(ZipArchiveOutputStream.class, actualZipOutputStream.getClass());

        // Test creating an AR archive output stream
        OutputStream outputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream actualArOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream2);
        Assert.assertSame(ArArchiveOutputStream.class, actualArOutputStream.getClass());

        // Test creating a CPIO archive output stream
        OutputStream outputStream3 = new ByteArrayOutputStream();
        ArchiveOutputStream actualCpioOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream3);
        Assert.assertSame(CpioArchiveOutputStream.class, actualCpioOutputStream.getClass());

        // Test creating a JAR archive output stream
        OutputStream outputStream4 = new ByteArrayOutputStream();
        ArchiveOutputStream actualJarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream4);
        Assert.assertSame(JarArchiveOutputStream.class, actualJarOutputStream.getClass());

        // Test creating a TAR archive output stream
        OutputStream outputStream5 = new ByteArrayOutputStream();
        ArchiveOutputStream actualTarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream5);
        Assert.assertSame(TarArchiveOutputStream.class, actualTarOutputStream.getClass());

        // Test creating a DUMP archive output stream
        OutputStream outputStream6 = new ByteArrayOutputStream();
        ArchiveOutputStream actualDumpOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream6);
        Assert.assertSame(DumpArchiveOutputStream.class, actualDumpOutputStream.getClass());
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        // Test creating a ZIP archive input stream from a known input stream
        InputStream inputStream = new ByteArrayInputStream(TEST_ZIP_SIGNATURE.getBytes());
        ArchiveInputStream actualZipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertSame(ZipArchiveInputStream.class, actualZipInputStream.getClass());

        // Test creating a non-existent archive input stream
        InputStream nonExistentInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualNonExistentInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", nonExistentInputStream);
        Assert.assertNull(actualNonExistentInputStream);

        // Test creating a JAR archive input stream from a known input stream
        InputStream expectedJarInputStream = new ByteArrayInputStream(TEST_JAR_SIGNATURE.getBytes());
        ArchiveInputStream actualJarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, expectedJarInputStream);
        Assert.assertSame(JarArchiveInputStream.class, actualJarInputStream.getClass());

        // Test creating an AR archive input stream from a known input stream
        InputStream expectedArInputStream = new ByteArrayInputStream(TEST_AR_SIGNATURE.getBytes());
        ArchiveInputStream actualArInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, expectedArInputStream);
        Assert.assertSame(ArArchiveInputStream.class, actualArInputStream.getClass());

        // Test creating a CPIO archive input stream from a known input stream
        InputStream expectedCpioInputStream = new ByteArrayInputStream(TEST_CPIO_SIGNATURE.getBytes());
        ArchiveInputStream actualCpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, expectedCpioInputStream);
        Assert.assertSame(CpioArchiveInputStream.class, actualCpioInputStream.getClass());

        // Test creating a TAR archive input stream from a known input stream
        InputStream expectedTarInputStream = new ByteArrayInputStream(TEST_TAR_SIGNATURE.getBytes());
        ArchiveInputStream actualTarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, expectedTarInputStream);
        Assert.assertSame(TarArchiveInputStream.class, actualTarInputStream.getClass());

        // Test creating a DUMP archive input stream from a known input stream
        InputStream expectedDumpInputStream = new ByteArrayInputStream(TEST_DUMP_SIGNATURE.getBytes());
        ArchiveInputStream actualDumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, expectedDumpInputStream);
        Assert.assertSame(DumpArchiveInputStream.class, actualDumpInputStream.getClass());
    }

    private static final byte[] TEST_ZIP_SIGNATURE = new byte[10]; // ZIP signature
    private static final byte[] TEST_JAR_SIGNATURE = new byte[8]; // JAR signature
    private static final byte[] TEST_AR_SIGNATURE = new byte[8]; // AR signature
    private static final byte[] TEST_CPIO_SIGNATURE = new byte[6]; // CPIO signature
    private static final byte[] TEST_TAR_SIGNATURE = new byte[8]; // TAR signature
    private static final byte[] TEST_DUMP_SIGNATURE = new byte[2]; // DUMP signature
}