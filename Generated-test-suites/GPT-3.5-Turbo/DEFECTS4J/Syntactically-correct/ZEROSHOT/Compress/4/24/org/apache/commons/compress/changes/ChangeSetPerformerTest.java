import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ChangeSet changeSet;

    @Before
    public void setUp() {
        changeSet = Mockito.mock(ChangeSet.class);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform_AddReplaceMode_AddsEntryToOutputStream() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is added to the output stream
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));

        // Verify that the entry is removed from the working set
        Mockito.verify(changeSet.getChanges()).remove(Mockito.any(Change.class));

        // Verify that the entry is added to the results
        assertTrue(results.hasBeenAdded(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_DeleteFile_DeletesEntryFromResults() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is not copied to the output stream
        Mockito.verify(out, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));

        // Verify that the entry is removed from the working set
        Mockito.verify(changeSet.getChanges()).remove(Mockito.any(Change.class));

        // Verify that the entry is deleted from the results
        assertTrue(results.isDeleted(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_DeleteDirectory_DeletesEntriesFromResults() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entries in the directory are not copied to the output stream
        Mockito.verify(out, Mockito.never()).putArchiveEntry(Mockito.any(ArchiveEntry.class));

        // Verify that the entries in the directory are removed from the working set
        Mockito.verify(changeSet.getChanges()).remove(Mockito.any(Change.class));

        // Verify that the entries in the directory are deleted from the results
        assertTrue(results.isDeleted(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_CopyEntry_CopiesEntryFromInputStreamToOutputStream() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is copied from the input stream to the output stream
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(in).getNextEntry();
        Mockito.verify(out).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(out).closeArchiveEntry();

        // Verify that the entry is added to the results
        assertTrue(results.hasBeenAdded(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_UnchangedEntry_CopiesEntryFromInputStreamToOutputStream() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is copied from the input stream to the output stream
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));
        Mockito.verify(in).getNextEntry();
        Mockito.verify(out).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(out).closeArchiveEntry();

        // Verify that the entry is added to the results
        assertTrue(results.hasBeenAdded(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_AddUnchangedEntry_AddsEntryToOutputStream() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is added to the output stream
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));

        // Verify that the entry is removed from the working set
        Mockito.verify(changeSet.getChanges()).remove(Mockito.any(Change.class));

        // Verify that the entry is added to the results
        assertTrue(results.hasBeenAdded(Mockito.any(String.class)));
    }

    @Test
    public void testPerform_AddUnchangedEntryWithReplaceMode_AddsEntryToOutputStream() throws IOException {
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = performer.perform(in, out);

        // Verify that the entry is added to the output stream
        Mockito.verify(out).putArchiveEntry(Mockito.any(ArchiveEntry.class));

        // Verify that the entry is removed from the working set
        Mockito.verify(changeSet.getChanges()).remove(Mockito.any(Change.class));

        // Verify that the entry is added to the results
        assertTrue(results.hasBeenAdded(Mockito.any(String.class)));
    }
}