import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ChangeSetPerformerTest {

    @Mock
    private ChangeSet changeSetMock;
    @Mock
    private ArchiveInputStream archiveInputStreamMock;
    @Mock
    private ArchiveOutputStream archiveOutputStreamMock;
    @Mock
    private InputStream inputStreamMock;
    @Mock
    private ArchiveEntry archiveEntryMock;

    private ChangeSetPerformer changeSetPerformer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        changeSetPerformer = new ChangeSetPerformer(changeSetMock);
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetResults results = changeSetPerformer.perform(archiveInputStreamMock, archiveOutputStreamMock);
        assertNotNull(results);
        // add more assertions if needed
    }

    @Test
    public void testIsDeletedLater() {
        Set workingSet = new LinkedHashSet();
        boolean isDeletedLater = changeSetPerformer.isDeletedLater(workingSet, archiveEntryMock);
        assertFalse(isDeletedLater);
        // add more assertions if needed
    }

    @Test
    public void testCopyStream() throws IOException {
        changeSetPerformer.copyStream(inputStreamMock, archiveOutputStreamMock, archiveEntryMock);
        // add assertions to verify the behavior of the method
    }
}