package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.compressors.Compressors;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSet changeSet;
    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        InputStream inputStream = TestUtils.getResourceAsStream("/testfiles/test.zip");
        ArchiveInputStream archiveInputStream = new ArchiveInputStream(inputStream);
        Set<Change> changes = new HashSet<>();
        changes.add(new Change("test/Test1.txt", null, "test/Test1_Copy.txt", Change.TYPE_ADD, false));
        changes.add(new Change("test/Test2.txt", null, null, Change.TYPE_DELETE, false));
        changeSet = new ChangeSet(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_ExpectedAddOperation() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getResourceAsBytes("/testfiles/test.zip")), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasBeenAdded("Test1_Copy.txt"));
        assertEquals(1, output.getNumberOfEntries());
        ArchiveEntry entry = output.getNextEntry();
        assertNotNull(entry);
        assertEquals("Test1_Copy.txt", entry.getName());
    }

    @Test
    public void testPerform_ExpectedDeleteOperation() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getResourceAsBytes("/testfiles/test.zip")), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasBeenDeleted("Test2.txt"));
        assertEquals(0, output.getNumberOfEntries());
    }

    @Test(expected = IOException.class)
    public void testPerform_ExceptionWhenInputIsEmpty() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(new byte[0]), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        performer.perform(input, output);
    }

    @Test(expected = IOException.class)
    public void testPerform_ExceptionWhenOutputIsEmpty() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getResourceAsBytes("/testfiles/test.zip")), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream(0));

        performer.perform(input, output);
    }

    @Test
    public void testPerform_ExpectedAddOperationFromStream() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getResourceAsBytes("/testfiles/test.zip")), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasBeenAdded("Test1_Copy.txt"));
        assertEquals(1, output.getNumberOfEntries());
        ArchiveEntry entry = output.getNextEntry();
        assertNotNull(entry);
        assertEquals("Test1_Copy.txt", entry.getName());

        InputStream stream = output.getOutputStream(new ArchiveEntry("Test3.txt"));
        IOUtils.write("Test3Content".getBytes(), stream, input.getAvailableBytesRead());
        stream.close();

        ByteArrayOutputStream outStream = (ByteArrayOutputStream) output;
        InputStream resultStream = new ByteArrayInputStream(outStream.toByteArray());
        ArchiveInputStream resultArchive = new ArchiveInputStream(resultStream, Compressors.IDENTITY);

        ArchiveEntry test3Entry = resultArchive.getNextEntry();
        assertNotNull(test3Entry);
        assertEquals("Test3.txt", test3Entry.getName());
    }

    @Test
    public void testPerform_ExpectedDeleteLater() throws IOException {
        ArchiveInputStream input = new ArchiveInputStream(new ByteArrayInputStream(TestUtils.getResourceAsBytes("/testfiles/test_delete_later.zip")), Compressors.IDENTITY);
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.hasBeenDeleted("Test1.txt"));
        assertEquals(0, output.getNumberOfEntries());
    }
}