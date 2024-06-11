import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner)
@PrepareForTest({ChangeSetPerformer.class})
public class ChangeSetPerformerTest {
    private static ChangeSet changeSet;
    private static InputStream inputStream;
    private static ByteArrayOutputStream outputStream;
    private static ArchiveInputStream in;
    private static ArchiveOutputStream out;

    @BeforeClass
    public static void setup() throws Exception {
        changeSet = new ChangeSet();
        changeSet.addChange(new Change(Change.TYPE_ADD, "file1.txt", null, new ByteArrayInputStream(new byte[10])));
        changeSet.addChange(new Change(Change.TYPE_DELETE, "file2.txt", null, null));
        changeSet.addChange(new Change(Change.TYPE_ADD, "file3.txt", null, new ByteArrayInputStream(new byte[20])) {
            {
                setReplaceMode(true);
            }
        });

        inputStream = new ByteArrayInputStream(new byte[50]);
        outputStream = new ByteArrayOutputStream();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    @Test
    public void testPerform_WithEmptyChangeSet() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(new ChangeSet());
        ChangeSetResults result = performer.perform(in, out);
        assertEquals(Collections.emptyList(), result.getAddedFromChangeSet());
        assertEquals(Collections.emptyList(), result.getDeleted());
        assertEquals(Collections.emptyList(), result.getAddedFromStream());
    }

    @Test
    public void testPerform_WithSimpleChangeSet() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(in, out);

        // Added from stream and change set
        assertEquals("file1.txt", result.getAddedFromStream().get(0));
        assertEquals("file3.txt", result.getAddedFromChangeSet().get(0));

        // Deleted
        assertEquals("file2.txt", result.getDeleted().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_WithNullInput() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_WithNullOutput() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, null);
    }

    @Test(expected = IOException.class)
    public void testPerform_WithIOException() throws IOException {
        byte[] data = {3, 4, 5};
        inputStream = new ByteArrayInputStream(data);
        outputStream = new ByteArrayOutputStream();
        in = new ArchiveInputStream(inputStream);
        out = new ArchiveOutputStream(outputStream);
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        performer.perform(in, out);

        // Ensure the method throws an IOException
        performer.perform(in, out);

        // The method should not add any entries to the output stream
        assertEquals(Collections.emptyList(), Arrays.asList(IOUtils.toByteArray(outputStream.toByteArray())));
    }

    @Test
    public void testPerform_WithDeleteLater() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(in, out);

        // Added from stream and change set
        assertEquals("file1.txt", result.getAddedFromStream().get(0));
        assertEquals("file3.txt", result.getAddedFromChangeSet().get(0));

        // Deleted
        assertEquals("file2.txt", result.getDeleted().get(0));

        // Check that file3.txt wasn't added twice
        assertEquals(1, result.getAddedFromChangeSet().size());
    }

    @Test
    public void testPerform_WithReplaceMode() throws IOException {
        ChangeSetPerformer performer = new ChangeSetPerformer(changeSet);
        ChangeSetResults result = performer.perform(in, out);

        // Added from stream and change set
        assertEquals("file1.txt", result.getAddedFromStream().get(0));
        assertEquals("file3.txt", result.getAddedFromChangeSet().get(0));

        // Check that file3.txt was replaced
        assertNotNull(result.getAddedFromStream().get(1));
        assertEquals("file3.txt", result.getAddedFromStream().get(1).getName());
    }
}