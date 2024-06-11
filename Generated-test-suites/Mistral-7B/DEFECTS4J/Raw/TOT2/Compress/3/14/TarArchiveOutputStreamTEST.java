import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws Exception {
        String testFile = "testFile.txt";
        String testFileName = "testFileName.txt";
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"));
        tarArchiveEntry.setName(testFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Test data".getBytes(), 0, "Test data".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        TarArchiveOutputStream testTarArchiveOutputStream = new TarArchiveOutputStream(new FileInputStream(testFile));
        TarArchiveEntry testArchiveEntry = testTarArchiveOutputStream.getNextEntry();
        Assert.assertEquals(testFileName, testArchiveEntry.getName());
        byte[] testData = new byte[10];
        testArchiveOutputStream.read(testData, 0, testData.length);
        Assert.assertArrayEquals("Test data".getBytes(), testData);
        testArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNegativeSize() throws Exception {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream("testFile.txt"));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Test data".getBytes(), 0, -1);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }
}