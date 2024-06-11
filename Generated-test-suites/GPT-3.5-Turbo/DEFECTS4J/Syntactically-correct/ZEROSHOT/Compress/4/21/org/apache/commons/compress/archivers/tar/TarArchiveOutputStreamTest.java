import org.junit.Assert;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

@Test
public void testConstructor1() {
    OutputStream os = null;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
    Assert.assertNotNull(tarArchiveOutputStream);
}

@Test
public void testConstructor2() {
    OutputStream os = null;
    int blockSize = 0;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, blockSize);
    Assert.assertNotNull(tarArchiveOutputStream);
}

@Test
public void testConstructor3() {
    OutputStream os = null;
    int blockSize = 0;
    int recordSize = 0;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, blockSize, recordSize);
    Assert.assertNotNull(tarArchiveOutputStream);
}

@Test
public void testSetLongFileMode() {
    int longFileMode = 0;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    tarArchiveOutputStream.setLongFileMode(longFileMode);
    // No assertion for void method
}

@Test
public void testFinish() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.finish();
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testClose() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.close();
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testGetRecordSize() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    int recordSize = tarArchiveOutputStream.getRecordSize();
    Assert.assertEquals(0, recordSize);
}

@Test
public void testPutArchiveEntry() {
    ArchiveEntry archiveEntry = new TarArchiveEntry(null, null);
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    } catch (ClassCastException e) {
        Assert.fail("ClassCastException thrown");
    }
}

@Test
public void testCloseArchiveEntry() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.closeArchiveEntry();
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testWrite() {
    byte[] wBuf = new byte[10];
    int wOffset = 0;
    int numToWrite = 10;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testWriteEOFRecord() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.writeEOFRecord();
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testFlush() {
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        tarArchiveOutputStream.flush();
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

@Test
public void testCreateArchiveEntry() {
    File inputFile = null;
    String entryName = null;
    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    try {
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        Assert.assertNotNull(archiveEntry);
    } catch (IOException e) {
        Assert.fail("IOException thrown");
    }
}

}