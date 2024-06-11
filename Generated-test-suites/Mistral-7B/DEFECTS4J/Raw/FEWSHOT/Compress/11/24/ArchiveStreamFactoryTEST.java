package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test with null input
        try {
            ArchiveInputStream inputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
            assertNull(inputStream);
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with valid archives
        for (String archiverName : ArchiveStreamFactory.ARCHIVER_NAMES) {
            try {
                InputStream inputStream = new ByteArrayInputStream(getTestFileBytes(archiverName));
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
                assertInstanceOf(getArchiveClass(archiverName), archiveInputStream);
                archiveInputStream.close();
            } catch (IOException | ArchiveException e) {
                // Expected
            }
        }

        // Test with empty input
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
            assertNull(archiveInputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with null output
        try {
            ArchiveOutputStream outputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, null);
            assertNull(outputStream);
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with valid archives
        for (String archiverName : ArchiveStreamFactory.ARCHIVER_NAMES) {
            try {
                OutputStream outputStream = new ByteArrayOutputStream();
                ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
                assertInstanceOf(getArchiveOutputStreamClass(archiverName), archiveOutputStream);
                archiveOutputStream.finish();
                ArchiveInputStream inputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, new ByteArrayInputStream(outputStream.toByteArray()));
                assertInstanceOf(getArchiveClass(archiverName), inputStream.getNextEntry());
                inputStream.close();
            } catch (IOException | ArchiveException e) {
                // Expected
            }
        }

        // Test with empty output
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, outputStream);
            assertNull(archiveOutputStream);
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() {
        // Test with null input
        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
            assertNull(archiveInputStream);
        } catch (ArchiveException e) {
            // Expected
        }

        // Test with valid archives
        for (String archiverName : ArchiveStreamFactory.ARCHIVER_NAMES) {
            try {
                byte[] testData = getTestFileBytes(archiverName);
                ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, new ByteArrayInputStream(testData));
                assertInstanceOf(getArchiveClass(archiverName), archiveInputStream);

                // Test reading entries
                int entryCount = 0;
                ArchiveEntry archiveEntry;
                while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
                    entryCount++;
                }
                assertEquals(getExpectedEntryCount(archiverName), entryCount);

                // Test reading a specific entry
                archiveInputStream.seek(archiveInputStream.getFilePointer() - archiveInputStream.size());
                archiveEntry = archiveInputStream.getNextEntry();
                assertInstanceOf(getExpectedArchiveEntryClass(archiverName, 0), archiveEntry);

                archiveInputStream.close();
            } catch (IOException | ArchiveException e) {
                // Expected
            }
        }
    }

    private byte[] getTestFileBytes(String archiverName) {
        String testFilePath = String.format("src/test/resources/test-%s.%s", archiverName, archiverName.equalsIgnoreCase("tar") ? "gz" : "");
        try {
            return new ByteArrayOutputStream(2048).toByteArray(new FileInputStream(testFilePath).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test file: " + testFilePath, e);
        }
    }

    private int getExpectedEntryCount(String archiverName) {
        switch (archiverName) {
            case "ar":
                return 3;
            case "cpio":
                return 6;
            case "dump":
                return 2;
            case "jar":
                try {
                    ZipFile zipFile = new ZipFile("src/test/resources/test.jar");
                    return zipFile.size();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read test JAR file.", e);
                }
            case "tar":
                try {
                    TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream("src/test/resources/test.tar.gz"));
                    TarEntry entry = tarInputStream.getNextEntry();
                    int entryCount = 0;
                    while (entry != null) {
                        entryCount++;
                        entry = tarInputStream.getNextEntry();
                    }
                    return entryCount;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read test TAR file.", e);
                }
            default:
                throw new IllegalArgumentException("Unsupported archive format: " + archiverName);
        }
    }

    private Class<?> getArchiveClass(String archiverName) {
        switch (archiverName) {
            case "ar":
                return ArArchiveInputStream.class;
            case "cpio":
                return CpioArchiveInputStream.class;
            case "dump":
                return DumpArchiveInputStream.class;
            case "jar":
                return JarArchiveInputStream.class;
            case "tar":
                return TarArchiveInputStream.class;
            case "zip":
                return ZipArchiveInputStream.class;
            default:
                throw new IllegalArgumentException("Unsupported archive format: " + archiverName);
        }
    }

    private Class<? extends ArchiveOutputStream> getArchiveOutputStreamClass(String archiverName) {
        switch (archiverName) {
            case "ar":
                return ArArchiveOutputStream.class;
            case "cpio":
                return CpioArchiveOutputStream.class;
            case "dump":
                return DumpArchiveOutputStream.class;
            case "jar":
                return JarArchiveOutputStream.class;
            case "tar":
                return TarArchiveOutputStream.class;
            case "zip":
                return ZipArchiveOutputStream.class;
            default:
                throw new IllegalArgumentException("Unsupported archive format: " + archiverName);
        }
    }

    private Class<? extends ArchiveEntry> getExpectedArchiveEntryClass(String archiverName, int index) {
        switch (archiverName) {
            case "ar":
                switch (index) {
                    case 0:
                        return ArArchiveEntry.class;
                    case 1:
                        return ArArchiveEntry.class;
                    case 2:
                        return ArArchiveEntry.class;
                    default:
                        throw new IllegalArgumentException("Invalid entry index: " + index);
                }
            case "cpio":
                switch (index) {
                    case 0:
                        return CpioInternalArchiveEntry.class;
                    case 1:
                        return CpioCommonHeader.class;
                    case 2:
                        return CpioNameIdHeader.class;
                    case 3:
                        return CpioFormatHeader.class;
                    case 4:
                        return CpioUnicodeHeader.class;
                    case 5:
                        return CpioMagicNumber.class;
                    default:
                        throw new IllegalArgumentException("Invalid entry index: " + index);
                }
            case "dump":
                switch (index) {
                    case 0:
                        return DumpHeader.class;
                    case 1:
                        return DumpFileHeader.class;
                    default:
                        throw new IllegalArgumentException("Invalid entry index: " + index);
                }
            case "jar":
                return ZipEntry.class;
            case "tar":
                switch (index) {
                    case 0:
                        return TarHeader.class;
                    default:
                        throw new IllegalArgumentException("Invalid entry index: " + index);
                }
            case "zip":
                switch (index) {
                    case 0:
                        return ZipEntry.class;
                    default:
                        throw new IllegalArgumentException("Invalid entry index: " + index);
                }
            default:
                throw new IllegalArgumentException("Unsupported archive format: " + archiverName);
        }
    }
}