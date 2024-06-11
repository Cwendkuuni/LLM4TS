package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException{
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creating an ArArchiveInputStream
        ByteArrayInputStream in1 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in1);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test creating a ZipArchiveInputStream
        ByteArrayInputStream in2 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in2);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test creating a TarArchiveInputStream
        ByteArrayInputStream in3 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in3);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);

        // Test creating a JarArchiveInputStream
        ByteArrayInputStream in4 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in4);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test creating a CpioArchiveInputStream
        ByteArrayInputStream in5 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in5);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test creating a DumpArchiveInputStream
        ByteArrayInputStream in6 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in6);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);

        // Test creating an unknown ArchiveInputStream
        ByteArrayInputStream in7 = new ByteArrayInputStream(new byte[100]);
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream("unknown", in7);
            fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Test creating an ArchiveInputStream with a null argument
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException{
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creating an ArArchiveOutputStream
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out1);
        assertTrue(arOutputStream instanceof ArArchiveOutputStream);

        // Test creating a ZipArchiveOutputStream
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out2);
        assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);

        // Test creating a TarArchiveOutputStream
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out3);
        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);

        // Test creating a JarArchiveOutputStream
        ByteArrayOutputStream out4 = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out4);
        assertTrue(jarOutputStream instanceof JarArchiveOutputStream);

        // Test creating a CpioArchiveOutputStream
        ByteArrayOutputStream out5 = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out5);
        assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);

        // Test creating an unknown ArchiveOutputStream
        ByteArrayOutputStream out6 = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream unknownOutputStream = factory.createArchiveOutputStream("unknown", out6);
            fail("ArchiveException expected");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Test creating an ArchiveOutputStream with a null argument
        try {
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() throws ArchiveException, IOException{
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creating a ZipArchiveInputStream
        ByteArrayInputStream in1 = new ByteArrayInputStream(new byte[] { 80, 75, 3, 4 });
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(in1);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test creating a JarArchiveInputStream
        ByteArrayInputStream in2 = new ByteArrayInputStream(new byte[] { 80, 75, 3, 4 });
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(in2);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test creating an ArArchiveInputStream
        ByteArrayInputStream in3 = new ByteArrayInputStream(new byte[] { 33, 60, 69, 62, 49, 65, 82, 1 });
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(in3);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test creating a CpioArchiveInputStream
        ByteArrayInputStream in4 = new ByteArrayInputStream(new byte[] { 1, 76, 87, 1 });
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(in4);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test creating a DumpArchiveInputStream
        ByteArrayInputStream in5 = new ByteArrayInputStream(new byte[] { 105, 115, 48, 49, 13, 10 });
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(in5);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);

        // Test creating a TarArchiveInputStream
        ByteArrayInputStream in6 = new ByteArrayInputStream(new byte[] { 31, -117, 8, 0, 26, 1, 36, 91, 65, 88 });
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(in6);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);

        // Test creating an ArchiveInputStream with an unsupported stream
        ByteArrayInputStream in7 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream unsupportedInputStream = factory.createArchiveInputStream(in7);
        assertNull(unsupportedInputStream);

        // Test creating an ArchiveInputStream with a null argument
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test creating an ArchiveInputStream with a non-mark supported stream
        ByteArrayInputStream in8 = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream nonMarkSupportedInputStream = factory.createArchiveInputStream(in8);
        assertNull(nonMarkSupportedInputStream);
    }
}