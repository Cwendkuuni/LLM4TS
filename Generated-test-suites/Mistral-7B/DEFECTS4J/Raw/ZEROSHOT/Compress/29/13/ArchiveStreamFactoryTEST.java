package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        // Test AR
        ArchiveInputStream in = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(in instanceof ArArchiveInputStream);
        in.close();

        // Test ARJ (not supported as an output stream type)
        try {
            ArchiveInputStream inArj = ArchiveStreamFactory.newInstance().createArchiveInputStream("arj", new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test CPIO
        ArchiveInputStream inCpio = ArchiveStreamFactory.newInstance().createArchiveInputStream("cpio", new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(inCpio instanceof CpioArchiveInputStream);
        inCpio.close();

        // Test Dump (not supported as an output stream type)
        try {
            ArchiveInputStream inDump = ArchiveStreamFactory.newInstance().createArchiveInputStream("dump", new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test Jar
        ArchiveInputStream inJar = ArchiveStreamFactory.newInstance().createArchiveInputStream("jar", new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(inJar instanceof JarArchiveInputStream);
        inJar.close();

        // Test Tar
        ArchiveInputStream inTar = ArchiveStreamFactory.newInstance().createArchiveInputStream("tar", new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(inTar instanceof TarArchiveInputStream);
        inTar.close();

        // Test Zip
        ArchiveInputStream inZip = ArchiveStreamFactory.newInstance().createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
        Assert.assertTrue(inZip instanceof ZipArchiveInputStream);
        inZip.close();

        // Test SevenZ (not supported)
        try {
            ArchiveInputStream inSevenZ = ArchiveStreamFactory.newInstance().createArchiveInputStream("7z", new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        // Test AR
        ArchiveOutputStream out = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", new ByteArrayOutputStream());
        Assert.assertTrue(out instanceof ArArchiveOutputStream);
        out.finish();
        out.close();

        // Test ARJ (not supported as an output stream type)
        try {
            ArchiveOutputStream outArj = ArchiveStreamFactory.newInstance().createArchiveOutputStream("arj", new ByteArrayOutputStream());
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test CPIO
        ArchiveOutputStream outCpio = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", new ByteArrayOutputStream());
        Assert.assertTrue(outCpio instanceof CpioArchiveOutputStream);
        outCpio.finish();
        outCpio.close();

        // Test Dump (not supported as an output stream type)
        try {
            ArchiveOutputStream outDump = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", new ByteArrayOutputStream());
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test Jar
        ArchiveOutputStream outJar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", new ByteArrayOutputStream());
        Assert.assertTrue(outJar instanceof JarArchiveOutputStream);
        outJar.finish();
        outJar.close();

        // Test Tar
        ArchiveOutputStream outTar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", new ByteArrayOutputStream());
        Assert.assertTrue(outTar instanceof TarArchiveOutputStream);
        outTar.finish();
        outTar.close();

        // Test Zip
        ArchiveOutputStream outZip = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", new ByteArrayOutputStream());
        Assert.assertTrue(outZip instanceof ZipArchiveOutputStream);
        outZip.finish();
        outZip.close();

        // Test SevenZ (not supported)
        try {
            ArchiveOutputStream outSevenZ = ArchiveStreamFactory.newInstance().createArchiveOutputStream("7z", new ByteArrayOutputStream());
            Assert.fail();
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test AR
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(in);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        // Test ARJ (not supported as an output stream type)
        try {
            ArchiveInputStream archiveInputStreamArj = ArchiveStreamFactory.newInstance().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test CPIO
        InputStream inCpio = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamCpio = ArchiveStreamFactory.newInstance().createArchiveInputStream(inCpio);
        Assert.assertTrue(archiveInputStreamCpio instanceof CpioArchiveInputStream);
        archiveInputStreamCpio.close();

        // Test Dump (not supported as an output stream type)
        try {
            ArchiveInputStream archiveInputStreamDump = ArchiveStreamFactory.newInstance().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test Jar
        InputStream inJar = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamJar = ArchiveStreamFactory.newInstance().createArchiveInputStream(inJar);
        Assert.assertTrue(archiveInputStreamJar instanceof JarArchiveInputStream);
        archiveInputStreamJar.close();

        // Test Tar
        InputStream inTar = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamTar = ArchiveStreamFactory.newInstance().createArchiveInputStream(inTar);
        Assert.assertTrue(archiveInputStreamTar instanceof TarArchiveInputStream);
        archiveInputStreamTar.close();

        // Test Zip
        InputStream inZip = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStreamZip = ArchiveStreamFactory.newInstance().createArchiveInputStream(inZip);
        Assert.assertTrue(archiveInputStreamZip instanceof ZipArchiveInputStream);
        archiveInputStreamZip.close();

        // Test SevenZ (not supported)
        try {
            ArchiveInputStream archiveInputStreamSevenZ = ArchiveStreamFactory.newInstance().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            Assert.fail();
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        // Test AR
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", out);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Test ARJ (not supported as an output stream type)
        try {
            ArchiveOutputStream archiveOutputStreamArj = ArchiveStreamFactory.newInstance().createArchiveOutputStream("arj", new ByteArrayOutputStream());
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test CPIO
        OutputStream outCpio = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamCpio = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", outCpio);
        Assert.assertTrue(archiveOutputStreamCpio instanceof CpioArchiveOutputStream);
        archiveOutputStreamCpio.finish();
        archiveOutputStreamCpio.close();

        // Test Dump (not supported as an output stream type)
        try {
            ArchiveOutputStream archiveOutputStreamDump = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", new ByteArrayOutputStream());
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test Jar
        OutputStream outJar = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamJar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", outJar);
        Assert.assertTrue(archiveOutputStreamJar instanceof JarArchiveOutputStream);
        archiveOutputStreamJar.finish();
        archiveOutputStreamJar.close();

        // Test Tar
        OutputStream outTar = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamTar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", outTar);
        Assert.assertTrue(archiveOutputStreamTar instanceof TarArchiveOutputStream);
        archiveOutputStreamTar.finish();
        archiveOutputStreamTar.close();

        // Test Zip
        OutputStream outZip = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStreamZip = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", outZip);
        Assert.assertTrue(archiveOutputStreamZip instanceof ZipArchiveOutputStream);
        archiveOutputStreamZip.finish();
        archiveOutputStreamZip.close();

        // Test SevenZ (not supported)
        try {
            ArchiveOutputStream archiveOutputStreamSevenZ = ArchiveStreamFactory.newInstance().createArchiveOutputStream("7z", new ByteArrayOutputStream());
            Assert.fail();
        } catch (StreamingNotSupportedException e) {
            // Expected
        }
    }

}