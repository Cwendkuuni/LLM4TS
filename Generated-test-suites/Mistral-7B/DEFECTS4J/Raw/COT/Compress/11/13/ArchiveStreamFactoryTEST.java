import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.ZipArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test createArchiveInputStream with known archive types
        InputStream input = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream expectedZip = new ZipArchiveInputStream(input);
        ArchiveInputStream actualZip = ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", input);
        assertEquals(expectedZip.getClass(), actualZip.getClass());

        input = new ByteArrayInputStream("Tar signature".getBytes());
        ArchiveInputStream expectedTar = new TarArchiveInputStream(input);
        ArchiveInputStream actualTar = ArchiveStreamFactory.getInstance().createArchiveInputStream("tar", input);
        assertEquals(expectedTar.getClass(), actualTar.getClass());

        input = new ByteArrayInputStream("Jar signature".getBytes());
        ArchiveInputStream expectedJar = new JarArchiveInputStream(input);
        ArchiveInputStream actualJar = ArchiveStreamFactory.getInstance().createArchiveInputStream("jar", input);
        assertEquals(expectedJar.getClass(), actualJar.getClass());

        input = new ByteArrayInputStream("Ar signature".getBytes());
        ArchiveInputStream expectedAr = new ArArchiveInputStream(input);
        ArchiveInputStream actualAr = ArchiveStreamFactory.getInstance().createArchiveInputStream("ar", input);
        assertEquals(expectedAr.getClass(), actualAr.getClass());

        input = new ByteArrayInputStream("Cpio signature".getBytes());
        ArchiveInputStream expectedCpio = new CpioArchiveInputStream(input);
        ArchiveInputStream actualCpio = ArchiveStreamFactory.getInstance().createArchiveInputStream("cpio", input);
        assertEquals(expectedCpio.getClass(), actualCpio.getClass());

        input = new ByteArrayInputStream("Dump signature".getBytes());
        ArchiveInputStream expectedDump = new DumpArchiveInputStream(input);
        ArchiveInputStream actualDump = ArchiveStreamFactory.getInstance().createArchiveInputStream("dump", input);
        assertEquals(expectedDump.getClass(), actualDump.getClass());

        // Test createArchiveInputStream with unsupported archive type
        input = new ByteArrayInputStream("Invalid signature".getBytes());
        ArchiveInputStream actualUnknown = ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", input);
        assertNull(actualUnknown);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test createArchiveOutputStream with known archive types
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedZip = new ZipArchiveOutputStream(output);
        ArchiveOutputStream actualZip = ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", output);
        assertEquals(expectedZip.getClass(), actualZip.getClass());

        output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedTar = new TarArchiveOutputStream(output);
        ArchiveOutputStream actualTar = ArchiveStreamFactory.getInstance().createArchiveOutputStream("tar", output);
        assertEquals(expectedTar.getClass(), actualTar.getClass());

        output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedJar = new JarArchiveOutputStream(output);
        ArchiveOutputStream actualJar = ArchiveStreamFactory.getInstance().createArchiveOutputStream("jar", output);
        assertEquals(expectedJar.getClass(), actualJar.getClass());

        output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedAr = new ArArchiveOutputStream(output);
        ArchiveOutputStream actualAr = ArchiveStreamFactory.getInstance().createArchiveOutputStream("ar", output);
        assertEquals(expectedAr.getClass(), actualAr.getClass());

        output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedCpio = new CpioArchiveOutputStream(output);
        ArchiveOutputStream actualCpio = ArchiveStreamFactory.getInstance().createArchiveOutputStream("cpio", output);
        assertEquals(expectedCpio.getClass(), actualCpio.getClass());

        // Test createArchiveOutputStream with null output
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromFirstBytes() throws Exception {
        // Test createArchiveInputStreamFromFirstBytes with known archive types
        InputStream input = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream actualZip = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("zip", actualZip.getName());

        input = new ByteArrayInputStream("Tar signature".getBytes());
        ArchiveInputStream actualTar = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("tar", actualTar.getName());

        input = new ByteArrayInputStream("Jar signature".getBytes());
        ArchiveInputStream actualJar = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("jar", actualJar.getName());

        input = new ByteArrayInputStream("Ar signature".getBytes());
        ArchiveInputStream actualAr = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("ar", actualAr.getName());

        input = new ByteArrayInputStream("Cpio signature".getBytes());
        ArchiveInputStream actualCpio = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("cpio", actualCpio.getName());

        input = new ByteArrayInputStream("Dump signature".getBytes());
        ArchiveInputStream actualDump = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertEquals("dump", actualDump.getName());

        input = new ByteArrayInputStream("Unknown signature".getBytes());
        ArchiveInputStream actualUnknown = ArchiveStreamFactory.getInstance().createArchiveInputStream(input);
        assertNull(actualUnknown);

        // Test createArchiveInputStreamFromFirstBytes with empty input
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actualEmpty = ArchiveStreamFactory.getInstance().createArchiveInputStream(emptyInput);
        assertNull(actualEmpty);
    }

    @Test
    public void testGetNextEntry() throws Exception {
        // Test getNextEntry for ZipArchiveInputStream
        InputStream zipInput = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream archiveInput = ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", zipInput);
        ZipArchiveInputStream zipArchiveInput = (ZipArchiveInputStream) archiveInput;
        ZipArchiveEntry zipEntry = zipArchiveInput.getNextEntry();
        assertNotNull(zipEntry);

        // Test getNextEntry for TarArchiveInputStream
        InputStream tarInput = new ByteArrayInputStream("Tar signature".getBytes());
        ArchiveInputStream archiveInputTar = ArchiveStreamFactory.getInstance().createArchiveInputStream("tar", tarInput);
        TarArchiveInputStream tarArchiveInput = (TarArchiveInputStream) archiveInputTar;
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarArchiveInput.getNextEntry();
        assertNotNull(tarEntry);
    }

    private static ArchiveStreamFactory getInstance() {
        return new ArchiveStreamFactory();
    }
}