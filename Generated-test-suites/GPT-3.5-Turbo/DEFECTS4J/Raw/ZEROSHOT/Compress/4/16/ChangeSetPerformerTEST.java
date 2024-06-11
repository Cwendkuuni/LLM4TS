import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
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

    @Test
    public void testPerform() throws IOException {
    	ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(Mockito.mock(ChangeSet.class));
        ArchiveInputStream in = Mockito.mock(ArchiveInputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ChangeSetResults results = changeSetPerformer.perform(in, out);
        Assert.assertNotNull(results);
    }

    @Test
    public void testIsDeletedLater() {
    	ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(Mockito.mock(ChangeSet.class));
    	Set workingSet = new LinkedHashSet();
    	ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
    	changeSetPerformer.isDeletedLater(workingSet, entry);
    }

    @Test
    public void testCopyStream() throws IOException {
    	ChangeSetPerformer changeSetPerformer = new ChangeSetPerformer(Mockito.mock(ChangeSet.class));
    	InputStream in = Mockito.mock(InputStream.class);
        ArchiveOutputStream out = Mockito.mock(ArchiveOutputStream.class);
        ArchiveEntry entry = Mockito.mock(ArchiveEntry.class);
        changeSetPerformer.copyStream(in, out, entry);
        Mockito.verify(out, Mockito.times(1)).putArchiveEntry(entry);
        Mockito.verify(IOUtils.class, Mockito.times(1)).copy(in, out);
        Mockito.verify(out, Mockito.times(1)).closeArchiveEntry();
    }

}