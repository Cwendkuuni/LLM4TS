import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        // Create initial ChangeSet for testing
        // Add some changes
        Change addChange = new Change(Change.TYPE_ADD, "file1.txt", false);
        File file1 = new File("src/test/resources/file1.txt");
        addChange.setInput(new FileInputStream(file1));
        addChange.setEntry(new ArchiveEntry("file1.txt"));
        changeSet.addChange(addChange);

        Change deleteChange = new Change(Change.TYPE_DELETE, "file2.txt", false);
        changeSet.addChange(deleteChange);

        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, "folder", false);
        changeSet.addChange(deleteDirChange);

        performer = new ChangeSetPerformer(changeSet);
    }

    @After
    public void tearDown() {
        performer = null;
    }

    @Test
    public void testPerform_AddReplaceModeEntry_StreamUpdated() throws IOException {
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        try {
            File inputFile = new File("src/test/resources/input.zip");
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            in = new ArchiveInputStream(fileInputStream);

            File outputFile = new File("src/test/resources/output.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            out = new ArchiveOutputStream(fileOutputStream);

            ChangeSetResults results = performer.perform(in, out);

            assertTrue(outputFile.exists());

            // Validate that the added entry from the changeSet was copied to the output stream
            assertTrue(results.hasBeenAdded("file1.txt"));

            // Validate that the entry with replace mode was copied to the output stream
            assertFalse(results.hasBeenAdded("file2.txt"));
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPerform_EntryDeleted_StreamUpdated() throws IOException {
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        try {
            File inputFile = new File("src/test/resources/input.zip");
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            in = new ArchiveInputStream(fileInputStream);

            File outputFile = new File("src/test/resources/output.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            out = new ArchiveOutputStream(fileOutputStream);

            ChangeSetResults results = performer.perform(in, out);

            assertTrue(outputFile.exists());

            // Validate that the deleted entry was not copied to the output stream
            assertFalse(results.hasBeenAdded("file2.txt"));

            // Validate that the entry not deleted was copied to the output stream
            assertTrue(results.hasBeenAdded("file3.txt"));
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPerform_DirectoryDeleted_StreamUpdated() throws IOException {
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        try {
            File inputFile = new File("src/test/resources/input.zip");
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            in = new ArchiveInputStream(fileInputStream);

            File outputFile = new File("src/test/resources/output.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            out = new ArchiveOutputStream(fileOutputStream);

            ChangeSetResults results = performer.perform(in, out);

            assertTrue(outputFile.exists());

            // Validate that the deleted directory was not copied to the output stream
            assertFalse(results.hasBeenAdded("folder/file3.txt"));

            // Validate that the entry in the directory not deleted was copied to the output stream
            assertTrue(results.hasBeenAdded("file4.txt"));
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Test
    public void testPerform_AddNoReplaceModeEntry_StreamUpdated() throws IOException {
        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        try {
            File inputFile = new File("src/test/resources/input.zip");
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            in = new ArchiveInputStream(fileInputStream);

            File outputFile = new File("src/test/resources/output.zip");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            out = new ArchiveOutputStream(fileOutputStream);

            ChangeSetResults results = performer.perform(in, out);

            assertTrue(outputFile.exists());

            // Validate that the entry from the changeSet with no replace mode was copied to the output stream
            assertTrue(results.hasBeenAdded("file5.txt"));
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}