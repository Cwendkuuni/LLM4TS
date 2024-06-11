import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String TEST_FILE_CONTENTS = "Test file contents.";
    private static final String TEST_FILE_NAME = "test.html";
    private static final String TEST_CHARSET_NAME = "UTF-8";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File(TEST_FILE_NAME);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_FILE_CONTents.getBytes(TEST_CHARSET_NAME));
        outputStream.flush();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(testFile, TEST_CHARSET_NAME, "");
        assertEquals(TEST_FILE_CONTENTS, document.body().text());

        document = DataUtil.load(inputStream, TEST_CHARSET_NAME, "");
        assertEquals(TEST_FILE_CONTENTS, document.body().text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><body>Test data.</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, TEST_CHARSET_NAME, "", Parser.htmlParser());
        assertEquals(testData, document.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        Random random = new Random();

        try (InputStream inputStream = new FileInputStream(inputFile);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            DataUtil.crossStreams(inputStream, outputStream);
            byte[] expected = new byte[inputFile.length()];
            inputStream.read(expected);
            byte[] actual = new byte[outputFile.length()];
            outputStream.read(actual);
            assertEquals(expected, actual);
        }
    }
}