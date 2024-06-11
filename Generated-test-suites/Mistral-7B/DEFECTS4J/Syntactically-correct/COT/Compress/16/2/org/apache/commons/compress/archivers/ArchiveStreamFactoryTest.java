package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers. ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.InputArchiveEntry;
import org.apache.commons.compress.archivers.OutputArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;

import java.io.*;

public class ArchiveStreamFactoryTest {

    private byte[] testData = "Test data".getBytes();

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(testData);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        InputArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiverName() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(testData);
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AR_Signature() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(testData);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        byte[] signature = new byte[12];
        inputStream.mark(signature.length);
        int signatureLength = inputStream.read(signature);
        inputStream.reset();
        ArchiveInputStream expectedArchiveInputStream = new ArArchiveInputStream(inputStream);
        Assert.assertSame(expectedArchiveInputStream.getClass(), archiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(testData);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        InputArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        OutputArchiveEntry entry = new ArArchiveEntry("test.txt", "Test data".getBytes(), true);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.writeToArchive(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finishArchive();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        OutputArchiveEntry entry = new ZipArchiveEntry("test.txt", "Test data".getBytes(), 0);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.writeToArchive(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finishArchive();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertNotNull(result);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiverName() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = ArchiveStreamFactory.getInstance();
        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @After
    public void cleanup() throws Exception {
        IOUtils.closeQuietly(testDataInputStream);
        IOUtils.closeQuietly(testDataOutputStream);
    }
}