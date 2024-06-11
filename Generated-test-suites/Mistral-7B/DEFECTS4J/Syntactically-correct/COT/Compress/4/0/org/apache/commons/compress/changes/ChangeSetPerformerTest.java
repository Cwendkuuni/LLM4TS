package org.apache.commons.compress.changes;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    // Test data
    private static final ChangeSet changeSet;
    private static final ChangeSetExpected changeSetExpected;

    static {
        InputStream inputStream = ChangeSetPerformerTest.class.getResourceAsStream("/TestData.tar");
        ChangeSetParser parser = new ChangeSetParser();
        changeSet = parser.parse(inputStream);

        ChangeSet expectedChangeSet = new ChangeSet();
        expectedChangeSet.addChange(new Change(Change.TYPE_ADD, "test1.txt", "test1.txt", null, true));
        expectedChangeSet.addChange(new Change(Change.TYPE_DELETE, "test2.txt", null, null));
        expectedChangeSet.addChange(new Change(Change.TYPE_ADD, "test3.txt", "test3.txt", null, false));
        changeSetExpected = expectedChangeSet;
    }

    private ChangeSetPerformer performer;
    private ArchiveInputStream testInput;
    private ArchiveOutputStream testOutput;

    @Before
    public void setUp() throws IOException {
        performer = new ChangeSetPerformer(changeSet);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        testOutput = new ArchiveOutputStream("testOutput.tar.gz", new GzipCompressorOutputStream(outputStream));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(changeSet.toByteArray());
        testInput = new TarInputStream(inputStream);
    }

    @Test
    public void testPerform_Basic() throws IOException {
        ChangeSetResults results = performer.perform(testInput, testOutput);
        Set actual = results.getChanges();
        assertEquals(changeSetExpected.getChanges(), actual);
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        // Test IOException when stream is closed
        testInput.close();
        performer.perform(testInput, testOutput);
    }

    @Test(expected = IOException.class)
    public void testPerform_NullOutputStream() {
        // Test IOException when OutputStream is null
        performer.perform(testInput, null);
    }

    @Test
    public void testIsDeletedLater_Basic() {
        Set workingSet = new LinkedHashSet(changeSet.getChanges());
        ArchiveEntry entry = new TarEntry("test1.txt");

        assertTrue(performer.isDeletedLater(workingSet, entry));

        workingSet.add(new Change(Change.TYPE_DELETE, "test1.txt", null, null));
        assertFalse(performer.isDeletedLater(workingSet, entry));
    }

    @Test
    public void testIsDeletedLater_NullEntry() {
        // Test null argument
        assertFalse(performer.isDeletedLater(Collections.emptySet(), null));
    }

    @Test
    public void testCopyStream_Basic() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        performer.copyStream(input, testOutput, new TarEntry("test.txt"));

        byte[] expected = "Hello World\0".getBytes();
        byte[] actual = output.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyStream_InputStreamClosed() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        testOutput.close();
        performer.copyStream(input, testOutput, new TarEntry("test.txt"));

        // Test that the InputStream was not closed
        assertTrue(input.available() > 0);
    }

    @Test
    public void testCopyStream_NullArchiveEntry() {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());

        assertFalse(performer.copyStream(input, testOutput, null));
    }

    @Test
    public void testCopyStream_NullInputStream() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        assertFalse(performer.copyStream(null, testOutput, new TarEntry("test.txt")));
    }

    @Test
    public void testCopyStream_NullArchiveEntryAndInputStream() {
        assertFalse(performer.copyStream(null, null, null));
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(testOutput);
        IOUtils.closeQuietly(testInput);
    }
}