import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class ChangeSetPerformerTest {

    @Mock
    private ChangeSet changeSet;

    @Mock
    private ArchiveInputStream archiveInputStream;

    @Mock
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPerform_AddReplaceMode() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }

    @Test
    public void testPerform_Delete() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }

    @Test
    public void testPerform_DeleteDir() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }

    @Test
    public void testIsDeletedLater_True() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }

    @Test
    public void testIsDeletedLater_False() {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }

    @Test
    public void testCopyStream() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        // Add test code here
    }
}