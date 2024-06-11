import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;

    @Before
    public void setUp() {
        ChangeSet changeSet = new ChangeSet();
        Set<Change> changes = new LinkedHashSet<>();
        // Add some sample changes for testing
        changes.add(new Change(Change.TYPE_ADD, "file1.txt", false));
        changes.add(new Change(Change.TYPE_DELETE, "file2.txt", false));
        changes.add(new Change(Change.TYPE_DELETE_DIR, "folder", false));
        changeSet.setChanges(changes);
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testConstructor() {
        assertNotNull(performer);
    }

    @Test
    public void testPerform() throws IOException {
        String content = "Hello World!";
        byte[] data = content.getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ArchiveInputStream in = new ArchiveInputStream(input);
        ArchiveOutputStream out = new ArchiveOutputStream(output);

        ChangeSetResults results = performer.perform(in, out);

        // Assert that the output stream contains the expected data
        byte[] resultData = output.toByteArray();
        String resultContent = new String(resultData);
        assertEquals(content, resultContent);

        // Assert that the results object is not null
        assertNotNull(results);
    }
}