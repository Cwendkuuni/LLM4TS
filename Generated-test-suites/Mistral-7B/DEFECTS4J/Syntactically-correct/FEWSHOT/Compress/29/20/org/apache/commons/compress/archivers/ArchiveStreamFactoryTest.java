package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test valid archives
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("ZIP", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream("PK\003\012\005test\002.\r\n\000\012.\r\n\000\015test\002\000\012.\r\n\000\014test\002\000\015test\001\000\000\020test\001\000\000\012.\r\n".getBytes());
        archiveInputStream = factory.createArchiveInputStream("ZIP", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new String("__ARCHIVE_MAGIC__\002\002\010test\002.\r\n\000\012.\r\n\000\006test\002\000\012.\r\n\000\014test\002\000\015test\001\000\000\020test\001\000\000\012.\r\n"
                + "\005test\001\000\000\032test\001\000\000\012.\r\n"
                + "\007test\001\000\000\020test\001\000\000\012.\r\n"
                + "\005test\001\000\000\020test\001\000\000\012.\r\n"
                + "\010test\001\000\000\020test\001\000\000\012.\r\n"
                + "\010test\001\000\000\012.\r\n"
                + "\000\012.\r\n").getBytes());
        archiveInputStream = factory.createArchiveInputStream("JAR", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new byte[32]); // Dump signature
        archiveInputStream = factory.createArchiveInputStream("DUMP", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new byte[7]); // Tar header
        archiveInputStream = factory.createArchiveInputStream("TAR", input);
        assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveInputStream("TAR", input));
        IOUtils.closeQuietly(archiveInputStream);

        // Test invalid archives
        input = new ByteArrayInputStream("invalid".getBytes());
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[1]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[10]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[12]); // Zip signature
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("ZIP", input));
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveInputStreamWithEntryEncoding() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");

        // Test valid archives
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("ZIP", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = factory.createArchiveInputStream("JAR", input, "UTF-8");
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream("PK\003\012\005test\002.\r\n\000\012.\r\n\000\015test\002\000\012.\r\n\000\014test\002\000\015test\001\000\000\020test\001\000\000\012.\r\n".getBytes());
        archiveInputStream = factory.createArchiveInputStream("ZIP", input);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new String("__ARCHIVE_MAGIC__\002\002\010test\002.\r\n\000\012.\r\n\000\006test\002\000\012.\r\n\000\014test\002\000\015test\001\000\000\020test\001\000\000\012.\r\n"
                + "\005test\001\000\000\032test\001\000\000\012.\r\n"
                + "\007test\001\000\000\020test\001\000\000\012.\r\n"
                + "\005test\001\000\000\020test\001\000\000\012.\r\n"
                + "\010test\001\000\000\020test\001\000\000\012.\r\n"
                + "\010test\001\000\000\012.\r\n"
                + "\000\012.\r\n").getBytes());
        archiveInputStream = factory.createArchiveInputStream("JAR", input, "UTF-8");
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new byte[32]); // Dump signature
        archiveInputStream = factory.createArchiveInputStream("DUMP", input, "UTF-8");
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);

        input = new ByteArrayInputStream(new byte[7]); // Tar header
        assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveInputStream("TAR", input));
        IOUtils.closeQuietly(input);

        // Test invalid archives
        input = new ByteArrayInputStream("invalid".getBytes());
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[1]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[10]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", input));
        IOUtils.closeQuietly(input);

        input = new ByteArrayInputStream(new byte[12]); // Zip signature
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("ZIP", input, "UTF-8"));
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test valid archives
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("ZIP", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("JAR", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("TAR", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("CPIO", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("AR", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        // Test invalid archives
        output = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("INVALID", output));
        output.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithEntryEncoding() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");

        // Test valid archives
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("ZIP", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("JAR", output, "UTF-8");
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("TAR", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("CPIO", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();

        output = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream("AR", output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

}