package org.apache.commons.compress.changes;

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
import org.apache.commons.compress.archivers. Archives;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerformAddReplaceMode() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change replaceChange = new ReplaceChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(replaceChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);

        assertEquals(new String(result), new String(inputStreamOut.toByteArray()));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddNotReplaceMode() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change addChange2 = new AddChange("test2.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(addChange2);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);

        assertEquals(new String(result), new String(inputStreamOut.toByteArray()));
        verify(archiveOutputStream, times(2)).putNextEntry(any(ArchiveEntry.class));
        verify(archiveOutputStream, times(2)).closeArchiveEntry();
    }

    @Test
    public void testPerformDelete() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformDeleteDir() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test/test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test/test.txt");
        Change deleteDirChange = new DeleteDirChange("test");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformOrderOfOperations() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change addChange2 = new AddChange("test2.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(addChange2);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddAndDelete() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change addChange2 = new AddChange("test2.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(addChange2);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddDirAndDeleteDir() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test/test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);

        ChangeSet changeSet = new DefaultChangeSet();
        Change addDirChange = new AddDirChange("test");
        Change deleteDirChange = new DeleteDirChange("test");
        changeSet.addChange(addDirChange);
        changeSet.addChange(deleteDirChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new ZipInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddAndDeleteWithStream() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream testStream = performer.perform(new ZipFile("src/test/resources/test.zip"), new ZipOutputStream(new ByteArrayOutputStream()));
        byte[] result = IOUtils.toByteArray(testStream.getInputStream(testStream.getEntry("test.txt")));
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddAndDeleteWithTar() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveOutputStream archiveOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(new TarInputStream(new ByteArrayInputStream(changeSet.saveToByteArray())), new TarOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddAndDeleteWithArchiveInputstream() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveInputStream archiveInputStream = new JarFile("src/test/resources/test.jar").getInputStream("test.txt");
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(archiveInputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.close();

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(archiveInputStream, new ZipOutputStream(new ByteArrayOutputStream()))[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }

    @Test
    public void testPerformAddAndDeleteWithArchiveOutputStream() throws IOException {
        ByteArrayOutputStream inputStreamOut = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ArchiveInputStream archiveInputStream = new ZipInputStream(new ByteArrayInputStream(new byte[0]));
        ArchiveOutputStream archiveOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new ZipEntry("test.txt");
        IOUtils.copy(inputStream, archiveOutputStream, null);
        archiveOutputStream.putNextEntry(archiveEntry);

        ChangeSet changeSet = new DefaultChangeSet();
        Change addChange = new AddChange("test.txt");
        Change deleteChange = new DeleteChange("test.txt");
        changeSet.addChange(addChange);
        changeSet.addChange(deleteChange);

        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);

        InputStream testStream = performer.perform(archiveInputStream, archiveOutputStream)[0];
        byte[] result = IOUtils.toByteArray(testStream);
        assertTrue(Arrays.equals(new byte[0][0], result));
        verify(archiveOutputStream, times(1)).closeArchiveEntry();
    }
}