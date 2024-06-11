import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform_noChanges() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(changeSet.getChanges()).thenReturn(Collections.emptySet());

        ChangeSetResults result = performer.perform(inputMock, outputMock);

        Assert.assertEquals(0, result.getAddedFromChangeSet().size());
        Assert.assertEquals(0, result.getDeleted().size());
        Assert.assertEquals(0, result.getAddedFromStream().size());
    }

    @Test
    public void testPerform_add() {
        ChangeSet changeSet = new ChangeSet() {
            @Override
            public Set getChanges() {
                Set changes = new HashSet<>();
                changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));
                return changes;
            }
        };

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        when(outputMock.getArchiveEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        when(outputMock.writeTo(ArgumentMatchers.any(OutputStream.class))).thenAnswer(invocation -> invocation.getArgument(0).write(output.toByteArray()));
        when(outputMock.finishEntry()).thenReturn(true);

        ChangeSetResults result = performer.perform(inputMock, outputMock);

        Assert.assertEquals(1, result.getAddedFromChangeSet().size());
        Assert.assertEquals("testFile.txt", result.getAddedFromChangeSet().iterator().next());
        Assert.assertEquals(0, result.getDeleted().size());
        Assert.assertEquals(0, result.getAddedFromStream().size());
    }

    @Test
    public void testPerform_delete() {
        ChangeSet changeSet = new ChangeSet() {
            @Override
            public Set getChanges() {
                Set changes = new HashSet<>();
                changes.add(new Change("testFile.txt", Change.TYPE_DELETE, null, null, null));
                return changes;
            }
        };

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));

        ChangeSetResults result = performer.perform(inputMock, outputMock);

        Assert.assertEquals(0, result.getAddedFromChangeSet().size());
        Assert.assertEquals(1, result.getDeleted().size());
        Assert.assertEquals("testFile.txt", result.getDeleted().iterator().next());
        Assert.assertEquals(0, result.getAddedFromStream().size());
    }

    @Test(expected = IOException.class)
    public void testPerform_inputStreamError() {
        ChangeSet changeSet = new ChangeSet() {
            @Override
            public Set getChanges() {
                Set changes = new HashSet<>();
                changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));
                return changes;
            }
        };

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);
        when(inputMock.read()).thenThrow(new IOException("Test error message"));

        ChangeSetResults result = performer.perform(inputMock, outputMock);
    }

    @Test(expected = IOException.class)
    public void testPerform_outputStreamError() {
        ChangeSet changeSet = new ChangeSet() {
            @Override
            public Set getChanges() {
                Set changes = new HashSet<>();
                changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));
                return changes;
            }
        };

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        when(outputMock.getArchiveEntry()).thenReturn(new ArchiveEntry("testFile.txt"));
        when(outputMock.writeTo(ArgumentMatchers.any(OutputStream.class))).thenAnswer(invocation -> {
            throw new IOException("Test error message");
        });

        ChangeSetResults result = performer.perform(inputMock, outputMock);
    }

    @Test
    public void testPerform_multipleChanges() {
        ChangeSet changeSet = new ChangeSet() {
            @Override
            public Set getChanges() {
                Set changes = new HashSet<>();
                changes.add(new Change("testFile1.txt", Change.TYPE_ADD, null, null, null));
                changes.add(new Change("testFile2.txt", Change.TYPE_DELETE, null, null, null));
                changes.add(new Change("testDir/testFile3.txt", Change.TYPE_ADD, null, null, null));
                return changes;
            }
        };

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream inputMock = mock(ArchiveInputStream.class);
        ArchiveOutputStream outputMock = mock(ArchiveOutputStream.class);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile1.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input1 = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testFile2.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input2 = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);

        when(inputMock.getNextEntry()).thenReturn(new ArchiveEntry("testDir/testFile3.txt"));
        when(inputMock.available()).thenReturn(1024);
        ByteArrayInputStream input3 = new ByteArrayInputStream(new byte[1024]);
        when(inputMock.wrap(ArgumentMatchers.any(InputStream.class))).thenReturn(inputMock);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        when(outputMock.getArchiveEntry()).thenReturn(new ArchiveEntry("testFile1.txt"));
        when(outputMock.writeTo(ArgumentMatchers.any(OutputStream.class))).thenAnswer(invocation -> {
            invocation.getArgument(0).write(input1.readAllBytes());
            return invocation.getArgument(0).size();
        });
        when(outputMock.finishEntry()).thenReturn(true);

        when(outputMock.getArchiveEntry()).thenReturn(new ArchiveEntry("testDir/testFile3.txt"));
        when(outputMock.writeTo(ArgumentMatchers.any(OutputStream.class))).thenAnswer(invocation -> {
            invocation.getArgument(0).write(input3.readAllBytes());
            return invocation.getArgument(0).size();
        });
        when(outputMock.finishEntry()).thenReturn(true);

        ChangeSetResults result = performer.perform(inputMock, outputMock);

        Assert.assertEquals(3, result.getAddedFromChangeSet().size());
        Assert.assertEquals("testFile1.txt", result.getAddedFromChangeSet().iterator().next());
        Assert.assertEquals("testDir/testFile3.txt", result.getAddedFromChangeSet().iterator().next());
        Assert.assertEquals("testFile1.txt", result.getAddedFromStream().iterator().next());
        Assert.assertEquals("testDir/testFile3.txt", result.getAddedFromStream().iterator().next());
        Assert.assertEquals(1, result.getDeleted().size());
        Assert.assertEquals("testFile2.txt", result.getDeleted().iterator().next());
    }

    @Test
    public void testIsDeletedLater_noDeletion() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        Set changes = new HashSet<>();
        changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));
        changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));

        Assert.assertFalse(performer.isDeletedLater(changes, new ArchiveEntry("testFile.txt")));
    }

    @Test
    public void testIsDeletedLater_deletionBeforeAdd() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        Set changes = new HashSet<>();
        changes.add(new Change("testFile.txt", Change.TYPE_DELETE, null, null, null));
        changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));

        ArchiveEntry entry = new ArchiveEntry("testFile.txt");

        Assert.assertTrue(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLater_noMatchingDeletion() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        Set changes = new HashSet<>();
        changes.add(new Change("testFile1.txt", Change.TYPE_DELETE, null, null, null));
        changes.add(new Change("testFile2.txt", Change.TYPE_ADD, null, null, null));

        ArchiveEntry entry = new ArchiveEntry("testFile1.txt");

        Assert.assertFalse(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLater_matchingDeletion() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        Set changes = new HashSet<>();
        changes.add(new Change("testFile.txt", Change.TYPE_DELETE, null, null, null));
        changes.add(new Change("testFile.txt", Change.TYPE_ADD, null, null, null));

        ArchiveEntry entry = new ArchiveEntry("testFile.txt");

        Assert.assertTrue(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testIsDeletedLater_deletionDirectory() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        Set changes = new HashSet<>();
        changes.add(new Change("testDir/testFile.txt", Change.TYPE_DELETE, null, null, null));
        changes.add(new Change("testDir/testFile.txt", Change.TYPE_ADD, null, null, null));

        ArchiveEntry entry = new ArchiveEntry("testDir/testFile.txt");

        Assert.assertTrue(performer.isDeletedLater(changes, entry));
    }

    @Test
    public void testCopyStream() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());

        InputStream input = new ByteArrayInputStream(Collections.singletonList((byte) 0x01).toArray(new byte[1]));
        ArchiveOutputStream output = new ArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new ArchiveEntry("testFile.txt");

        performer.copyStream(input, output, entry);

        byte[] expected = new byte[]{0x01};
        byte[] actual = Arrays.copyOfRange(output.toByteArray(), 0, output.size());

        Assert.assertArrayEquals(expected, actual);
    }
}