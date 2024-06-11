
package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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


public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        String ar = ArchiveStreamFactory.AR;
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arAis = factory.createArchiveInputStream(ar, arInputStream);
        assertNotNull(arAis);
        assertTrue(arAis instanceof ArArchiveInputStream);

        // Test ZIP
        String zip = ArchiveStreamFactory.ZIP;
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipAis = factory.createArchiveInputStream(zip, zipInputStream);
        assertNotNull(zipAis);
        assertTrue(zipAis instanceof ZipArchiveInputStream);

        // Test TAR
        String tar = ArchiveStreamFactory.TAR;
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarAis = factory.createArchiveInputStream(tar, tarInputStream);
        assertNotNull(tarAis);
        assertTrue(tarAis instanceof TarArchiveInputStream);

        // Test JAR
        String jar = ArchiveStreamFactory.JAR;
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarAis = factory.createArchiveInputStream(jar, jarInputStream);
        assertNotNull(jarAis);
        assertTrue(jarAis instanceof JarArchiveInputStream);

        // Test CPIO
        String cpio = ArchiveStreamFactory.CPIO;
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioAis = factory.createArchiveInputStream(cpio, cpioInputStream);
        assertNotNull(cpioAis);
        assertTrue(cpioAis instanceof CpioArchiveInputStream);

        // Test DUMP
        String dump = ArchiveStreamFactory.DUMP;
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpAis = factory.createArchiveInputStream(dump, dumpInputStream);
        assertNotNull(dumpAis);
        assertTrue(dumpAis instanceof DumpArchiveInputStream);

        // Test unsupported format
        String unsupported = "unsupported";
        InputStream unsupportedInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(unsupported, unsupportedInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        String ar = ArchiveStreamFactory.AR;
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arAos = factory.createArchiveOutputStream(ar, arOutputStream);
        assertNotNull(arAos);
        assertTrue(arAos instanceof ArArchiveOutputStream);

        // Test ZIP
        String zip = ArchiveStreamFactory.ZIP;
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipAos = factory.createArchiveOutputStream(zip, zipOutputStream);
        assertNotNull(zipAos);
        assertTrue(zipAos instanceof ZipArchiveOutputStream);

        // Test TAR
        String tar = ArchiveStreamFactory.TAR;
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarAos = factory.createArchiveOutputStream(tar, tarOutputStream);
        assertNotNull(tarAos);
        assertTrue(tarAos instanceof TarArchiveOutputStream);

        // Test JAR
        String jar = ArchiveStreamFactory.JAR;
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarAos = factory.createArchiveOutputStream(jar, jarOutputStream);
        assertNotNull(jarAos);
        assertTrue(jarAos instanceof JarArchiveOutputStream);

        // Test CPIO
        String cpio = ArchiveStreamFactory.CPIO;
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioAos = factory.createArchiveOutputStream(cpio, cpioOutputStream);
        assertNotNull(cpioAos);
        assertTrue(cpioAos instanceof CpioArchiveOutputStream);

        // Test unsupported format
        String unsupported = "unsupported";
        OutputStream unsupportedOutputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream(unsupported, unsupportedOutputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipAis = factory.createArchiveInputStream(zipInputStream);
        assertNotNull(zipAis);
        assertTrue(zipAis instanceof ZipArchiveInputStream);

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarAis = factory.createArchiveInputStream(jarInputStream);
        assertNotNull(jarAis);
        assertTrue(jarAis instanceof JarArchiveInputStream);

        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arAis = factory.createArchiveInputStream(arInputStream);
        assertNotNull(arAis);
        assertTrue(arAis instanceof ArArchiveInputStream);

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioAis = factory.createArchiveInputStream(cpioInputStream);
        assertNotNull(cpioAis);
        assertTrue(cpioAis instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpAis = factory.createArchiveInputStream(dumpInputStream);
        assertNotNull(dumpAis);
        assertTrue(dumpAis instanceof DumpArchiveInputStream);

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarAis = factory.createArchiveInputStream(tarInputStream);
        assertNotNull(tarAis);
        assertTrue(tarAis instanceof TarArchiveInputStream);

        // Test unsupported format
        InputStream unsupportedInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(unsupportedInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }
}
