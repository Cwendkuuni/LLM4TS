import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private Set<Change> changes;
    private ArchiveInputStream inputMock;
    private ArchiveOutputStream outputMock;

    @Before
    public void setUp() {
        changes = new LinkedHashSet<>();

        Change addChange = new Change("file1.txt", Change.TYPE_ADD, null, null, null, Change.REPLACE_MODE_OFF);
        Change addChange2 = new Change("file2.txt", Change.TYPE_ADD, null, null, null, Change.REPLACE_MODE_OFF);
        Change deleteChange = new Change("file1.txt", Change.TYPE_DELETE, null, null, null, false);
        Change deleteDirChange = new Change("dir1/", Change.TYPE_DELETE_DIR, null, null, null, false);

        changes.add(addChange);
        changes.add(addChange2);
        changes.add(deleteChange);
        changes.add(deleteDirChange);

        performer = new ChangeSetPerformer(new ChangeSet(changes));
        inputMock = Mockito.mock(ArchiveInputStream.class);
        outputMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testPerform_AddAndDeleteExistingFile() throws IOException {
        // given
        ArchiveEntry addEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock.getName()).thenReturn("file1.txt");
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(deleteEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        ChangeSetResults results = performer.perform(inputMock, outputMock);

        // then
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
        assertTrue(results.hasBeenAdded("file2.txt"));
        assertTrue(results.hasBeenAdded("file1.txt"));
    }

    @Test
    public void testPerform_AddAndDeleteNonExistingFile() throws IOException {
        // given
        Mockito.when(inputMock.getNextEntry()).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        ChangeSetResults results = performer.perform(inputMock, outputMock);

        // then
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(0, results.getDeleted().size());
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenAdded("file2.txt"));
    }

    @Test(expected = IOException.class)
    public void testPerform_IOException() throws IOException {
        // given
        ArchiveEntry addEntryMock = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock).thenThrow(new IOException());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        performer.perform(inputMock, outputMock);

        // then
        fail("Exception expected but not thrown");
    }

    @Test
    public void testPerform_SameNameAddAndDeleteDir() throws IOException {
        // given
        ArchiveEntry addEntryMock = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock.getName()).thenReturn("dir1/file1.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("dir1/file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        performer.perform(inputMock, outputMock);

        // then
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded("file1.txt"));
        assertTrue(results.hasBeenAdded("dir1/file1.txt"));
    }

    @Test
    public void testPerform_AddAndDeleteDir() throws IOException {
        // given
        ArchiveEntry addEntryMock = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock.getName()).thenReturn("dir1/file1.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("dir1/");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        performer.perform(inputMock, outputMock);

        // then
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("dir1/"));
        assertFalse(results.hasBeenAdded("dir1/file1.txt"));
    }

    @Test
    public void testPerform_AddAndDeleteSameFileWithReplaceMode() throws IOException {
        // given
        ArchiveEntry addEntryMock1 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry addEntryMock2 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock1.getName()).thenReturn("file1.txt");
        Mockito.when(addEntryMock2.getName()).thenReturn("file1.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock1).thenReturn(addEntryMock2).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        Change addChangeWithReplace = new Change("file1.txt", Change.TYPE_ADD, null, null, null, Change.REPLACE_MODE_ON);
        changes.removeIf(change -> change.getName().equals("file1.txt") && change.isReplaceMode() == false);
        performer = new ChangeSetPerformer(new ChangeSet(changes));

        // when
        performer.perform(inputMock, outputMock);

        // then
        assertEquals(1, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
    }

    @Test
    public void testPerform_AddAndDeleteSameFileWithoutReplaceMode() throws IOException {
        // given
        ArchiveEntry addEntryMock1 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry addEntryMock2 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock1.getName()).thenReturn("file1.txt");
        Mockito.when(addEntryMock2.getName()).thenReturn("file1.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock1).thenReturn(addEntryMock2).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        performer.perform(inputMock, outputMock);

        // then
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
    }

    @Test
    public void testPerform_AddAndDeleteDifferentFiles() throws IOException {
        // given
        ArchiveEntry addEntryMock1 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry addEntryMock2 = Mockito.mock(ArchiveEntry.class);
        ArchiveEntry deleteEntryMock = Mockito.mock(ArchiveEntry.class);
        Mockito.when(addEntryMock1.getName()).thenReturn("file1.txt");
        Mockito.when(addEntryMock2.getName()).thenReturn("file2.txt");
        Mockito.when(deleteEntryMock.getName()).thenReturn("file1.txt");
        Mockito.when(inputMock.getNextEntry()).thenReturn(addEntryMock1).thenReturn(addEntryMock2).thenReturn(deleteEntryMock).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outputMock.getOutputStream()).thenReturn(outputStream);

        // when
        performer.perform(inputMock, outputMock);

        // then
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertFalse(results.hasBeenAdded("file1.txt"));
        assertFalse(results.hasBeenAdded("file2.txt"));
    }
}