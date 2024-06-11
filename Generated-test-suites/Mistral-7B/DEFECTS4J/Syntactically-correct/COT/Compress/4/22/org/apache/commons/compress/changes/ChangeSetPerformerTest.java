import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetChange;
import org.apache.commons.compress.changes.ChangeSetPerformer;

/**
 * Test class for ChangeSetPerformer
 */
public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    private ArchiveInputStream input;
    private ArchiveOutputStream output;

    @Before
    public void setup() throws Exception {
        InputStream testStream = TestUtils.getResourceAsStream("test.tar.gz");
        input = new ArchiveInputStream(testStream);
        output = new ArchiveOutputStream("temp.tar.gz");

        Set<Change> changes = new HashSet<>();
        changes.add(createChange("test1.txt", "test1.txt", Change.TYPE_ADD, false));
        changes.add(createChange("test2.txt", "test2.txt", Change.TYPE_DELETE, false));
        changes.add(createChange("test3.txt", "test3.txt", Change.TYPE_ADD, true));

        changeSet = new ChangeSet(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws Exception {
        ChangeSetResults result = performer.perform(input, output);

        assertEquals(3, result.getAddedFromChangeSet().size());
        assertEquals(1, result.getDeleted().size());
    }

    @Test
    public void testPerform_NoChanges() throws Exception {
        Set<Change> changes = performer.getChanges();
        changes.clear();

        ChangeSetResults result = performer.perform(input, output);

        assertTrue(result.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws Exception {
        doThrow(IOException.class).when(output).putArchiveEntry(ArgumentMatchers.any(ArchiveEntry.class));

        performer.perform(input, output);
    }

    @Test
    public void testIsDeletedLater() {
        Set<Change> changes = new HashSet<>();
        changes.add(createChange("test1.txt", "test1.txt", Change.TYPE_ADD, false));
        changes.add(createChange("test1.txt", null, Change.TYPE_DELETE, false));

        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        assertTrue(performer.isDeletedLater(changes, createEntry("test1.txt")));
    }

    @Test
    public void testIsDeletedLater_NoMatch() {
        Set<Change> changes = new HashSet<>();
        changes.add(createChange("test1.txt", "test1.txt", Change.TYPE_ADD, false));
        changes.add(createChange("test2.txt", "test2.txt", Change.TYPE_DELETE, false));

        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changes));

        assertFalse(performer.isDeletedLater(changes, createEntry("test1.txt")));
    }

    @Test
    public void testCopyStream() throws Exception {
        InputStream input = new ByteArrayInputStream(Utils.toByteArray(new String("test").getBytes()));
        ArchiveOutputStream output = mock(ArchiveOutputStream.class);
        ArchiveEntry entry = mock(ArchiveEntry.class);

        performer.copyStream(input, output, entry);

        verify(output, times(1)).putArchiveEntry(entry);
        verify(output, times(1)).writeTo(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.anyLong());
        verify(output, times(1)).closeArchiveEntry();
    }

    private ArchiveEntry createEntry(String name) {
        return Mockito.mock(ArchiveEntry.class, Mockito.argThat(entry -> entry.getName().equals(name)));
    }

    private Change createChange(String from, String to, int type, boolean replaceMode) {
        return new ChangeSetChange(from, to, type, replaceMode);
    }
}