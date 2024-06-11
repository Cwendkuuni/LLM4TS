import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ChangeSetPerformerTest {
    private final ByteArrayInputStream inputStream;
    private final ByteArrayOutputStream outputStream;
    private final ArchiveInputStream expectedInput;
    private final ArchiveOutputStream expectedOutput;
    private final ChangeSetPerformer performer;

    public ChangeSetPerformerTest(ByteArrayInputStream inputStream,
                                 ByteArrayOutputStream outputStream,
                                 ArchiveInputStream expectedInput,
                                 ArchiveOutputStream expectedOutput) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.expectedInput = expectedInput;
        this.expectedOutput = expectedOutput;
        this.performer = new ChangeSetPerformer(new InMemoryChangeSet(Arrays.asList(
                new AddChange("test.txt", "test data".getBytes(StandardCharsets.UTF_8)),
                new DeleteChange("test.txt"))));
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {createInputStream("test1.zip", "input1.txt", "output1.txt"),
                 createOutputStream("test1.zip"),
                 createInputStream("test1_expected.zip"),
                 createOutputStream("test1_expected.zip")},
                {createInputStream("test2.zip", "input2.txt", "output2.txt"),
                 createOutputStream("test2.zip"),
                 createInputStream("test2_expected.zip"),
                 createOutputStream("test2_expected.zip")}
        });
    }

    @Test
    public void testPerform() throws IOException {
        ChangeSetResults results = performer.perform(expectedInput, expectedOutput);

        Assert.assertArrayEquals(inputStream.readAllBytes(), expectedInput.readAllBytes());
        Assert.assertArrayEquals(outputStream.toByteArray(), expectedOutput.readAllBytes());
        Assert.assertEquals(results.getAddedFromChangeSet(), expectedGetAddedFromChangeSet());
        Assert.assertEquals(results.getDeleted(), expectedGetDeleted());
        Assert.assertEquals(results.getAddedFromStream(), expectedGetAddedFromStream());
    }

    private static ArchiveInputStream createInputStream(String fileName, String inputName, String outputName) throws IOException {
        ZipFile zipFile = new ZipFile(new File(fileName));
        ArchiveInputStream inputStream = new ArchiveInputStream(zipFile.getInputStream(zipFile.getEntry(inputName)));
        return inputStream;
    }

    private static ArchiveOutputStream createOutputStream(String fileName) throws IOException {
        return new ArchiveOutputStream(new File(fileName));
    }

    private static byte[] createInputStream(String fileName) throws IOException {
        ZipFile zipFile = new ZipFile(new File(fileName));
        ArchiveInputStream inputStream = new ArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("test.txt")));
        return IOUtils.toByteArray(inputStream);
    }

    private int expectedGetAddedFromChangeSet() {
        return 1;
    }

    private int expectedGetDeleted() {
        return 1;
    }

    private int expectedGetAddedFromStream() {
        return 0;
    }
}