import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    private ArchiveInputStream inputStreamMock;
    private ArchiveOutputStream outputStreamMock;
    private Set<Change> changeSet;

    @BeforeEach
    public void setUp() {
        changeSet = new HashSet<>();
        changeSet.add(new Change(Change.TYPE_ADD, "test.txt", "test.txt", false));
        changeSet.add(new Change(Change.TYPE_DELETE, "test.txt", null, false));
        performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        inputStreamMock = Mockito.mock(ArchiveInputStream.class);
        outputStreamMock = Mockito.mock(ArchiveOutputStream.class);
    }

    @Test
    public void testConstructor() {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet(changeSet));
        Assertions.assertNotNull(performer);
    }

    @Test
    public void testPerform() throws IOException {
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("test.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(outputStreamMock.writeTo(Mockito.any(byte[].class))).thenAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            IOUtils.write( Arrays.asList(new String("Test Content").getBytes()), outputStreamMock);
            return bytes;
        });

        performer.perform(inputStreamMock, outputStreamMock);
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        Mockito.verify(outputStreamMock, Mockito.times(1)).putArchiveEntry(entry);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }

    @Test
    public void testPerformWithAddAndDeleteOperations() throws IOException {
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("test.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(outputStreamMock.writeTo(Mockito.any(byte[].class))).thenAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            IOUtils.write( Arrays.asList(new String("Test Content").getBytes(), new String("Test Content").getBytes()), outputStreamMock);
            return bytes;
        });

        performer.perform(inputStreamMock, outputStreamMock);
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        Mockito.verify(outputStreamMock, Mockito.times(2)).putArchiveEntry(entry);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }

    @Test
    public void testPerformWithNullNextEntry() throws IOException {
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(null);
        Mockito.when(outputStreamMock.writeTo(Mockito.any(byte[].class))).thenAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            IOUtils.write( Arrays.asList(new String("Test Content").getBytes()), outputStreamMock);
            return bytes;
        });

        performer.perform(inputStreamMock, outputStreamMock);
        Mockito.verify(outputStreamMock, Mockito.times(1)).putArchiveEntry(Mockito.any(ArchiveEntry.class));
    }

    @Test(expected = IOException.class)
    public void testPerformWithIOException() throws IOException {
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("test.txt"));
        Mockito.when(inputStreamMock.readAllBytes()).thenThrow(new IOException("Read Error"));

        performer.perform(inputStreamMock, outputStreamMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPerformWithUnsupportedStreamOperation() {
        Mockito.when(inputStreamMock.getNextEntry()).thenReturn(new ArchiveEntry("test.txt"));

        ArchiveInputStream inputStreamMockThrows = Mockito.mock(ArchiveInputStream.class);
        Mockito.when(inputStreamMockThrows.readAllBytes()).thenReturn(new byte[0]);
        Mockito.when(inputStreamMockThrows.writeTo(Mockito.any(byte[].class))).thenThrow(new UnsupportedOperationException("Write Operation Not Supported"));

        performer.perform(inputStreamMockThrows, outputStreamMock);
    }

    // Add more test cases if needed
}