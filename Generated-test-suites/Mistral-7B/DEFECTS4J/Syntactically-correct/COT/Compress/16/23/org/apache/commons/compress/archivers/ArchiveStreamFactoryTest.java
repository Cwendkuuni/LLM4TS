package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.test.TestUtils;
import org.apache.commons.compress.archivers.util.ArchiveEntryTest;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArguments_ReturnsExpectedInstance() throws Exception {
        for (ArchiveStreamFactory.ArchiveType archiveType : ArchiveStreamFactory.ArchiveType.values()) {
            InputStream inputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes(archiveType.getExtension()));
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveType.getExtension(), inputStream);
            assertEquals(archiveType, archiveInputStream.getFormat());
            inputStream.close();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiveType_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() throws Exception {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test
    public void testCreateArchiveInputStream_UnknownFormat_ThrowsArchiveException() throws Exception {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream("unknown", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArguments_ReturnsExpectedInstance() throws Exception {
        for (ArchiveStreamFactory.ArchiveType archiveType : ArchiveStreamFactory.ArchiveType.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveType.getExtension(), outputStream);
            assertEquals(archiveType, archiveOutputStream.getFormat());
            archiveOutputStream.finish();
            outputStream.close();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiveType_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() throws Exception {
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_WriteAndReadBack_MatchesInput() throws Exception {
        for (ArchiveStreamFactory.ArchiveType archiveType : ArchiveStreamFactory.ArchiveType.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveType.getExtension(), outputStream);
            ArchiveEntry testEntry = new ArchiveEntryTest("TestFile.txt");
            archiveOutputStream.putArchiveEntry(testEntry);
            IOUtils.copy(new FileInputStream(TestUtils.getTestFile("TestFile.txt")), archiveOutputStream);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveType.getExtension(), inputStream);
            ArchiveEntry readEntry = archiveInputStream.getNextEntry();
            ArchiveEntryTest expectedEntry = (ArchiveEntryTest) TestUtils.getTestFileEntry("TestFile.txt");
            assertEquals(expectedEntry, readEntry);
            inputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStream_Autodetection_ReturnsExpectedInstance() throws Exception {
        for (ArchiveStreamFactory.ArchiveType archiveType : ArchiveStreamFactory.ArchiveType.values()) {
            InputStream inputStream = new FileInputStream(TestUtils.getTestFile(archiveType.getExtension()));
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
            assertEquals(archiveType, archiveInputStream.getFormat());
            inputStream.close();
        }
    }
}