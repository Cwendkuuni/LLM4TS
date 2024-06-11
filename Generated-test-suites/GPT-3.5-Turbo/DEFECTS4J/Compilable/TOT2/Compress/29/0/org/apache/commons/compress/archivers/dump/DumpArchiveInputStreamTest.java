package org.apache.commons.compress.archivers.dump;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveException;

public class DumpArchiveInputStreamTest {

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        assertNotNull(dumpArchiveInputStream);
    }

    @Test
    public void testConstructorWithEncoding() throws IOException, ArchiveException {
        String encoding = "UTF-8";
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is, encoding);
        assertNotNull(dumpArchiveInputStream);
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        assertNotNull(dumpArchiveInputStream.getSummary());
    }

    @Test
    public void testRead() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        byte[] buf = new byte[10];
        int bytesRead = dumpArchiveInputStream.read(buf, 0, 10);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testGetNextDumpEntry() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        assertNull(dumpArchiveInputStream.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        assertNull(dumpArchiveInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        dumpArchiveInputStream.close();
        // No exceptions thrown means it closed successfully
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException, ArchiveException {
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
        dumpArchiveInputStream.close();
        byte[] buf = new byte[10];
        dumpArchiveInputStream.read(buf, 0, 10);
    }
}