import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ChangeSetPerformerTest {

    private ChangeSetPerformer performer;
    private ArchiveEntry testEntry1;
    private ArchiveEntry testEntry2;
    private ArchiveEntry testEntry3;
    private ArchiveInputStream testInput;
    private ArchiveOutputStream testOutput;
    private ChangeSet changeSet;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws IOException {
        changeSet = createTestChangeSet();
        performer = new ChangeSetPerformer(changeSet);
        testInput = createTestArchive();
        testOutput = new ArchiveOutputStream(new File(tempFolder.newFile("output.tar.gz")));
    }

    @Test
    public void testPerform_addFileWithReplaceMode() throws IOException {
        // Given
        ArchiveEntry expectedOutputEntry = new MyArchiveEntry("expected_output.txt");

        // When
        ChangeSetResults results = performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputEntry = testOutput.getNextEntry();
        assertThat(outputEntry, notNullValue());
        assertThat(outputEntry, equalTo(expectedOutputEntry));
        assertThat(results.getAddedFromChangeSet(), is(Arrays.asList("expected_output.txt")));
    }

    @Test
    public void testPerform_addFileWithoutReplaceMode() throws IOException {
        // Given
        ArchiveEntry expectedOutputEntry1 = new MyArchiveEntry("expected_output1.txt");
        ArchiveEntry expectedOutputEntry2 = new MyArchiveEntry("expected_output2.txt");

        // When
        ChangeSetResults results = performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputEntry1 = testOutput.getNextEntry();
        assertThat(outputEntry1, notNullValue());
        assertThat(outputEntry1, equalTo(expectedOutputEntry1));
        ArchiveEntry outputEntry2 = testOutput.getNextEntry();
        assertThat(outputEntry2, notNullValue());
        assertThat(outputEntry2, equalTo(expectedOutputEntry2));
        assertThat(results.getAddedFromChangeSet(), is(Arrays.asList("expected_output1.txt", "expected_output2.txt")));
    }

    @Test
    public void testPerform_deleteFile() throws IOException {
        // Given
        ArchiveEntry inputEntry = new MyArchiveEntry("input.txt");

        // When
        performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputEntry = testInput.getNextEntry();
        assertThat(outputEntry, nullValue());
        assertThat(testOutput.getNextEntry(), nullValue());
    }

    @Test
    public void testPerform_deleteDirectory() throws IOException {
        // Given
        ArchiveEntry inputDirectoryEntry = new MyArchiveEntry("input_directory/");
        ArchiveEntry inputFileEntry1 = new MyArchiveEntry("input_directory/file1.txt");
        ArchiveEntry inputFileEntry2 = new MyArchiveEntry("input_directory/file2.txt");

        // When
        performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputDirectoryEntry = testInput.getNextEntry();
        ArchiveEntry outputFileEntry1 = testInput.getNextEntry();
        ArchiveEntry outputFileEntry2 = testInput.getNextEntry();

        assertThat(outputDirectoryEntry, nullValue());
        assertThat(outputFileEntry1, nullValue());
        assertThat(outputFileEntry2, nullValue());
    }

    @Test
    public void testPerform_error_whenPerformingAnOperationTwice() throws IOException {
        // Given
        performer.perform(testInput, testOutput);

        // When & Then
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("This ChangeSetPerformer instance has already been used.");

        performer.perform(testInput, testOutput);
    }

    @Test
    public void testPerform_error_whenInputIsEmpty() throws IOException {
        // Given
        testInput = new ArchiveInputStream(new FileInputStream(new File("")));

        // When & Then
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The input ArchiveInputStream is empty.");

        performer.perform(testInput, testOutput);
    }

    @Test
    public void testPerform_error_whenOutputIsEmpty() throws IOException {
        // Given
        testOutput = new ArchiveOutputStream(new FileOutputStream(""));

        // When & Then
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The output ArchiveOutputStream is empty.");

        performer.perform(testInput, testOutput);
    }

    @Test
    public void testPerform_error_whenInputDoesntContainExpectedEntry() throws IOException {
        // Given
        ArchiveEntry inputEntry = new MyArchiveEntry("input.txt");

        // When
        ChangeSetResults results = performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputEntry = testInput.getNextEntry();
        assertThat(outputEntry, nullValue());
        assertThat(results.getDeleted(), is(Collections.emptyList()));
    }

    @Test
    public void testPerform_error_whenOutputContainsUnexpectedEntry() throws IOException {
        // Given
        ArchiveEntry unexpectedOutputEntry = new MyArchiveEntry("unexpected_output.txt");

        // When
        performer.perform(testInput, testOutput);

        // Then
        ArchiveEntry outputEntry = testOutput.getNextEntry();
        assertThat(outputEntry, notNullValue());
        assertThat(outputEntry, not(equalTo(unexpectedOutputEntry)));
        assertThat(results.getAddedFromStream(), is(Collections.emptyList()));
    }

    private ArchiveInputStream createTestArchive() throws IOException {
        ArchiveOutputStream testOutput = new ArchiveOutputStream(new File(tempFolder.newFile("test.tar.gz")));
        testOutput.putNextEntry(new MyArchiveEntry("input.txt"));
        testOutput.write("Test content".getBytes());
        testOutput.closeArchiveEntry();
        testOutput.close();

        return new ArchiveInputStream(new FileInputStream(tempFolder.newFile("test.tar.gz")));
    }

    private ChangeSet createTestChangeSet() {
        List<Change> changes = Arrays.asList(
                new Change(Change.TYPE_ADD, new MyArchiveEntry("expected_output.txt"), null, "expected_output.txt", null, true),
                new Change(Change.TYPE_ADD, new MyArchiveEntry("expected_output1.txt"), null, "expected_output1.txt", null, false),
                new Change(Change.TYPE_ADD, new MyArchiveEntry("expected_output2.txt"), null, "expected_output2.txt", null, false),
                new Change(Change.TYPE_DELETE, null, "input.txt", null, null, null),
                new Change(Change.TYPE_DELETE_DIR, null, "input_directory/", null, null, null)
        );

        Map<String, ChangeSet.ChangeSetEntry> entries = new LinkedHashMap<>();
        for (Change change : changes) {
            entries.put(change.getTargetFile(), new ChangeSet.ChangeSetEntry(change));
        }

        return new ChangeSet(entries);
    }

    private static class MyArchiveEntry extends ArchiveEntry {
        public MyArchiveEntry(String name) {
            super(name);
        }
    }
}