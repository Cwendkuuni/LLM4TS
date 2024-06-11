import org.junit.Test;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Test AR
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(arInputStream);
        assertTrue(arInputStream instanceof ArArchiveInputStream);
        try {
            arInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test ZIP
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(zipInputStream);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        try {
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test TAR
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(tarInputStream);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);
        try {
            tarInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test JAR
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(jarInputStream);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);
        try {
            jarInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test CPIO
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(cpioInputStream);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        try {
            cpioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test DUMP
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(dumpInputStream);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
        try {
            dumpInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test unknown format
        try {
            factory.createArchiveInputStream("unknown", in);
            fail("ArchiveException not thrown for unknown format");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }

        // Test null archiverName
        try {
            factory.createArchiveInputStream(null, in);
            fail("IllegalArgumentException not thrown for null archiverName");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }

        // Test null inputStream
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException not thrown for null inputStream");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();

        // Test AR
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(arOutputStream);
        assertTrue(arOutputStream instanceof ArArchiveOutputStream);
        try {
            arOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test ZIP
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(zipOutputStream);
        assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test TAR
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(tarOutputStream);
        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test JAR
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(jarOutputStream);
        assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
        try {
            jarOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test CPIO
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(cpioOutputStream);
        assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test unknown format
        try {
            factory.createArchiveOutputStream("unknown", out);
            fail("ArchiveException not thrown for unknown format");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }

        // Test null archiverName
        try {
            factory.createArchiveOutputStream(null, out);
            fail("IllegalArgumentException not thrown for null archiverName");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }

        // Test null outputStream
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException not thrown for null outputStream");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Test ZIP
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(in);
        assertNotNull(zipInputStream);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        try {
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test JAR
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(in);
        assertNotNull(jarInputStream);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);
        try {
            jarInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test AR
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(in);
        assertNotNull(arInputStream);
        assertTrue(arInputStream instanceof ArArchiveInputStream);
        try {
            arInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test CPIO
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(in);
        assertNotNull(cpioInputStream);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        try {
            cpioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test DUMP
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(in);
        assertNotNull(dumpInputStream);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
        try {
            dumpInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test TAR
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(in);
        assertNotNull(tarInputStream);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);
        try {
            tarInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test unsupported format
        try {
            InputStream unsupportedIn = new ByteArrayInputStream(new byte[0]) {
                @Override
                public boolean markSupported() {
                    return false;
                }
            };
            factory.createArchiveInputStream(unsupportedIn);
            fail("IllegalArgumentException not thrown for unsupported format");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }

        // Test unknown format
        try {
            InputStream unknownIn = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(unknownIn);
            fail("ArchiveException not thrown for unknown format");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}