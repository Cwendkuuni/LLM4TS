import org.junit.Test;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_WithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveInputStream(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_WithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveInputStream("ar", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_WithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveOutputStream(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_WithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveOutputStream("ar", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_WithNullInputStream_MarkNotSupported() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.createArchiveInputStream(null);
    }

    @Test
    public void testCreateArchiveInputStream_WithValidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        ArchiveInputStream inputStream = factory.createArchiveInputStream("ar", new FileInputStream("test.ar"));

        assertNotNull(inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_WithValidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("ar", new FileOutputStream("test.ar"));

        assertNotNull(outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectZipArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.zip");
        byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x04};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectJarArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.jar");
        byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x04};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectArArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.ar");
        byte[] signature = new byte[]{0x21, 0x3C, 0x61, 0x72, 0x63, 0x68, 0x3E};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectCpioArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.cpio");
        byte[] signature = new byte[]{0x30, 0x37, 0x30, 0x37, 0x30, 0x30, 0x30, 0x30};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectDumpArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.dump");
        byte[] signature = new byte[]{0x30, 0x6D, 0x8F, 0x8F};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectTarArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.tar");
        byte[] signature = new byte[]{0x75, 0x73, 0x74, 0x61, 0x72, 0x00, 0x30, 0x30};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetectNoArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        File file = new File("test.noarchive");
        byte[] signature = new byte[]{0x00, 0x00, 0x00, 0x00};

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(signature);
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        }
    }
}