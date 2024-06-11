import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@Category(org.apache.commons.compress.TestCategories.UNIT)
public class ChangeSetPerformerTest {
    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() throws IOException {
        changeSet = new ChangeSet();
        Change addChange1 = new Change(Change.TYPE_ADD, null, "add1.txt");
        Change addChange2 = new Change(Change.TYPE_ADD, null, "add2.txt");
        Change addChange3 = new Change(Change.TYPE_ADD, null, "add3.txt");
        Change deleteChange1 = new Change(Change.TYPE_DELETE, null, "delete1.txt");
        Change deleteChange2 = new Change(Change.TYPE_DELETE, null, "delete2.txt");
        Change deleteChange3 = new Change(Change.TYPE_DELETE, null, "delete3.txt");
        Change deleteDirChange1 = new Change(Change.TYPE_DELETE_DIR, null, "deleteDir1");

        addChange1.setReplaceMode(true);
        addChange2.setReplaceMode(true);
        deleteChange1.setReplaceMode(true);
        deleteChange2.setReplaceMode(true);
        deleteDirChange1.setReplaceMode(true);

        changeSet.addChange(addChange1);
        changeSet.addChange(addChange2);
        changeSet.addChange(addChange3);
        changeSet.addChange(deleteChange1);
        changeSet.addChange(deleteChange2);
        changeSet.addChange(deleteChange3);
        changeSet.addChange(deleteDirChange1);

        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_emptyInputs() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = new ArchiveOutputStream("memory:" + getClass().getName() + ":out.zip", new org.apache.commons.compress.archivers.zip.ZipFileOutputStreamFactory());
        ChangeSetResults result = performer.perform(new ArchiveInputStream(new ByteArrayInputStream(new byte[0])), archiveOut);

        assertEquals(0, result.size());
        assertFalse(archiveOut.hasNextEntry());
    }

    @Test
    public void testPerform_noChanges() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = new ArchiveOutputStream("memory:" + getClass().getName() + ":out.zip", new org.apache.commons.compress.archivers.zip.ZipFileOutputStreamFactory());
        ChangeSetResults result = performer.perform(new ArchiveInputStream(in), archiveOut);

        assertEquals(0, result.size());
        assertFalse(archiveOut.hasNextEntry());
    }

    @Test
    public void testPerform_addChanges() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = new ArchiveOutputStream("memory:" + getClass().getName() + ":out.zip", new org.apache.commons.compress.archivers.zip.ZipFileOutputStreamFactory());

        ChangeSetResults result = performer.perform(in, archiveOut);

        ByteArrayInputStream testOutputStream = new ByteArrayInputStream(result.getOutput().toByteArray());
        ArchiveInputStream testInputStream = new ArchiveInputStream(testOutputStream);

        ArchiveEntry testEntry1 = testInputStream.getNextEntry();
        assertEquals("add1.txt", testEntry1.getName());
        ArchiveEntry testEntry2 = testInputStream.getNextEntry();
        assertEquals("add2.txt", testEntry2.getName());
        ArchiveEntry testEntry3 = testInputStream.getNextEntry();
        assertEquals("add3.txt", testEntry3.getName());

        assertEquals(3, result.size());
    }

    @Test
    public void testPerform_deleteChanges() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input.zip").getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = new ArchiveOutputStream("memory:" + getClass().getName() + ":out.zip", new org.apache.commons.compress.archivers.zip.ZipFileOutputStreamFactory());

        ChangeSetResults result = performer.perform(in, archiveOut);

        ByteArrayInputStream testOutputStream = new ByteArrayInputStream(result.getOutput().toByteArray());
        ArchiveInputStream testInputStream = new ArchiveInputStream(testOutputStream);

        // Added files should not be present in the output
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(0, testInputStream.getNextEntryCount());

        // Deleted files should not be present in the input
        ArchiveEntry testEntry1 = in.getNextEntry();
        assertEquals("delete1.txt", testEntry1.getName());
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(1, testInputStream.getNextEntryCount());

        ArchiveEntry testEntry2 = testInputStream.getNextEntry();
        assertEquals("delete2.txt", testEntry2.getName());
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(2, testInputStream.getNextEntryCount());

        ArchiveEntry testEntry3 = testInputStream.getNextEntry();
        assertEquals("delete3.txt", testEntry3.getName());
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(3, testInputStream.getNextEntryCount());

        // Deleted directory should not be present in the input
        ArchiveEntry testEntry4 = testInputStream.getNextEntry();
        assertEquals("deleteDir1", testEntry4.getName());
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(1, testInputStream.getNextEntryCount());

        // Check that all entries have been deleted
        assertEquals(0, result.size());
    }

    @Test
    public void testPerform_addAndDeleteChanges() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(TestUtils.getTestFile("ChangeSetPerformerTest/testData/input.zip").getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = new ArchiveOutputStream("memory:" + getClass().getName() + ":out.zip", new org.apache.commons.compress.archivers.zip.ZipFileOutputStreamFactory());

        ChangeSetResults result = performer.perform(in, archiveOut);

        ByteArrayInputStream testOutputStream = new ByteArrayInputStream(result.getOutput().toByteArray());
        ArchiveInputStream testInputStream = new ArchiveInputStream(testOutputStream);

        // Added files should be present in the output
        ArchiveEntry testEntry1 = testInputStream.getNextEntry();
        assertEquals("add1.txt", testEntry1.getName());
        ArchiveEntry testEntry2 = testInputStream.getNextEntry();
        assertEquals("add2.txt", testEntry2.getName());
        ArchiveEntry testEntry3 = testInputStream.getNextEntry();
        assertEquals("add3.txt", testEntry3.getName());

        // Deleted files should not be present in the output
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(0, testInputStream.getNextEntryCount());

        // Deleted directory should not be present in the input
        ArchiveEntry testEntry4 = in.getNextEntry();
        assertEquals("deleteDir1", testEntry4.getName());
        assertFalse(testInputStream.getNextEntry() instanceof ArchiveEntry);
        assertEquals(1, testInputStream.getNextEntryCount());

        // Check that all entries have been added except the deleted ones
        assertEquals(3, result.size());
    }
}