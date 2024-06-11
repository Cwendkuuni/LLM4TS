package org.apache.commons.compress.changes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testChangeSetPerformer() {
        ChangeSet changeSet = mockChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        assertEquals(changeSet.getChanges(), performer.getChanges());
    }

    @Test
    public void testPerform_noChanges() throws IOException {
        ChangeSet changeSet = mockChangeSet();
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        performer.perform(in, out);
        verifyNoChanges(performer);
    }

    @Test
    public void testPerform_addFile() throws IOException {
        String inputData = "Hello World!";
        String fileName = "testFile.txt";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArchiveEntry inputEntry = new TarArchiveEntry(fileName);
        Change addChange = new Change(Change.TYPE_ADD, fileName, null, null, null, null, null, false);
        Set changes = new HashSet();
        changes.add(addChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(inputEntry);
        when(inputEntry.getName()).thenReturn(fileName);
        when(inputEntry.getSize()).thenReturn(inputData.length());
        performer.perform(in, out);
        verifyAddedFromChangeSet(performer, fileName);
        verifyStreamed(performer, inputData.getBytes(), fileName);
    }

    @Test
    public void testPerform_addFileWithReplaceMode() throws IOException {
        String inputData = "Hello World!";
        String fileName = "testFile.txt";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArchiveEntry inputEntry = new TarArchiveEntry(fileName);
        Change addChange = new Change(Change.TYPE_ADD, fileName, null, null, null, null, null, true);
        Set changes = new HashSet();
        changes.add(addChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(inputEntry);
        when(inputEntry.getName()).thenReturn(fileName);
        when(inputEntry.getSize()).thenReturn(inputData.length());
        performer.perform(in, out);
        verifyAddedFromChangeSetReplaced(performer, fileName);
        verifyStreamed(performer, inputData.getBytes(), fileName);
    }

    @Test
    public void testPerform_deleteFile() throws IOException {
        String fileName = "testFile.txt";
        ArchiveEntry inputEntry = new TarArchiveEntry(fileName);
        Change deleteChange = new Change(Change.TYPE_DELETE, null, null, null, null, null, null, null);
        Set changes = new HashSet();
        changes.add(deleteChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(inputEntry);
        when(inputEntry.getName()).thenReturn(fileName);
        performer.perform(in, out);
        verifyDeleted(performer, fileName);
    }

    @Test
    public void testPerform_deleteDir() throws IOException {
        String dirName = "testDir/";
        ArchiveEntry inputEntry = new TarArchiveEntry(dirName + "testFile.txt");
        Change deleteDirChange = new Change(Change.TYPE_DELETE_DIR, null, null, null, null, null, null, null);
        Set changes = new HashSet();
        changes.add(deleteDirChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(inputEntry);
        when(inputEntry.getName()).thenReturn(dirName + "testFile.txt");
        performer.perform(in, out);
        verifyDeletedDir(performer, dirName);
    }

    @Test
    public void testPerform_copyFile() throws IOException {
        String inputData = "Hello World!";
        String srcFileName = "srcFile.txt";
        String dstFileName = "dstFile.txt";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArchiveEntry srcEntry = new TarArchiveEntry(srcFileName);
        ArchiveEntry dstEntry = new TarArchiveEntry(dstFileName);
        Change addChange = new Change(Change.TYPE_ADD, srcFileName, null, null, null, null, null, false);
        Change copyChange = new Change(Change.TYPE_COPY, srcFileName, dstFileName, null, null, null, null, false);
        Set changes = new HashSet();
        changes.add(addChange);
        changes.add(copyChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(srcEntry).thenReturn(dstEntry);
        when(srcEntry.getName()).thenReturn(srcFileName);
        when(dstEntry.getName()).thenReturn(dstFileName);
        when(srcEntry.getSize()).thenReturn(inputData.length());
        performer.perform(in, out);
        verifyAddedFromChangeSet(performer, srcFileName);
        verifyAddedFromChangeSet(performer, dstFileName);
        verifyStreamed(performer, inputData.getBytes(), srcFileName);
        verifyStreamed(performer, inputData.getBytes(), dstFileName);
    }

    @Test
    public void testPerform_multipleChanges() throws IOException {
        String inputData = "Hello World!";
        String srcFileName = "srcFile.txt";
        String dstFileName = "dstFile.txt";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        ArchiveEntry srcEntry = new TarArchiveEntry(srcFileName);
        ArchiveEntry dstEntry = new TarArchiveEntry(dstFileName);
        Change addChange = new Change(Change.TYPE_ADD, srcFileName, null, null, null, null, null, false);
        Change copyChange = new Change(Change.TYPE_COPY, srcFileName, dstFileName, null, null, null, null, false);
        Set changes = new HashSet();
        changes.add(addChange);
        changes.add(copyChange);
        ChangeSet changeSet = new ChangeSet(changes);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ArchiveInputStream in = mockArchiveInputStream();
        ArchiveOutputStream out = mockArchiveOutputStream();
        when(in.getNextEntry()).thenReturn(srcEntry).thenReturn(srcEntry).thenReturn(dstEntry);
        when(srcEntry.getName()).thenReturn(srcFileName).thenReturn(srcFileName);
        when(dstEntry.getName()).thenReturn(dstFileName);
        when(srcEntry.getSize()).thenReturn(inputData.length()).thenReturn(inputData.length());
        performer.perform(in, out);
        verifyAddedFromChangeSet(performer, srcFileName);
        verifyAddedFromChangeSet(performer, dstFileName);
        verifyStreamed(performer, inputData.getBytes(), srcFileName);
        verifyStreamed(performer, inputData.getBytes(), dstFileName);
    }

    private void verifyAddedFromChangeSet(ChangeSetPerformer performer, String fileName) {
        ChangeSetResults results = performer.getResults();
        assertTrue(results.hasBeenAdded(fileName));
    }

    private void verifyAddedFromChangeSetReplaced(ChangeSetPerformer performer, String fileName) {
        ChangeSetResults results = performer.getResults();
        assertTrue(results.hasBeenAddedReplaced(fileName));
    }

    private void verifyDeleted(ChangeSetPerformer performer, String fileName) {
        ChangeSetResults results = performer.getResults();
        assertTrue(results.hasBeenDeleted(fileName));
    }

    private void verifyDeletedDir(ChangeSetPerformer performer, String dirName) {
        ChangeSetResults results = performer.getResults();
        assertTrue(results.hasBeenDeletedDir(dirName));
    }

    private void verifyStreamed(ChangeSetPerformer performer, byte[] data, String fileName) throws IOException {
        InputStream inputStream = performer.getInputStream();
        byte[] buffer = new byte[data.length];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
        assertEquals(fileName, performer.getLastPerformedFile());
    }

    private void verifyNoChanges(ChangeSetPerformer performer) {
        ChangeSetResults results = performer.getResults();
        assertTrue(Collection.class.isAssignableFrom(results.getClass()));
        assertEquals(0, results.size());
    }

    private ArchiveInputStream mockArchiveInputStream() {
        return mock(ArchiveInputStream.class);
    }

    private ArchiveOutputStream mockArchiveOutputStream() {
        return mock(ArchiveOutputStream.class);
    }

    private ChangeSet mockChangeSet() {
        Set changes = new HashSet();
        ChangeSet changeSet = new ChangeSet(changes);
        return changeSet;
    }
}