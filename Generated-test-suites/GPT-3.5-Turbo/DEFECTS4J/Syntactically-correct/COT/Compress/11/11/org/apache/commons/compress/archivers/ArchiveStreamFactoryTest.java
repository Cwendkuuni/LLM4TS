
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP Archive
        String zipArchiver = ArchiveStreamFactory.ZIP;
        InputStream zipInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(zipArchiver, zipInput);
        Assert.assertNotNull(zipInputStream);
        
        // Test TAR Archive
        String tarArchiver = ArchiveStreamFactory.TAR;
        InputStream tarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(tarArchiver, tarInput);
        Assert.assertNotNull(tarInputStream);
        
        // Test AR Archive
        String arArchiver = ArchiveStreamFactory.AR;
        InputStream arInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(arArchiver, arInput);
        Assert.assertNotNull(arInputStream);
        
        // Test JAR Archive
        String jarArchiver = ArchiveStreamFactory.JAR;
        InputStream jarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(jarArchiver, jarInput);
        Assert.assertNotNull(jarInputStream);
        
        // Test CPIO Archive
        String cpioArchiver = ArchiveStreamFactory.CPIO;
        InputStream cpioInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(cpioArchiver, cpioInput);
        Assert.assertNotNull(cpioInputStream);
        
        // Test DUMP Archive
        String dumpArchiver = ArchiveStreamFactory.DUMP;
        InputStream dumpInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(dumpArchiver, dumpInput);
        Assert.assertNotNull(dumpInputStream);
        
        // Test unknown Archive
        String unknownArchiver = "unknown";
        InputStream unknownInput = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream(unknownArchiver, unknownInput);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test null archiverName
        try {
            ArchiveInputStream nullArchiverInputStream = factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test null in
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP Archive
        String zipArchiver = ArchiveStreamFactory.ZIP;
        OutputStream zipOutput = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(zipArchiver, zipOutput);
        Assert.assertNotNull(zipOutputStream);
        
        // Test TAR Archive
        String tarArchiver = ArchiveStreamFactory.TAR;
        OutputStream tarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(tarArchiver, tarOutput);
        Assert.assertNotNull(tarOutputStream);
        
        // Test AR Archive
        String arArchiver = ArchiveStreamFactory.AR;
        OutputStream arOutput = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(arArchiver, arOutput);
        Assert.assertNotNull(arOutputStream);
        
        // Test JAR Archive
        String jarArchiver = ArchiveStreamFactory.JAR;
        OutputStream jarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(jarArchiver, jarOutput);
        Assert.assertNotNull(jarOutputStream);
        
        // Test CPIO Archive
        String cpioArchiver = ArchiveStreamFactory.CPIO;
        OutputStream cpioOutput = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(cpioArchiver, cpioOutput);
        Assert.assertNotNull(cpioOutputStream);
        
        // Test unknown Archive
        String unknownArchiver = "unknown";
        OutputStream unknownOutput = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream unknownOutputStream = factory.createArchiveOutputStream(unknownArchiver, unknownOutput);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test null archiverName
        try {
            ArchiveOutputStream nullArchiverOutputStream = factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test null out
        try {
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
  
    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP Archive
        InputStream zipInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(zipInput);
        Assert.assertNotNull(zipInputStream);
        
        // Test TAR Archive
        InputStream tarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(tarInput);
        Assert.assertNotNull(tarInputStream);
        
        // Test AR Archive
        InputStream arInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(arInput);
        Assert.assertNotNull(arInputStream);
        
        // Test JAR Archive
        InputStream jarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(jarInput);
        Assert.assertNotNull(jarInputStream);
        
        // Test CPIO Archive
        InputStream cpioInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(cpioInput);
        Assert.assertNotNull(cpioInputStream);
        
        // Test DUMP Archive
        InputStream dumpInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(dumpInput);
        Assert.assertNotNull(dumpInputStream);
        
        // Test TAR Archive with a bigger buffer to check the signature
        byte[] tarheader = new byte[512];
        InputStream tarInputWithHeader = new ByteArrayInputStream(tarheader);
        ArchiveInputStream tarInputStreamWithHeader = factory.createArchiveInputStream(tarInputWithHeader);
        Assert.assertNotNull(tarInputStreamWithHeader);
        
        // Test unknown Archive
        InputStream unknownInput = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream(unknownInput);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test null in
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream((InputStream) null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
