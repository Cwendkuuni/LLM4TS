import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.*;

public class ArchiveStreamFactoryTest {

    ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() throws Exception {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_validArchiverNameValidInputStream_success() throws Exception {
        String archiverName = "zip";
        InputStream in = new FileInputStream("test.zip");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);

        assertNotNull(archiveInputStream);
        // assert other expected behaviors
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName_throwException() throws Exception {
        String archiverName = null;
        InputStream in = new FileInputStream("test.zip");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream_throwException() throws Exception {
        String archiverName = "zip";
        InputStream in = null;
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_invalidArchiverName_throwException() throws Exception {
        String archiverName = "invalidArchiver";
        InputStream in = new FileInputStream("test.zip");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test
    public void testCreateArchiveOutputStream_validArchiverNameValidOutputStream_success() throws Exception {
        String archiverName = "zip";
        OutputStream out = new FileOutputStream("test.zip");
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);

        assertNotNull(archiveOutputStream);
        // assert other expected behaviors
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName_throwException() throws Exception {
        String archiverName = null;
        OutputStream out = new FileOutputStream("test.zip");
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream_throwException() throws Exception {
        String archiverName = "zip";
        OutputStream out = null;
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_invalidArchiverName_throwException() throws Exception {
        String archiverName = "invalidArchiver";
        OutputStream out = new FileOutputStream("test.zip");
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStream_validInputStream_success() throws Exception {
        InputStream in = new FileInputStream("test.zip");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);

        assertNotNull(archiveInputStream);
        // assert other expected behaviors
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream_throwException() throws Exception {
        InputStream in = null;
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_inputStreamDoesNotSupportMark_throwException() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_invalidArchive_throwException() throws Exception {
        InputStream in = new FileInputStream("test.txt");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }

    // Add more test cases for other methods if required

}