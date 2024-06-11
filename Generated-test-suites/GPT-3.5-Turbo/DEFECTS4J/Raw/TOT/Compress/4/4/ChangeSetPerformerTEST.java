import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.junit.Test;

public class ChangeSetPerformerTest {

    @Test
    public void testPerform() throws IOException, ArchiveException {
        // Prepare test data
        Set<Change> changes = new LinkedHashSet<>();
        
        Change addChange = new Change("test.txt", Change.TYPE_ADD, false);
        ByteArrayInputStream addInput = new ByteArrayInputStream("test".getBytes());
        addChange.setInput(addInput);
        changes.add(addChange);
        
        Change deleteChange = new Change("test.txt", Change.TYPE_DELETE, false);
        changes.add(deleteChange);
        
        Change replaceChange = new Change("test.txt", Change.TYPE_ADD, true);
        ByteArrayInputStream replaceInput = new ByteArrayInputStream("replace".getBytes());
        replaceChange.setInput(replaceInput);
        changes.add(replaceChange);
        
        ChangeSet changeSet = new ChangeSet(changes);
        
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        
        // Create test input stream
        ByteArrayOutputStream inStream = new ByteArrayOutputStream();
        ArchiveOutputStream outStream = new TarArchiveOutputStream(inStream);
        outStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outStream.write("original".getBytes());
        outStream.closeArchiveEntry();
        outStream.finish();
        
        ByteArrayInputStream in = new ByteArrayInputStream(inStream.toByteArray());
        
        // Create test output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream resultStream = new TarArchiveOutputStream(out);
        
        // Perform changes
        ChangeSetResults results = performer.perform(new TarArchiveInputStream(in), resultStream);
        
        // Verify results
        assertNotNull(results);
        
        assertTrue(results.deleted("test.txt"));
        assertFalse(results.hasBeenAdded("test.txt"));
        
        // Verify output stream
        resultStream.finish();
        
        ByteArrayInputStream resultIn = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream resultInStream = new TarArchiveInputStream(resultIn);
        ArchiveEntry entry = resultInStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        byte[] data = new byte[(int) entry.getSize()];
        resultInStream.read(data);
        resultInStream.closeEntry();
        resultInStream.close();
        assertEquals("replace", new String(data));

    }
}