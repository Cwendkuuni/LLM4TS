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

    /**
     * Test createArchiveInputStream() with known archive types.
     * @throws Exception Exception.
     */
    @Test
    public void testCreateInputStream_knownTypes() throws Exception {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // AR
        final InputStream arInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.AR_DATA);
        final ArchiveInputStream arOutput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertTrue(arOutput instanceof ArArchiveInputStream);

        // ARJ
        final InputStream arjInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.ARJ_DATA);
        final ArchiveInputStream arjOutput = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
        Assert.assertTrue(arjOutput instanceof ArjArchiveInputStream);

        // CPIO
        final InputStream cpioInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.CPIO_DATA);
        final ArchiveInputStream cpioOutput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertTrue(cpioOutput instanceof CpioArchiveInputStream);

        // DUMP
        final InputStream dumpInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.DUMP_DATA);
        final ArchiveInputStream dumpOutput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertTrue(dumpOutput instanceof DumpArchiveInputStream);

        // JAR
        final InputStream jarInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.JAR_DATA);
        final ArchiveInputStream jarOutput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertTrue(jarOutput instanceof JarArchiveInputStream);

        // TAR
        final InputStream tarInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.TAR_DATA);
        final ArchiveInputStream tarOutput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertTrue(tarOutput instanceof TarArchiveInputStream);

        // ZIP
        final InputStream zipInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.ZIP_DATA);
        final ArchiveInputStream zipOutput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertTrue(zipOutput instanceof ZipArchiveInputStream);
    }

    /**
     * Test createArchiveOutputStream() with known archive types.
     * @throws Exception Exception.
     */
    @Test
    public void testCreateOutputStream_knownTypes() throws Exception {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // AR
        final OutputStream os = new ByteArrayOutputStream();
        final ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, os);
        arOutput.putNextEntry(new org.apache.commons.compress.archivers.ar.ARArchiveEntry("test.txt", "test data".getBytes()));
        arOutput.write( "test data".getBytes() );
        arOutput.closeArchiveEntry();
        arOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.AR_DATA, os.toByteArray());

        // ARJ
        final OutputStream arjOs = new ByteArrayOutputStream();
        final ArchiveOutputStream arjOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, arjOs);
        arjOutput.putNextEntry(new org.apache.commons.compress.archivers.arj.ArjArchiveEntry("test.txt", "test data".getBytes()));
        arjOutput.write( "test data".getBytes() );
        arjOutput.closeArchiveEntry();
        arjOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.ARJ_DATA, arjOs.toByteArray());

        // CPIO
        final OutputStream cpioOs = new ByteArrayOutputStream();
        final ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOs);
        cpioOutput.putNextEntry(new org.apache.commons.compress.archivers.cpio.CpioArchiveEntry("test.txt", "test data".getBytes()));
        cpioOutput.write( "test data".getBytes() );
        cpioOutput.closeArchiveEntry();
        cpioOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.CPIO_DATA, cpioOs.toByteArray());

        // DUMP
        final OutputStream dumpOs = new ByteArrayOutputStream();
        final ArchiveOutputStream dumpOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOs);
        dumpOutput.putNextEntry(new org.apache.commons.compress.archivers.dump.DumpArchiveEntry("test.txt", "test data".getBytes()));
        dumpOutput.write( "test data".getBytes() );
        dumpOutput.closeArchiveEntry();
        dumpOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.DUMP_DATA, dumpOs.toByteArray());

        // JAR
        final OutputStream jarOs = new ByteArrayOutputStream();
        final ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOs);
        jarOutput.putNextEntry(new org.apache.commons.compress.archivers.jar.JarArchiveEntry("test.txt", "test data".getBytes()));
        jarOutput.write( "test data".getBytes() );
        jarOutput.closeArchiveEntry();
        jarOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.JAR_DATA, jarOs.toByteArray());

        // TAR
        final OutputStream tarOs = new ByteArrayOutputStream();
        final ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOs);
        tarOutput.putNextEntry(new org.apache.commons.compress.archivers.tar.TarArchiveEntry("test.txt", "test data".getBytes()));
        tarOutput.write( "test data".getBytes() );
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.TAR_DATA, tarOs.toByteArray());

        // ZIP
        final OutputStream zipOs = new ByteArrayOutputStream();
        final ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOs);
        zipOutput.putNextEntry(new org.apache.commons.compress.archivers.zip.ZipArchiveEntry("test.txt", "test data".getBytes()));
        zipOutput.write( "test data".getBytes() );
        zipOutput.closeArchiveEntry();
        zipOutput.finish();
        Assert.assertEquals(ArchiveStreamFactoryTestData.ZIP_DATA, zipOs.toByteArray());
    }

    /**
     * Test createArchiveInputStream() with unknown archive types.
     * @throws Exception Exception.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateInputStream_unknownType() throws Exception {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();

        final InputStream unknownInputStream = new ByteArrayInputStream(ArchiveStreamFactoryTestData.SEVEN_Z_DATA);
        factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, unknownInputStream);
    }

    /**
     * Test createArchiveOutputStream() with unknown archive types.
     * @throws Exception Exception.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateOutputStream_unknownType() throws Exception {
        final ArchiveStreamFactory factory = new ArchiveStreamFactory();

        final OutputStream unknownOutputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, unknownOutputStream);
    }
}