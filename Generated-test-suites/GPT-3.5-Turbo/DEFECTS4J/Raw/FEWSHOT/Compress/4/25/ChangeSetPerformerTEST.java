import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChangeSetPerformerTest {

    @Mock
    private ArchiveInputStream mockInputStream;
    
    @Mock
    private ArchiveOutputStream mockOutputStream;
    
    private ChangeSetPerformer performer;
    private ChangeSet changeSet;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        changeSet = new ChangeSet();
        changeSet.getChanges().addAll(Arrays.asList(new Change("test1", Change.TYPE_ADD, "file1"), 
                                                   new Change("test2", Change.TYPE_ADD, "file2"), 
                                                   new Change("test3", Change.TYPE_DELETE, "file3")));
        performer = new ChangeSetPerformer(changeSet);
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetResults results = performer.perform(mockInputStream, mockOutputStream);
        
        assertEquals(2, results.getAddedFromChangeSet().size());
        assertEquals(1, results.getDeleted().size());
        assertNull(results.getAddedFromStream());
    }
    
    @Test
    public void testIsDeletedLater() {
        Set<Change> workingSet = new LinkedHashSet<>();
        
        ArchiveEntry testEntry = new ArchiveEntry("testFile");
        ArchiveEntry deleteEntry = new ArchiveEntry("deleteFile");
        
        workingSet.add(new Change("test1", Change.TYPE_DELETE, "deleteFile"));
        
        boolean deletedLater = performer.isDeletedLater(workingSet, deleteEntry);
        assertEquals(true, deletedLater);
        
        deletedLater = performer.isDeletedLater(workingSet, testEntry);
        assertEquals(false, deletedLater);
    }
    
    @Test
    public void testCopyStream() throws IOException {
        ArchiveEntry testEntry = new ArchiveEntry("testFile");
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        
        performer.copyStream(inputStream, mockOutputStream, testEntry);
    }
}