import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutput;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioOutput = new CpioArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() {
        try {
            cpioOutput.close();
        } catch (IOException e) {
            fail("Failed to close CpioArchiveOutputStream");
        }
    }

    @Test
    public void testPutArchiveEntry_ValidEntry_Success() {
        try {
            File file = new File("test.txt");
            CpioArchiveEntry entry = new CpioArchiveEntry(file, "test.txt");
            cpioOutput.putArchiveEntry(entry);
            cpioOutput.closeArchiveEntry();
            byte[] result = outputStream.toByteArray();
            // Assert that the output stream contains the correct CPIO entry data
            // ...
        } catch (IOException e) {
            fail("Failed to put archive entry");
        }
    }

    @Test
    public void testPutArchiveEntry_ExistingEntry_ExceptionThrown() {
        try {
            File file = new File("test.txt");
            CpioArchiveEntry entry1 = new CpioArchiveEntry(file, "test.txt");
            CpioArchiveEntry entry2 = new CpioArchiveEntry(file, "test.txt");
            cpioOutput.putArchiveEntry(entry1);
            try {
                cpioOutput.putArchiveEntry(entry2);
                fail("Expected IOException to be thrown");
            } catch (IOException e) {
                // Expected exception
            }
        } catch (IOException e) {
            fail("Failed to put archive entry");
        }
    }

    @Test
    public void testPutArchiveEntry_FormatMismatch_ExceptionThrown() {
        try {
            File file = new File("test.txt");
            CpioArchiveEntry entry1 = new CpioArchiveEntry(file, "test.txt");
            CpioArchiveEntry entry2 = new CpioArchiveEntry(file, "test.txt", FORMAT_OLD_BINARY);
            cpioOutput.putArchiveEntry(entry1);
            try {
                cpioOutput.putArchiveEntry(entry2);
                fail("Expected IOException to be thrown");
            } catch (IOException e) {
                // Expected exception
            }
        } catch (IOException e) {
            fail("Failed to put archive entry");
        }
    }

    @Test
    public void testCloseArchiveEntry_InvalidEntrySize_ExceptionThrown() {
        try {
            File file = new File("test.txt");
            CpioArchiveEntry entry = new CpioArchiveEntry(file, "test.txt");
            entry.setSize(10);
            cpioOutput.putArchiveEntry(entry);
            try {
                cpioOutput.closeArchiveEntry();
                fail("Expected IOException to be thrown");
            } catch (IOException e) {
                // Expected exception
            }
        } catch (IOException e) {
            fail("Failed to put archive entry");
        }
    }

    @Test
    public void testFinish_UnclosedEntries_ExceptionThrown() {
        try {
            File file = new File("test.txt");
            CpioArchiveEntry entry = new CpioArchiveEntry(file, "test.txt");
            cpioOutput.putArchiveEntry(entry);
            try {
                cpioOutput.finish();
                fail("Expected IOException to be thrown");
            } catch (IOException e) {
                // Expected exception
            }
        } catch (IOException e) {
            fail("Failed to put archive entry");
        }
    }

}