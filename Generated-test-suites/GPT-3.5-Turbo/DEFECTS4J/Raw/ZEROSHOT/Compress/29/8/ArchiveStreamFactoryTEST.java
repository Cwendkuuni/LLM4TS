import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(archiverName, in);
            assertNotNull(is);
            assertEquals(ZipArchiveInputStream.class, is.getClass());
        } catch (ArchiveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "tar";
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(archiverName, out);
            assertNotNull(os);
            assertEquals(TarArchiveOutputStream.class, os.getClass());
        } catch (ArchiveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertNotNull(is);
        } catch (ArchiveException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}