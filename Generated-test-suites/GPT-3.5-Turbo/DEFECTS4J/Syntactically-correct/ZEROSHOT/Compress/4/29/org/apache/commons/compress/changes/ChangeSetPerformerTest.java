import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.changes.Change;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {
    @Test
    public void testPerform() throws IOException {
        // Arrange
        ChangeSet changeSet = new ChangeSet();
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(changeSet);

        ArchiveInputStream in = null;
        ArchiveOutputStream out = null;

        // Act
        ChangeSetResults results = changeSetPerformer.perform(in, out);

        // Assert
        assertNotNull(results);
        // Add additional assertions as needed

    }

    @Test
    public void testIsDeletedLater() {
        // Arrange
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer();

        Set<Change> workingSet = new LinkedHashSet<>();
        ArchiveEntry entry = new ArchiveEntry();

        // Act
        boolean result = changeSetPerformer.isDeletedLater(workingSet, entry);

        // Assert
        assertFalse(result);
        // Add additional assertions as needed

    }

    @Test
    public void testCopyStream() throws IOException {
        // Arrange
        ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer();

        InputStream in = new ByteArrayInputStream(new byte[]{});
        ArchiveOutputStream out = new ArchiveOutputStream();
        ArchiveEntry entry = new ArchiveEntry();

        // Act
        changeSetPerformer.copyStream(in, out, entry);

        // Assert
        // Add assertions as needed

    }
}