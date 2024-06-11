package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception not expected here: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Exception not expected here: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(null, in);
            fail("Exception expected here");
        } catch (IllegalArgumentException e) {
            // expecting IllegalArgumentException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            fail("Exception expected here");
        } catch (IllegalArgumentException e) {
            // expecting IllegalArgumentException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAutoDetectionWithNullInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null);
            fail("Exception expected here");
        } catch (IllegalArgumentException e) {
            // expecting IllegalArgumentException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAutoDetectionWithUnsupportedMark() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new InputStream() {
                @Override
                public int read() {
                    return 0;
                }
            };
            factory.createArchiveInputStream(in);
            fail("Exception expected here");
        } catch (IllegalArgumentException e) {
            // expecting IllegalArgumentException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAutoDetectionWithUnknownArchive() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(in);
            fail("Exception expected here");
        } catch (ArchiveException e) {
            // expecting ArchiveException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAutoDetectionWithUnsupportedResetAndMarkOperations() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]) {
                @Override
                public void mark(int readlimit) {
                    // do nothing
                    // override to do nothing, which indicates the lack of support
                }
            };
            factory.createArchiveInputStream(in);
            fail("Exception expected here");
        } catch (ArchiveException e) {
            // expecting ArchiveException
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}