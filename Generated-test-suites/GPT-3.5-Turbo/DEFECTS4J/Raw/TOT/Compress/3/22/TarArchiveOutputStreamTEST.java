import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        // Test constructor with default block size and record size
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new OutputStreamStub());
        assertNotNull(tarOutput);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testConstructor2() {
        // Test constructor with custom block size
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new OutputStreamStub(), 1024);
        assertNotNull(tarOutput);
        assertEquals(1024, tarOutput.getRecordSize());
    }

    @Test
    public void testConstructor3() {
        // Test constructor with custom block size and record size
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new OutputStreamStub(), 1024, 512);
        assertNotNull(tarOutput);
        assertEquals(1024, tarOutput.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new OutputStreamStub());
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStreamStub outputStream = new OutputStreamStub();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Test finish method
        tarOutput.finish();
        assertEquals(512, outputStream.getWrittenBytes().length);
    }

    @Test
    public void testClose() throws IOException {
        OutputStreamStub outputStream = new OutputStreamStub();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Test close method
        tarOutput.close();
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStreamStub outputStream = new OutputStreamStub();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Test putArchiveEntry method
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(1000L);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        assertEquals(1024, outputStream.getWrittenBytes().length);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_incompleteWrite() throws IOException {
        OutputStreamStub outputStream = new OutputStreamStub();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Test closeArchiveEntry method with incomplete write
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(1000L);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[512], 0, 256);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStreamStub outputStream = new OutputStreamStub();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Test write method
        tarOutput.write(new byte[100]);
        tarOutput.write(new byte[200], 0, 150);
        assertEquals(350, outputStream.getWrittenBytes().length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new OutputStreamStub());
        
        // Test createArchiveEntry method
        File inputFile = new File("testFile.txt");
        String entryName = "file.txt";
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(inputFile.getName(), entry.getName());
        assertEquals(entryName, entry.getName());
    }

    // Custom OutputStream stub for testing
    private class OutputStreamStub extends OutputStream {
        private byte[] writtenBytes;
        private boolean closed;

        public OutputStreamStub() {
            writtenBytes = new byte[0];
            closed = false;
        }

        @Override
        public void write(int b) throws IOException {
            byte[] newBytes = new byte[writtenBytes.length + 1];
            System.arraycopy(writtenBytes, 0, newBytes, 0, writtenBytes.length);
            newBytes[writtenBytes.length] = (byte) b;
            writtenBytes = newBytes;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public byte[] getWrittenBytes() {
            return writtenBytes;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}