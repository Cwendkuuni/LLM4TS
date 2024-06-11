package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for the {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creation of ArchiveInputStreams for different archive types.
     */
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test with valid archive names
        ArchiveInputStream inZip = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inZip);
        assertTrue(inZip instanceof ZipArchiveInputStream);
        inZip.close();

        ArchiveInputStream inTar = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inTar);
        assertTrue(inTar instanceof TarArchiveInputStream);
        inTar.close();

        ArchiveInputStream inJar = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inJar);
        assertTrue(inJar instanceof JarArchiveInputStream);
        inJar.close();

        ArchiveInputStream inCpio = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inCpio);
        assertTrue(inCpio instanceof CpioArchiveInputStream);
        inCpio.close();

        ArchiveInputStream inAr = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inAr);
        assertTrue(inAr instanceof ArArchiveInputStream);
        inAr.close();

        ArchiveInputStream inDump = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(inDump);
        assertTrue(inDump instanceof DumpArchiveInputStream);
        inDump.close();

        ArchiveInputStream inUnknown = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        assertNull(inUnknown);

        // Test with null input
        ArchiveInputStream inNull = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(inNull);

        // Test with invalid archive name
        ArchiveInputStream inInvalid = factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
        assertNull(inInvalid);
    }

    /**
     * Test creation of ArchiveOutputStreams for different archive types.
     */
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test with valid archive names
        OutputStream outZip = new ByteArrayOutputStream();
        ArchiveOutputStream osZip = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outZip);
        assertNotNull(osZip);
        assertTrue(osZip instanceof ZipArchiveOutputStream);
        osZip.finish();

        OutputStream outTar = new ByteArrayOutputStream();
        ArchiveOutputStream osTar = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outTar);
        assertNotNull(osTar);
        assertTrue(osTar instanceof TarArchiveOutputStream);
        osTar.finish();

        OutputStream outJar = new ByteArrayOutputStream();
        ArchiveOutputStream osJar = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outJar);
        assertNotNull(osJar);
        assertTrue(osJar instanceof JarArchiveOutputStream);
        osJar.finish();

        OutputStream outCpio = new ByteArrayOutputStream();
        ArchiveOutputStream osCpio = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outCpio);
        assertNotNull(osCpio);
        assertTrue(osCpio instanceof CpioArchiveOutputStream);
        osCpio.finish();

        OutputStream outAr = new ByteArrayOutputStream();
        ArchiveOutputStream osAr = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outAr);
        assertNotNull(osAr);
        assertTrue(osAr instanceof ArArchiveOutputStream);
        osAr.finish();

        OutputStream outDump = new ByteArrayOutputStream();
        ArchiveOutputStream osDump = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, outDump);
        assertNotNull(osDump);
        assertTrue(osDump instanceof DumpArchiveOutputStream);
        osDump.finish();

        OutputStream outUnknown = new ByteArrayOutputStream();
        ArchiveOutputStream osUnknown = factory.createArchiveOutputStream("unknown", outUnknown);
        assertNull(osUnknown);

        // Test with null output
        ArchiveOutputStream osNull = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(osNull);

        // Test with invalid archive name
        ArchiveOutputStream osInvalid = factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
        assertNull(osInvalid);
    }

    /**
     * Test creation of ArchiveInputStreams from byte arrays.
     */
    @Test
    public void testCreateArchiveInputStreamFromByteArrays() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        byte[] zipData = new byte[10];
        ArchiveInputStream inZip = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipData));
        assertFalse(inZip.hasNextEntry());
        inZip.close();

        byte[] tarData = new byte[10];
        ArchiveInputStream inTar = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarData));
        assertFalse(inTar.hasNextEntry());
        inTar.close();

        byte[] jarData = new byte[10];
        ArchiveInputStream inJar = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarData));
        assertFalse(inJar.hasNextEntry());
        inJar.close();

        byte[] cpioData = new byte[10];
        ArchiveInputStream inCpio = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioData));
        assertFalse(inCpio.hasNextEntry());
        inCpio.close();

        byte[] arData = new byte[10];
        ArchiveInputStream inAr = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arData));
        assertFalse(inAr.hasNextEntry());
        inAr.close();

        byte[] dumpData = new byte[10];
        ArchiveInputStream inDump = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpData));
        assertFalse(inDump.hasNextEntry());
        inDump.close();

        ArchiveInputStream inInvalid = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNull(inInvalid);
    }
}