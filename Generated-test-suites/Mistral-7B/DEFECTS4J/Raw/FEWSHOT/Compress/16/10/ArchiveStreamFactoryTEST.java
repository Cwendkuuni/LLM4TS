package org.apache.commons.cli.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInfo;
import org.apache.commons.compress.archivers.zip.ZipCentralDirectoryFile;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            InputStream inputStream = getSampleFileInputStream(archiverName);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertInstanceOf(ArchiveInputStream.class, archiveInputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR)) {
                assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR)) {
                assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO)) {
                assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP)) {
                assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR)) {
                assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP)) {
                assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
            }
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            byte[] bytes = getSampleFileBytes(archiverName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertInstanceOf(ArchiveInputStream.class, archiveInputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR)) {
                assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR)) {
                assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO)) {
                assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP)) {
                assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR)) {
                assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP)) {
                assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
            }
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertInstanceOf(ArchiveOutputStream.class, archiveOutputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR)) {
                assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR)) {
                assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO)) {
                assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP)) {
                assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR)) {
                assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP)) {
                assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
            }
            archiveOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromInputStream() throws IOException {
        String[] archiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : archiverNames) {
            InputStream inputStream = getSampleFileInputStream(archiverName);
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertInstanceOf(ArchiveOutputStream.class, archiveOutputStream);
            if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR)) {
                assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR)) {
                assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO)) {
                assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP)) {
                assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR)) {
                assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
            } else if (archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP)) {
                assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
            }

            ZipArchiveOutputStream zipArchiveOutputStream = (ZipArchiveOutputStream) archiveOutputStream;
            ZipEntry zipEntry = new ZipEntry("test.txt");
            zipArchiveOutputStream.putNextEntry(zipEntry);
            byte[] testBytes = "Test contents".getBytes();
            zipArchiveOutputStream.write(testBytes, 0, testBytes.length);
            zipArchiveOutputStream.closeEntry();
            archiveOutputStream.close();

            InputStream archiveInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, archiveInputStream);
            ZipCentralDirectoryFile centralDirectoryFile = jarArchiveInputStream.getCentralDirectoryFile();
            assertNull(centralDirectoryFile.getComment());
            ZipEntry zipEntry1 = jarArchiveInputStream.getNextEntry();
            assertInstanceOf(ZipEntry.class, zipEntry1);
            assertInstanceOf(ZipEntry.class, jarArchiveInputStream.getNextEntry());
            jarArchiveInputStream.close();
            archiveInputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithExtendedInfo() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        String fileName = "test.txt";
        String comment = "Test comment";
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        ZipArchiveOutputStream zipArchiveOutputStream = (ZipArchiveOutputStream) archiveOutputStream;
        ZipEntry zipEntry = new ZipEntry(fileName);
        Zip64ExtendedInfo extendedInfo = new Zip64ExtendedInfo();
        extendedInfo.setComment(comment);
        zipEntry.setExtendedInfo(extendedInfo);
        zipArchiveOutputStream.putNextEntry(zipEntry);
        byte[] testBytes = "Test contents".getBytes();
        zipArchiveOutputStream.write(testBytes, 0, testBytes.length);
        zipArchiveOutputStream.closeEntry();
        archiveOutputStream.close();

        InputStream archiveInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, archiveInputStream);
        ZipCentralDirectoryFile centralDirectoryFile = jarArchiveInputStream.getCentralDirectoryFile();
        ZipEntry zipEntry1 = centralDirectoryFile.getEntry(fileName);
        assertInstanceOf(ZipEntry.class, zipEntry1);
        assertEquals(comment, zipEntry1.getComment());
        jarArchiveInputStream.close();
        archiveInputStream.close();
    }

    private InputStream getSampleFileInputStream(String archiverName) throws IOException {
        InputStream inputStream;
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                inputStream = ArchiveStreamFactory.class.getResourceAsStream("/test-data/ar/test.ar");
                break;
            case ArchiveStreamFactory.CPIO:
                inputStream = ArchiveStreamFactory.class.getResourceAsStream("/test-data/cpio/test.cpio");
                break;
            case ArchiveStreamFactory.DUMP:
                inputStream = ArchiveStreamFactory.class.getResourceAsStream("/test-data/dump/test.dump");
                break;
            case ArchiveStreamFactory.JAR:
                inputStream = new FileInputStream(new File("test-data/test.jar"));
                break;
            case ArchiveStreamFactory.TAR:
                inputStream = ArchiveStreamFactory.class.getResourceAsStream("/test-data/tar/test.tar");
                break;
            case ArchiveStreamFactory.ZIP:
                ZipFile zipFile = new ZipFile("test-data/test.zip");
                inputStream = zipFile.getInputStream(zipFile.getEntry("test.txt"));
                break;
            default:
                throw new RuntimeException("Unsupported archiver: " + archiverName);
        }
        return inputStream;
    }

    private byte[] getSampleFileBytes(String archiverName) throws IOException {
        InputStream inputStream = getSampleFileInputStream(archiverName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = inputStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, length);
        }
        byteArrayOutputStream.close();
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}