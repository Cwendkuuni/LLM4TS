import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            InputStream input = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
            assertTrue(archiveInput instanceof ArArchiveInputStream);
            input.close();
            
            input = new ByteArrayInputStream(new byte[10]);
            archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
            assertTrue(archiveInput instanceof ZipArchiveInputStream);
            input.close();
            
            input = new ByteArrayInputStream(new byte[10]);
            archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
            assertTrue(archiveInput instanceof TarArchiveInputStream);
            input.close();
            
            input = new ByteArrayInputStream(new byte[10]);
            archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
            assertTrue(archiveInput instanceof JarArchiveInputStream);
            input.close();
            
            input = new ByteArrayInputStream(new byte[10]);
            archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
            assertTrue(archiveInput instanceof CpioArchiveInputStream);
            input.close();
            
            input = new ByteArrayInputStream(new byte[10]);
            archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
            assertTrue(archiveInput instanceof DumpArchiveInputStream);
            input.close();
            
            try {
                input = new ByteArrayInputStream(new byte[10]);
                archiveInput = factory.createArchiveInputStream("unknown", input);
                fail("Expected ArchiveException to be thrown");
            } catch (ArchiveException e) {
                // expected exception
            }
            
            try {
                archiveInput = factory.createArchiveInputStream(null, input);
                fail("Expected IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
            
            try {
                archiveInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
                fail("Expected IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            OutputStream output = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
            assertTrue(archiveOutput instanceof ArArchiveOutputStream);
            output.close();
            
            output = new ByteArrayOutputStream();
            archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
            assertTrue(archiveOutput instanceof ZipArchiveOutputStream);
            output.close();

            output = new ByteArrayOutputStream();
            archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
            assertTrue(archiveOutput instanceof TarArchiveOutputStream);
            output.close();
            
            output = new ByteArrayOutputStream();
            archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
            assertTrue(archiveOutput instanceof JarArchiveOutputStream);
            output.close();
            
            output = new ByteArrayOutputStream();
            archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
            assertTrue(archiveOutput instanceof CpioArchiveOutputStream);
            output.close();
            
            try {
                output = new ByteArrayOutputStream();
                archiveOutput = factory.createArchiveOutputStream("unknown", output);
                fail("Expected ArchiveException to be thrown");
            } catch (ArchiveException e) {
                // expected exception
            }
            
            try {
                archiveOutput = factory.createArchiveOutputStream(null, output);
                fail("Expected IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
            
            try {
                archiveOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
                fail("Expected IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            
            byte[] zipSignature = new byte[] {80, 75, 3, 4};
            InputStream input = new ByteArrayInputStream(zipSignature);
            ArchiveInputStream archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof ZipArchiveInputStream);
            input.close();
            
            byte[] jarSignature = new byte[] {80, 75, 3, 4};
            input = new ByteArrayInputStream(jarSignature);
            archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof JarArchiveInputStream);
            input.close();
            
            byte[] arSignature = new byte[] {33, 60, 109, 97, 114, 107, 62};
            input = new ByteArrayInputStream(arSignature);
            archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof ArArchiveInputStream);
            input.close();
            
            byte[] cpioSignature = new byte[] {070, 005, 011, 020};
            input = new ByteArrayInputStream(cpioSignature);
            archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof CpioArchiveInputStream);
            input.close();
            
            byte[] dumpSignature = new byte[] {037, 223};
            input = new ByteArrayInputStream(dumpSignature);
            archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof DumpArchiveInputStream);
            input.close();
            
            byte[] tarSignature = new byte[512];
            input = new ByteArrayInputStream(tarSignature);
            archiveInput = factory.createArchiveInputStream(input);
            assertTrue(archiveInput instanceof TarArchiveInputStream);
            input.close();
            
            byte[] unknownSignature = new byte[] {1, 2, 3, 4};
            input = new ByteArrayInputStream(unknownSignature);
            try {
                archiveInput = factory.createArchiveInputStream(input);
                fail("Expected ArchiveException to be thrown");
            } catch (ArchiveException e) {
                // expected exception
            }
            input.close();
            
            try {
                archiveInput = factory.createArchiveInputStream(null);
                fail("Expected IllegalArgumentException to be thrown");
            } catch (IllegalArgumentException e) {
                // expected exception
            }
            
            input = new ByteArrayInputStream(new byte[10]);
            try {
                archiveInput = factory.createArchiveInputStream(input);
                fail("Expected ArchiveException to be thrown");
            } catch (ArchiveException e) {
                // expected exception
            }
            input.close();
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}