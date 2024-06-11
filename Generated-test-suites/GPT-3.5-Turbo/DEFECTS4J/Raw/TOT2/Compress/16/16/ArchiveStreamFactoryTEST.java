import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test for AR archive format
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception not expected");
        }

        // Add more test cases for other archive formats if needed
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test for ZIP archive format
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception not expected");
        }

        // Add more test cases for other archive formats if needed
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Test for autodetecting archive type
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (ArchiveException | IllegalArgumentException | IOException e) {
            fail("Exception not expected");
        }

        // Add edge cases and error scenarios for autodetect
    }
}