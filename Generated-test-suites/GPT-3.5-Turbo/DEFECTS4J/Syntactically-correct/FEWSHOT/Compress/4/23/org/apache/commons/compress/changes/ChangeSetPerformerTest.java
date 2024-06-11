package org.apache.commons.compress.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ChangeSetPerformerTest {
    
    private ChangeSetPerformer performerMock;
    private ChangeSet changeSetMock;
    private ArchiveInputStream inMock;
    private ArchiveOutputStream outMock;
    
    @Before
    public void setUp() {
        changeSetMock = mock(ChangeSet.class);
        performerMock = new ChangeSetPerformer(changeSetMock);
        inMock = mock(ArchiveInputStream.class);
        outMock = mock(ArchiveOutputStream.class);
    }
    
    @Test
    public void testConstructor() {
        assertTrue(performerMock instanceof ChangeSetPerformer);
    }
    
    @Test
    public void testPerform() throws IOException {
        ArchiveEntry entryMock = mock(ArchiveEntry.class);
        Change changeMock = mock(Change.class);
        
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Mockito.when(changeMock.getInput()).thenReturn(inputStream);
        Mockito.when(changeSetMock.getChanges()).thenReturn(getChangesSet(changeMock));
        Mockito.when(changeMock.type()).thenReturn(Change.TYPE_ADD);
        Mockito.when(changeMock.isReplaceMode()).thenReturn(true);
        Mockito.when(changeMock.getEntry()).thenReturn(entryMock);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(outMock.putArchiveEntry(Mockito.any(ArchiveEntry.class))).thenReturn(null);
        Mockito.when(outMock.closeArchiveEntry()).thenReturn(null);
        
        ChangeSetResults results = performerMock.perform(inMock, outMock);
        
        assertNotNull(results);
    }
    
    private Set<Change> getChangesSet(Change change) {
        Set<Change> changes = new LinkedHashSet<Change>();
        changes.add(change);
        return changes;
    }
}