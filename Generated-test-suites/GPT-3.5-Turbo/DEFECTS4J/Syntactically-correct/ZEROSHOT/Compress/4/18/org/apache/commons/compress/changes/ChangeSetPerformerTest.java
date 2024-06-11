import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    private ChangeSetPerformer performer;
    
    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1", true, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file2", true, null));
        changeSet.addChange(new Change(Change.TYPE_DELETE_DIR, "dir1", true, null));
        
        performer = new ChangeSetPerformer(changeSet);
    }
    
    @Test
    public void testPerform() throws IOException {
        ArchiveInputStream in = createArchiveInputStream();
        ArchiveOutputStream out = createArchiveOutputStream();
        
        ChangeSetResults results = performer.perform(in, out);
        
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertTrue(results.getAddedFromChangeSet().contains("file1"));
        assertTrue(results.getAddedFromChangeSet().contains("dir1"));
        assertTrue(results.getDeleted().contains("file2"));
    }
    
    private ArchiveInputStream createArchiveInputStream() {
        byte[] data = new byte[5];
        for (int i = 0; i < 5; i++) {
            data[i] = (byte) (i + 1);
        }
        return new ArchiveInputStream(new ByteArrayInputStream(data));
    }
    
    private ArchiveOutputStream createArchiveOutputStream() {
        return new ArchiveOutputStream(new ByteArrayOutputStream());
    }
}